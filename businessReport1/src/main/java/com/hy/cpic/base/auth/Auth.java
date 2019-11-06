package com.hy.cpic.base.auth;

import com.alibaba.fastjson.JSONObject;
import com.hy.util.HttpRequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

@Component
public class Auth {

    @Value("${system.ipm}")
    private String ipmUrl;

    public String tokenAuthRequest(String token) throws URISyntaxException {
        String url = ipmUrl + "login/timeOut?" + "token=" + token;
        return HttpRequestUtils.httpGet(url, null);
    }

    public boolean vailResposeString(String result) {
        if (StringUtils.isNotBlank(result)) {
            try {
                JSONObject res = JSONObject.parseObject(result);
                return res.getBoolean("success");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
