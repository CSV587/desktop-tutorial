package com.hy.cpic.base.api;

import com.alibaba.fastjson.JSONObject;
import com.hy.cpic.base.utils.ErrorUtil;
import com.hy.util.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * .
 * Created by of liaoxg
 * date: 2019-01-28
 * user: lxg
 * package_name: com.hy.cpic.base.api
 */
@Slf4j
@Component
public class AddressApi {

    @Value("${system.site}")
    private String siteUrl;

    public String getAddress(String text) {
        String url = siteUrl;
        String res = "";
        JSONObject body = new JSONObject();
        body.put("addressText", text);
        try {
            res = HttpRequestUtils.httpPost(url, body);
            log.debug("addressRes:{}", res);
        } catch (Exception e) {
            log.error(ErrorUtil.getStackTrace(e));
        }
        return res;
    }
}
