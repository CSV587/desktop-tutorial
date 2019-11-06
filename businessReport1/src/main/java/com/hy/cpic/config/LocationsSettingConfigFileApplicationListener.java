package com.hy.cpic.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.PropertiesPropertySource;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

/**
 * @author zxd
 * @since 2018/8/27.
 */
@Slf4j
@SuppressWarnings("unchecked")
public class LocationsSettingConfigFileApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {
    @Override
    public int getOrder() {
        return ConfigFileApplicationListener.DEFAULT_ORDER - 1;
    }

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        SpringApplication app = event.getSpringApplication();

        for (ApplicationListener<?> listener : app.getListeners()) {
            if (listener instanceof ConfigFileApplicationListener) {
                String searchLocations = event.getEnvironment().getProperty("spring.config.location");
                if (StringUtils.isNotEmpty(searchLocations)) {
                    Properties properties = new Properties();
                    fileToProperties(properties, Paths.get(searchLocations, "application.properties"));
                    yamlToProperties(properties, Paths.get(searchLocations, "application.yaml"));
                    yamlToProperties(properties, Paths.get(searchLocations, "application.yml"));
                    event.getEnvironment().getPropertySources().addFirst(new PropertiesPropertySource("myProps", properties));
                }
            }
        }
    }

    private static void fileToProperties(Properties properties, Path path) {
        if (path.toFile().exists()) {
            try (InputStream in = Files.newInputStream(path)) {
                properties.load(in);
            } catch (IOException e) {
                log.error("", e);
            }
        }
    }

    private static void yamlToProperties(Properties properties, Path path) {
        if (path.toFile().exists()) {
            try (InputStream in = Files.newInputStream(path)) {
                Yaml yaml = new Yaml();
                Map<String, Object> config = yaml.loadAs(in, Map.class);
                toProperties(properties, "", config);
            } catch (IOException e) {
                log.error("", e);
            }
        }
    }

    private static void toProperties(Properties properties, String key, Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String cascadeKey = entry.getKey();
            if (StringUtils.isNotEmpty(key)) {
                cascadeKey = key + "." + cascadeKey;
            }
            if (map.get(entry.getKey()) instanceof Map) {
                toProperties(properties, cascadeKey, (Map<String, Object>) entry.getValue());
            } else {
                properties.put(cascadeKey, map.get(entry.getKey()).toString());
            }
        }
    }
}
