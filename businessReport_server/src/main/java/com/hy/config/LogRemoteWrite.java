package com.hy.config;

import cn.haoyitec.common.LogVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * .
 *
 * @author zxd
 * @since 2018/8/10.
 */
final class LogRemoteWrite {
    /**
     * .
     * 工具类隐藏构造函数
     */
    private LogRemoteWrite() {

    }

    /**
     * .
     * 写入值
     *
     * @param url          目的地址
     * @param request      request
     * @param response     response
     * @param methodName   方法名
     * @param e            异常值
     * @param responseTime 响应时间
     * @param clzName      类名
     * @throws IOException IOException
     */
    static void write(final String url,
                      final HttpServletRequest request,
                      final HttpServletResponse response,
                      final String methodName,
                      final Exception e,
                      final long responseTime,
                      final String clzName)
        throws IOException {
        LogVo logVo = new LogVo();
        logVo.setResult("success");
        logVo.setResponseTime(responseTime);
        if (e != null) {
            logVo.setResult("fail");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            logVo.setMsg(sw.toString());
            sw.close();
        }
        logVo.setMethodName(methodName);
        logVo.setClientIp(IpTools.getClientIp(request));
        logVo.setServerIp(request.getServerName());
        String token = request.getParameter("token");
        logVo.setToken(token);
        logVo.setUrl(request.getRequestURL().toString());
        logVo.setClzName(clzName);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity
            = new HttpEntity<>(JSONObject.toJSONString(logVo), headers);
        restTemplate.exchange(url + "?token=" + token,
            HttpMethod.POST,
            entity,
            String.class);
    }
}
