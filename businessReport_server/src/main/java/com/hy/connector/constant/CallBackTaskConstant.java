package com.hy.connector.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-25
 * user: lxg
 * package_name: com.hy.constant
 */
@Component
@PropertySource(value = "classpath:scheduler.properties", encoding = "UTF-8")
public class CallBackTaskConstant {

    /**
     * .
     * 回访问卷请求地址
     */
    @Getter
    private static String callBackUrl;

    /**
     * .
     * 回访历史请求地址
     */
    @Getter
    private static String callBackHistoryUrl;


    /**
     * .
     * 上传录音文件地址
     */
    @Getter
    private static String noticeFileUrl;

    /**
     * .
     * 设置上传录音文件地址
     *
     * @param url 上传录音文件地址
     */
    @Value("${noticeFileUrl}")
    public void setNoticeFileUrl(final String url) {
        CallBackTaskConstant.noticeFileUrl = url;
    }

    /**
     * .
     * 回访历史地址
     *
     * @param url 回访历史地址
     */
    @Value("${callBackHistoryUrl}")
    public void setCallBackHistoryUrl(final String url) {
        CallBackTaskConstant.callBackHistoryUrl = url;
    }

    /**
     * .
     * 回访问卷地址
     *
     * @param url 回访问卷地址
     */
    @Value("${callBackUrl}")
    public void setCallBackUrl(final String url) {
        CallBackTaskConstant.callBackUrl = url;
    }
}
