package com.hy.cpic.base.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.util.HttpRequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IPMApi {

    @Value("${system.ipm}")
    private String ipmUrl;

    private static Map<String, String> projectMap = new ConcurrentHashMap<>();

    String queryUserProjectList(String token) {
        String url = ipmUrl + "project/queryUserProject?token=" + token;
        String res = "";
        try {
            res = HttpRequestUtils.httpGet(url, null);
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

    public String getProjectName(String token, String projectId) {
        String url = ipmUrl + "project/queryProjectNameByProjectId/" + projectId + "?token=" + token;
        String projectName = "";
        try {
            String res = HttpRequestUtils.httpGet(url, null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONObject value = (JSONObject) jsonObject.get("value");
            projectName = value.getString("projectName");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectName;
    }

    public String getUserName(String token, String userId) {
        String url = ipmUrl + "user/queryUserName/" + userId + "?token=" + token;
        String userName = "";
        try {
            String res = HttpRequestUtils.httpGet(url, null);
                JSONObject jsonObject = JSONObject.parseObject(res);
            JSONObject value = (JSONObject) jsonObject.get("value");
            if (value != null) {
                userName = value.getString("userName");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userName;
    }

    public List<String> getUserProjectIdList(String token) {
        queryUserProjectList(token);
        return new ArrayList<>(projectMap.keySet());
    }

    public String getUserIdByToken(String token) {
        String url = ipmUrl + "user/queryUserIdByToken?token=" + token;
        String userId = "";
        try {
            String res = HttpRequestUtils.httpGet(url, null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONObject value = (JSONObject) jsonObject.get("value");
            if (value != null) {
                userId = value.getJSONObject("result").getString("userId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }

}
