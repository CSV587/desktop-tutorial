package com.hy.config;

import javax.servlet.http.HttpServletRequest;

/**
 * .
 * 获取Ip
 *
 * @author sq
 * @since 2018/7/3
 */
final class IpTools {

    /**
     * .
     * 工具类隐藏构造函数
     */
    private IpTools() {

    }

    /**
     * .
     * 获取登录用户IP地址
     *
     * @param request request
     * @return Ip地址
     */
    static String getClientIp(final HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

}
