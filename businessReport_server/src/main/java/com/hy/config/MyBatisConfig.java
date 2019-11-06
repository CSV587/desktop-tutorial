package com.hy.config;


import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * .
 * MyBaits配置加载
 */
@Configuration
@MapperScan("com.hy.mapper.oracle")
public class MyBatisConfig {

    /**
     * .
     * mybatis 扫描
     *
     * @return ConfigurationCustomizer
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setMapUnderscoreToCamelCase(true);
    }
}
