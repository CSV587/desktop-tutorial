package com.hy.base.auth;

import com.alibaba.fastjson.JSONObject;
import com.hy.base.api.BasicConfig;
import com.hy.util.HttpRequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

/**
 * .
 * 校验
 */
@Component
public class Auth {
    /**
     * .
     * 校验token
     *
     * @param token user token
     * @return 结果
     * @throws URISyntaxException URISyntaxException
     */
    public String tokenAuthRequest(final String token)
        throws URISyntaxException {
        String url = BasicConfig.getIpmUrl()
            + "login/timeOut?" + "token="
            + token;
        return HttpRequestUtils.httpGet(url, null);
    }

    /**
     * .
     * 校验响应报文
     *
     * @param result 响应文本
     * @return 校验结果
     */
    public boolean vailResponseString(final String result) {
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
