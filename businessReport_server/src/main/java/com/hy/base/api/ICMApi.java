package com.hy.base.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.util.HttpRequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * .
 * 获取呼叫策略数据接口
 */
@Component
public class ICMApi {

    /**
     * .
     * 规则Map
     */
    private static Map<String, String> ruleMap = new ConcurrentHashMap<>();
    /**
     * .
     * 任务Map
     */
    private static Map<String, String> taskMap = new ConcurrentHashMap<>();

    /**
     * .
     * 流程Map
     */
    private static Map<String, String> flowMap = new ConcurrentHashMap<>();

    /**
     * .
     * 获取项目Id对应规则列表字符串 包含规则Id和规则名称
     *
     * @param projectId 项目id
     * @return 规则列表字符串
     */
    JSONArray getRuleList(final String projectId) {
        String url = BasicConfig.getIcmUrl()
            + "connector/get_rule_list?projectId="
            + projectId;
        String res = "";
        try {
            res = HttpRequestUtils.httpGet(url, null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONArray resultArray = (JSONArray) jsonObject.get("value");
            syncRuleList(resultArray);
            return resultArray;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    /**
     * .
     * 规则列表
     *
     * @param projectId 项目Id
     * @return 规则列表
     */
    public JSONArray ruleList(final String projectId) {
        String url = BasicConfig.getIcmUrl()
            + "connector/get_rule_list?projectId="
            + projectId;
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

    /**
     * .
     * 同步规则列表
     *
     * @param resultArray 结果数组
     */
    private void syncRuleList(final JSONArray resultArray) {
        for (Object o : resultArray) {
            JSONObject jsonObject = (JSONObject) o;
            String ruleId = jsonObject.getString("ruleId");
            String ruleName = jsonObject.getString("ruleName");
            if (StringUtils.isNotBlank(ruleId)
                && StringUtils.isNotBlank(ruleName)) {
                ruleMap.put(ruleId, ruleName);
            }
        }
    }

    /**
     * .
     * 获取规则名称
     *
     * @param ruleId 规则Id
     * @return 规则名称
     */
    public String getRuleName(final String ruleId) {
        if (ruleMap != null
            && ruleMap.size() > 0
            && StringUtils.isNotBlank(ruleId)) {
            String ruleName = ruleMap.get(ruleId);
            if (StringUtils.isNotBlank(ruleName)) {
                return ruleName;
            }
        }
        return "";
    }

    /**
     * .
     * 获取任务字符
     *
     * @param ruleId 规则Id
     * @return 任务字符串
     */
    JSONArray getTaskList(final String ruleId) {
        String url = BasicConfig.getIcmUrl()
            + "connector/get_task_list?ruleId="
            + ruleId;
        String res = "";
        try {
            res = HttpRequestUtils.httpGet(url, null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONArray resultArray = (JSONArray) jsonObject.get("value");
            syncTaskList(resultArray);
            return resultArray;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    /**
     * .
     * 同步规则列表
     *
     * @param resultArray 查询结果
     */
    private void syncTaskList(final JSONArray resultArray) {
        for (Object o : resultArray) {
            JSONObject jsonObject = (JSONObject) o;
            String taskId = jsonObject.getString("taskId");
            String taskName = jsonObject.getString("taskName");
            if (StringUtils.isNotBlank(taskId)
                && StringUtils.isNotBlank(taskName)) {
                taskMap.put(taskId, taskName);
            }
        }
    }

    /**
     * .
     * 获取任务名称
     *
     * @param ruleId 任务Id
     * @return 任务名称
     */
    public String getTaskName(final String ruleId) {
        if (taskMap != null
            && taskMap.size() > 0
            && StringUtils.isNotBlank(ruleId)) {
            String taskName = taskMap.get(ruleId);
            if (StringUtils.isNotBlank(taskName)) {
                return taskName;
            }
        }
        return "";
    }

    /**
     * .
     * 获取流程字符
     *
     * @param projectId 项目Id
     * @return 流程字符串
     */
    JSONArray getFlowList(final String projectId) {
        String url = BasicConfig.getIcmUrl()
            + "connector/get_flow_list?projectId="
            + projectId;
        String res = "";
        try {
            res = HttpRequestUtils.httpGet(url, null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONArray resultArray = (JSONArray) jsonObject.get("value");
            syncFlowList(resultArray);
            return resultArray;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    /**
     * .
     * 同步流程列表
     *
     * @param resultArray 查询结果
     */
    private void syncFlowList(final JSONArray resultArray) {
        for (Object o : resultArray) {
            JSONObject jsonObject = (JSONObject) o;
            String flowId = jsonObject.getString("flowId");
            String flowName = jsonObject.getString("flowName");
            if (StringUtils.isNotBlank(flowId)
                && StringUtils.isNotBlank(flowName)) {
                flowMap.put(flowId, flowName);
            }
        }
    }

    /**
     * .
     * 获取流程名称
     *
     * @param flowId    流程Id
     * @param projectId 流程Id
     * @return 流程名称
     */
    public String getFlowName(final String flowId,
                              final String projectId) {
        if (StringUtils.isNotBlank(flowId)) {
            if (flowMap.size() == 0
                || StringUtils.isBlank(flowMap.get(flowId))) {
                getFlowList(projectId);
                if (StringUtils.isNotBlank(flowMap.get(flowId))) {
                    return flowMap.get(flowId);
                } else {
                    return "";
                }
            } else {
                return flowMap.get(flowId);
            }
        } else {
            return "";
        }
    }
}
