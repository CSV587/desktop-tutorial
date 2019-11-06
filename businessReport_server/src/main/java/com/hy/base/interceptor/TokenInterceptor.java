package com.hy.base.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.hy.base.auth.Auth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * .
 * token校验拦截
 * author : wellhor Zhao
 * date ; 2018-8-12
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    /**
     * .
     * 校验类
     */
    private final Auth auth;

    /**
     * .
     * 构造函数
     *
     * @param auth1 校验类
     */
    public TokenInterceptor(final Auth auth1) {
        this.auth = auth1;
    }

    /**
     * .
     * 在目标方法执行前执行
     *
     * @param request  request
     * @param response response
     * @param handler  请求对应执行方法
     * @return 是否成功
     * @throws Exception Exception
     */
    @Override
    public boolean preHandle(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Object handler) throws Exception {
        Map<String, String[]> params = request.getParameterMap();
        String token = "";
        if (params.get("token") != null && params.get("token").length > 0) {
            token = params.get("token")[0];
        }
        if (StringUtils.isNotBlank(token)) {
            String res = auth.tokenAuthRequest(token);
            if (StringUtils.isNotBlank(res)) {
                boolean flag = auth.vailResponseString(res);
                if (!flag) {
                    writeInfo(response, res);
                }
                return flag;
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("message", "ipm-Token校验服务校验失败");
                jsonObject.put("code", "-10");
                jsonObject.put("success", "false");
                jsonObject.put("time",
                    new SimpleDateFormat("yyyy-MM-dd hh;mm;ss")
                        .format(new Date())
                );
                writeInfo(response, jsonObject.toJSONString());
                return false;
            }
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "请求未携带token信息");
            jsonObject.put("code", "-10");
            jsonObject.put("success", "false");
            jsonObject.put("time",
                new SimpleDateFormat("yyyy-MM-dd hh;mm;ss").format(new Date()));
            writeInfo(response, jsonObject.toJSONString());
            return false;
        }
    }

    /**
     * .
     * 写入响应结果
     *
     * @param response response
     * @param res      结果
     */
    private void writeInfo(final HttpServletResponse response,
                           final String res) {
        try (OutputStream outputStream = response.getOutputStream()) {
            //通过设置响应头控制浏览器以UTF-8的编码显示数据，如果不加这句话，那么浏览器显示的将是乱码
            response.setHeader("content-type",
                "application/json;charset=UTF-8");
            //将字符转换成字节数组，指定以UTF-8编码进行转换
            byte[] dataByteArr = res.getBytes(StandardCharsets.UTF_8);
            //使用OutputStream流向客户端输出字节数组
            outputStream.write(dataByteArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
