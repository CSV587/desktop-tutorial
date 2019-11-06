package com.hy.iom.base.auth;

import com.alibaba.fastjson.JSONObject;
import com.hy.iom.base.config.BasicConfig;
import com.hy.util.HttpRequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

@Component
public class Auth {

    public String tokenAuthRequest(String token) throws URISyntaxException {
        String url = BasicConfig.getIPMURL() + "login/timeOut?" + "token=" + token;
        return HttpRequestUtils.httpGet(url, null, 5000);
    }

    public boolean vailResposeString(String result) {
        if (StringUtils.isNotBlank(result)) {
            try {
                JSONObject res = JSONObject.parseObject(result);
                return res.getBoolean("success");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
