package com.hy.base.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.util.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * .
 * 获取权限管理数据接口
 */
@Slf4j
@Component
public class IPMApi {

    /**
     * .
     * 项目Map存储
     */
    private static Map<String, String> projectMap = new ConcurrentHashMap<>();

    /**
     * .
     * 获取user对应项目包含id和项目名称
     *
     * @param token user token
     * @return 对应项目字符串包含id和项目名称
     */
    JSONArray queryUserProjectList(final String token) {
        String url = BasicConfig.getIpmUrl()
            + "project/queryUserProject?token="
            + token;
        String res = "";
        try {
            res = HttpRequestUtils.httpGet(url, null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONObject value = (JSONObject) jsonObject.get("value");
            JSONArray resultArray = (JSONArray) value.get("result");
            jsonObject.put("value", resultArray);
            syncProjectList(resultArray);
            return resultArray;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    /**
     * .
     * 获取项目id列表
     *
     * @param token user token
     * @return 项目id列表
     */
    public List<String> getUserProjectIdList(final String token) {
        queryUserProjectList(token);
        return new ArrayList<>(projectMap.keySet());
    }

    /**
     * .
     * 同步项目列表
     *
     * @param resultArray 项目数组
     */
    private void syncProjectList(final JSONArray resultArray) {
        for (Object o : resultArray) {
            JSONObject jsonObject = (JSONObject) o;
            String projectName = jsonObject.getString("projectName");
            String projectId = jsonObject.getString("projectId");
            if (StringUtils.isNotBlank(projectName)
                && StringUtils.isNotBlank(projectId)) {
                projectMap.put(projectId, projectName);
            }
        }
    }

    /**
     * .
     * 根据项目Id获取对应项目名称
     *
     * @param projectId 项目Id
     * @return 项目名称
     */
    public String getProjectName(final String projectId) {
        if (projectMap != null
            && projectMap.size() > 0
            && StringUtils.isNotBlank(projectId)) {
            String projectName = projectMap.get(projectId);
            if (StringUtils.isNotBlank(projectName)) {
                return projectName;
            }
        }
        return "";
    }

    /**
     * .
     * 获取对应用户id
     *
     * @param token user token
     * @return 用户id
     */
    public String getUserId(final String token) {
        String url = BasicConfig.getIpmUrl()
            + "user/queryUserIdByToken?token="
            + token;
        String res;
        try {
            res = HttpRequestUtils.httpGet(url, null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONObject value = jsonObject.getJSONObject("value");
            JSONObject result = value.getJSONObject("result");
            res = result.getString("userId");
        } catch (Exception e) {
            return null;
        }
        return res;
    }

    /**
     * .
     * 获取对应用户name
     *
     * @param token
     * @return 用户name
     */
    public String getUserName(final String token) {
        String url = BasicConfig.getIpmUrl()
            + "user/queryUserNameByToken?token="
            + token;
        String res;
        try {
            res = HttpRequestUtils.httpGet(url, null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONObject value = jsonObject.getJSONObject("value");
            JSONObject result = value.getJSONObject("result");
            res = result.getString("userName");
        } catch (Exception e) {
            return null;
        }
        return res;
    }
}
