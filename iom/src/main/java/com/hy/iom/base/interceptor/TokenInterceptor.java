package com.hy.iom.base.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.hy.iom.base.auth.Auth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * author : wellhor Zhao
 * date ; 2018-8-12
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private Auth auth;

    // 在目标方法执行前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String[]> params = request.getParameterMap();
        String token = "";
        if (params.get("token") != null && params.get("token").length > 0) {
            token = params.get("token")[0];
        }
        if (StringUtils.isNotBlank(token)) {
            String res = auth.tokenAuthRequest(token);
            if (StringUtils.isNotBlank(res)) {
                boolean flag = auth.vailResposeString(res);
                if (!flag) {
                    writeInfo(response, res);
                }
                return flag;
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("message", "ipm-Token校验服务校验失败");
                jsonObject.put("code", "-10");
                jsonObject.put("success", "false");
                jsonObject.put("time", new SimpleDateFormat("yyyy-MM-dd hh;mm;ss").format(new Date()));
                writeInfo(response, jsonObject.toJSONString());
                return false;
            }
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "请求未携带token信息");
            jsonObject.put("code", "-10");
            jsonObject.put("success", "false");
            jsonObject.put("time", new SimpleDateFormat("yyyy-MM-dd hh;mm;ss").format(new Date()));
            writeInfo(response, jsonObject.toJSONString());
            return false;
        }
    }

    private void writeInfo(HttpServletResponse response, String res) {
        try (OutputStream outputStream = response.getOutputStream()) {
            response.setHeader("content-type", "application/json;charset=UTF-8"); //通过设置响应头控制浏览器以UTF-8的编码显示数据，如果不加这句话，那么浏览器显示的将是乱码
            byte[] dataByteArr = res.getBytes(StandardCharsets.UTF_8); //将字符转换成字节数组，指定以UTF-8编码进行转换
            outputStream.write(dataByteArr); //使用OutputStream流向客户端输出字节数组
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 在目标方法执行后执行，但在请求返回前，我们仍然可以对 ModelAndView进行修改
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    // 在请求已经返回之后执行
    @Override
    public void afterCompletion(
        HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }


}
