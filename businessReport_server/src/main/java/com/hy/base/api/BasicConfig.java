package com.hy.base.api;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * .
 * 配置文件赋值
 */
@Component
@PropertySource("classpath:system.properties")
public class BasicConfig {

    /**
     * .
     * 权限管理平台请求地址
     */
    @Getter
    @Value("${system.ipm}")
    private static String ipmUrl;

    /**
     * .
     * 呼叫策略平台请求地址
     */
    @Getter
    @Value("${system.icm}")
    private static String icmUrl;

    /**
     * .
     * 运营管理平台请求地址
     */
    @Getter
    @Value("${system.iom}")
    private static String iomUrl;

    /**
     * .
     * ipmUrl 初始化
     *
     * @param url ipm地址
     */
    @Value(value = "${system.ipm}")
    public void setIpmUrl(final String url) {
        BasicConfig.ipmUrl = url;
    }

    /**
     * .
     * icmUrl 初始化
     *
     * @param url icm地址
     */
    @Value(value = "${system.icm}")
    public void setIcmUrl(final String url) {
        BasicConfig.icmUrl = url;
    }

    /**
     * .
     * iomUrl 初始化
     *
     * @param url iom地址
     */
    @Value(value = "${system.iom}")
    public void setIomUrl(final String url) {
        BasicConfig.iomUrl = url;
    }


}
