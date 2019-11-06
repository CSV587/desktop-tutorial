package com.hy;

import com.hy.scheduler.annotation.EnableZkSchedulerAdmin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * .
 * 主类
 */
@EnableZkSchedulerAdmin
@SpringBootApplication
public class StartApplication extends SpringBootServletInitializer {

    /**
     * .
     * 主函数
     *
     * @param args 参数数组
     */
    public static void main(final String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }

    /**
     * .
     * 加载自定义配置
     *
     * @param builder SpringApplicationBuilder
     * @return SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder) {
        return builder.sources(StartApplication.class);
    }

    /**
     * .
     * 兼容jndi
     *
     * @param name jndi名称
     * @return 返回值
     * @throws IllegalArgumentException IllegalArgumentException
     * @throws NamingException          NamingException
     */
    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "spring.datasource", name = "jndi-name")
    public DataSource jndiDataSource(
        @Value("${spring.datasource.jndi-name}") final String name)
        throws IllegalArgumentException,
        NamingException {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName(name);
        bean.setProxyInterface(DataSource.class);
        bean.setLookupOnStartup(false);
        bean.afterPropertiesSet();
        return (DataSource) bean.getObject();
    }
}
