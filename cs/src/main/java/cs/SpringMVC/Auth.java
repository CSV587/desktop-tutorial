package cs.SpringMVC;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

@Component
public class Auth {

    public String tokenAuthRequest(String token) throws URISyntaxException {
        String url = "127.0.0.1:8080/" + "login/timeOut?" + "token=" + token;
        return "OK!";
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
