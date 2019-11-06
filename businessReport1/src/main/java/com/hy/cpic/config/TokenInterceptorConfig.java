package com.hy.cpic.config;

import com.hy.cpic.base.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * author : wellhor Zhao
 * date : 2018-8-12
 * description : Token验证拦截器
 */
@Configuration
public class TokenInterceptorConfig implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;

    public TokenInterceptorConfig(TokenInterceptor tokenInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**")
            .excludePathPatterns("/api/**").excludePathPatterns("/recordInfo/**")
            .excludePathPatterns("/*.css").excludePathPatterns("/*.js")
            .excludePathPatterns("/config.json")
            .excludePathPatterns("/voice/**");
    }

}
