package com.hy.cpic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.naming.NamingException;
import javax.sql.DataSource;

@EnableScheduling
@SpringBootApplication
public class CPICApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CPICApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(CPICApplication.class);
    }

    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "spring.datasource", name = "jndi-name")
    public DataSource jndiDataSource(@Value("${spring.datasource.jndi-name}") String name)
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
