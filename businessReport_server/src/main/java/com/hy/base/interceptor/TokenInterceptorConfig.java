package com.hy.base.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * .
 * author : wellhor Zhao
 * date : 2018-8-12
 * description : Token验证拦截器
 */
@Configuration
public class TokenInterceptorConfig implements WebMvcConfigurer {

    /**
     * .
     * token拦截接口
     */
    private final TokenInterceptor tokenInterceptor;

    /**
     * .
     * 构造函数
     *
     * @param interceptor token拦截接口
     */
    public TokenInterceptorConfig(final TokenInterceptor interceptor) {
        this.tokenInterceptor = interceptor;
    }

    /**
     * .
     * 增加拦截器
     *
     * @param registry 拦截注册器
     */
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/api/**")
            .excludePathPatterns("/recordInfo/**")
            .excludePathPatterns("/*.css")
            .excludePathPatterns("/*.js")
            .excludePathPatterns("/config.json")
            .excludePathPatterns("/connector/**")
            .excludePathPatterns("/zk/**");
    }

}
