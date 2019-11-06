package com.hy.cpic.base.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.util.HttpRequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ICMApi {

    @Value("${system.icm}")
    private String icmUrl;

    private static Map<String, String> ruleMap = new ConcurrentHashMap<>();
    private static Map<String, String> taskMap = new ConcurrentHashMap<>();

    String getRuleList(String projectId) {
        String url = icmUrl + "connector/get_rule_list?projectId=" + projectId;
        String res = "";
        try {
            res = HttpRequestUtils.httpGet(url, null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONArray resultArray = (JSONArray) jsonObject.get("value");
            ansyRuleList(resultArray);
            res = jsonObject.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public JSONArray ruleList(String projectId) {
        String url = icmUrl + "connector/get_rule_list?projectId=" + projectId;
        JSONArray resultArray = new JSONArray();
        try {
            String res = HttpRequestUtils.httpGet(url, null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            resultArray = (JSONArray) jsonObject.get("value");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultArray;
    }

    private void ansyRuleList(JSONArray resultArray) {
        for (Object o : resultArray) {
            JSONObject jsonObject = (JSONObject) o;
            String ruleId = jsonObject.getString("ruleId");
            String ruleName = jsonObject.getString("ruleName");
            if (StringUtils.isNotBlank(ruleId) && StringUtils.isNotBlank(ruleName)) {
                ruleMap.put(ruleId, ruleName);
            }
        }
    }

    public String getRuleName(String ruleId) {
        if (ruleMap != null && ruleMap.size() > 0 && StringUtils.isNotBlank(ruleId)) {
            String ruleName = ruleMap.get(ruleId);
            return StringUtils.isNotBlank(ruleName) ? ruleName : "";
        }
        return "";
    }

    String getTaskList(String ruleId) {
        String url = icmUrl + "connector/get_task_list?ruleId=" + ruleId;
        String res = "";
        try {
            res = HttpRequestUtils.httpGet(url, null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONArray resultArray = (JSONArray) jsonObject.get("value");
            ansyTaskList(resultArray);
            res = jsonObject.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private void ansyTaskList(JSONArray resultArray) {
        for (Object o : resultArray) {
            JSONObject jsonObject = (JSONObject) o;
            String taskId = jsonObject.getString("taskId");
            String taskName = jsonObject.getString("taskName");
            if (StringUtils.isNotBlank(taskId) && StringUtils.isNotBlank(taskName)) {
                taskMap.put(taskId, taskName);
            }
        }
    }
}
