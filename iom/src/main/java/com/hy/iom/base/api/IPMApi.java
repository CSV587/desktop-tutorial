package com.hy.iom.base.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.iom.base.config.BasicConfig;
import com.hy.util.HttpRequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IPMApi {

    private static Map<String, String> projectMap = new ConcurrentHashMap<>();

    public String queryUserProjectList(String token) {
        String url = BasicConfig.getIPMURL() + "project/queryUserProject?token=" + token;
        String res = null;
        try {
            res = HttpRequestUtils.httpGet(url, null, 5000);
        } catch (URISyntaxException e) {
            return res;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONObject value = (JSONObject) jsonObject.get("value");
            JSONArray resultArray = (JSONArray) value.get("result");
            jsonObject.put("value", resultArray);
            ansyProjectList(resultArray);
            res = jsonObject.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<String> getUserProjectIdList(String token) {
        queryUserProjectList(token);
        return new ArrayList<>(projectMap.keySet());
    }

    private void ansyProjectList(JSONArray resultArray) {
        for (Object o : resultArray) {
            JSONObject jsonObject = (JSONObject) o;
            String projectName = jsonObject.getString("projectName");
            String projectId = jsonObject.getString("projectId");
            if (StringUtils.isNotBlank(projectName) && StringUtils.isNotBlank(projectId)) {
                projectMap.put(projectId, projectName);
            }
        }
    }

    public String getProjectName(String projectId) {
        if (projectMap != null && projectMap.size() > 0 && StringUtils.isNotBlank(projectId)) {
            String projectName = projectMap.get(projectId);
            return StringUtils.isNotBlank(projectName) ? projectName : "";
        }
        return "";
    }

    public String getUserId(String token) {
        String url = BasicConfig.getIPMURL() + "/user/queryUserIdByToken?token=" + token;
        String res = null;
        try {
            res = HttpRequestUtils.httpGet(url, null, 5000);
        } catch (URISyntaxException e) {
            return res;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONObject value = jsonObject.getJSONObject("value");
            JSONObject result = value.getJSONObject("result");
            res = result.getString("userId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

}
