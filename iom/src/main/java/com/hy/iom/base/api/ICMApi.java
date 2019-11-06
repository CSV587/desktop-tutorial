package com.hy.iom.base.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.iom.base.config.BasicConfig;
import com.hy.util.HttpRequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ICMApi {

    private static Map<String, String> ruleMap = new ConcurrentHashMap<>();
    private static Map<String, String> flowMap = new ConcurrentHashMap<>();
    private static Map<String, String> taskMap = new ConcurrentHashMap<>();

    public String getRuleList(String projectId) {
        String url = BasicConfig.getICMURL() + "connector/get_rule_list?projectId=" + projectId;
        String res = null;
        try {
            res = HttpRequestUtils.httpGet(url, null, 5000);
        } catch (URISyntaxException e) {
            return res;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONArray resultArray = (JSONArray) jsonObject.get("value");
            ansyRuleList(resultArray);
            res = jsonObject.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
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

    public String getRuleName(String ruleId, String projectId) {
        if (StringUtils.isNotBlank(ruleId)) {
            if (ruleMap.size() == 0 || StringUtils.isBlank(ruleMap.get(ruleId))) {
                getRuleList(projectId);
                return StringUtils.isNotBlank(ruleMap.get(ruleId)) ? ruleMap.get(ruleId) : "";
            } else {
                return ruleMap.get(ruleId);
            }
        } else {
            return "";
        }
    }

    public String getFlowList(String projectId) {
        String url = BasicConfig.getICMURL() + "connector/get_flow_list?projectId=" + projectId;
        String res = "";
        try {
            res = HttpRequestUtils.httpGet(url, null, 5000);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONArray resultArray = (JSONArray) jsonObject.get("value");
            ansyFlowList(resultArray);
            res = jsonObject.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private void ansyFlowList(JSONArray resultArray) {
        for (Object o : resultArray) {
            JSONObject jsonObject = (JSONObject) o;
            String flowId = jsonObject.getString("flowId");
            String flowName = jsonObject.getString("flowName");
            if (StringUtils.isNotBlank(flowId) && StringUtils.isNotBlank(flowName)) {
                flowMap.put(flowId, flowName);
            }
        }
    }

    public String getFlowName(String flowId, String projectId) {
        if (StringUtils.isNotBlank(flowId)) {
            if (flowMap.size() == 0 || StringUtils.isBlank(flowMap.get(flowId))) {
                getFlowList(projectId);
                return StringUtils.isNotBlank(flowMap.get(flowId)) ? flowMap.get(flowId) : "";
            } else {
                return flowMap.get(flowId);
            }
        } else {
            return "";
        }
    }


    public String getTaskList(String ruleId) {
        String url = BasicConfig.getICMURL() + "connector/get_task_list?ruleId=" + ruleId;
        String res = "";
        try {
            res = HttpRequestUtils.httpGet(url, null, 5000);
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

    public String getTaskName(String taskId, String ruleId) {
        if (StringUtils.isNotBlank(taskId)) {
            if (flowMap.size() == 0 || StringUtils.isBlank(taskMap.get(taskId))) {
                getTaskList(ruleId);
                return StringUtils.isNotBlank(taskMap.get(taskId)) ? taskMap.get(taskId) : "";
            } else {
                return taskMap.get(taskId);
            }
        } else {
            return "";
        }
    }


}
