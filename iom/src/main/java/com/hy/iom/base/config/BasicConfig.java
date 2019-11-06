package com.hy.iom.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 配置文件赋值
 */
@Component
@PropertySource("classpath:system.properties")
public class BasicConfig {

    private static String IPMURL;

    private static String ICMURL;

    private static String IOMURL ;

    private static String tempFileDirPath;

    public static String getIPMURL() {
        return IPMURL;
    }

    @Value("${system.ipm}")
    public void setIPMURL(String IPMURL) {
        BasicConfig.IPMURL = IPMURL;
    }

    public static String getICMURL() {
        return ICMURL;
    }

    @Value("${system.icm}")
    public void setICMURL(String ICMURL) {
        BasicConfig.ICMURL = ICMURL;
    }

    public static String getIOMURL() {
        return IOMURL;
    }

    @Value("${system.iom}")
    public void setIOMURL(String IOMURL) {
        BasicConfig.IOMURL = IOMURL;
    }


    public static String getTempFileDirPath() {
        return tempFileDirPath;
    }

    @Value("${system.tempFileDirPath}")
    public void setTempFileDirPath(String tempFileDirPath) {
        BasicConfig.tempFileDirPath = tempFileDirPath;
    }
}
