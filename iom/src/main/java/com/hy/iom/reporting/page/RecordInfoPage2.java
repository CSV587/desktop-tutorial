package com.hy.iom.reporting.page;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RecordInfoPage2 extends RecordInfoPage {

    private List<String> endNodeNameList;

    private List<String> callResultList;

    private List<String> tradeStateList;

    private List<String> callNodeNameList;

    private Map<String,Object> dynamicQueryMap;

    public RecordInfoPage2(RecordInfoPage re) {
        this.setProjectId(re.getProjectId());
        this.setTaskId(re.getTaskId());
        this.setRecordStartTime(re.getRecordStartTime());
        this.setRecordEndTime(re.getRecordEndTime());
        this.setOnState(re.getOnState());
        this.setBlackListFlag(re.getBlackListFlag());
        this.setCallCount(re.getCallCount());
        this.setDurationStart(re.getDurationStart());
        this.setDurationEnd(re.getDurationEnd());
        this.setRuleId(re.getRuleId());
        this.setRuleName(re.getRuleName());
        this.setFinalNodeName(re.getFinalNodeName());
        this.setTradeState(re.getTradeState());
        this.setTradeType(re.getTradeType());
        this.setDynamicQuery(re.getDynamicQuery());
        this.setSuccessCount(re.getSuccessCount());
        this.setFailCount(re.getFailCount());
        this.setFailRate(re.getFailRate());
        this.setTagName(re.getTagName());
        this.setTagId(re.getTagId());
        this.setCallDate(re.getCallDate());
        this.setConnectCount(re.getConnectCount());
        this.setUnconnectCount(re.getUnconnectCount());

        if(StringUtils.isNotBlank(re.getCallNumber())){
            this.setCallNumber(re.getCallNumber());
        }

        if(StringUtils.isNotBlank(re.getEndNodeName())) {
            this.setEndNodeName(re.getEndNodeName());
            this.endNodeNameList = Arrays.asList(re.getEndNodeName().split(","));
        }

        if(StringUtils.isNotBlank(re.getCallResult())) {
            this.setCallResult(re.getCallResult());
            this.callResultList = Arrays.asList(re.getCallResult().split(","));
        }

        if(StringUtils.isNotBlank(re.getTradeState())) {
            this.tradeStateList = Arrays.asList(re.getTradeState().split(","));
        }

        if(StringUtils.isNotBlank(re.getCallNodeName())) {
            this.setCallNodeName(re.getCallNodeName());
            this.callNodeNameList = Arrays.asList(re.getCallNodeName().split(","));
        }

        if(StringUtils.isNotBlank(re.getDynamicQuery())) {
            this.dynamicQueryMap = JSONObject.parseObject(this.getDynamicQuery()).getInnerMap();
        }
    }

    public List<String> getEndNodeNameList() {
        return endNodeNameList;
    }

    public void setEndNodeNameList(List<String> endNodeNameList) {
        this.endNodeNameList = endNodeNameList;
    }

    public List<String> getCallResultList() {
        return callResultList;
    }

    public void setCallResultList(List<String> callResultList) {
        this.callResultList = callResultList;
    }

    public List<String> getTradeStateList() {
        return tradeStateList;
    }

    public void setTradeStateList(List<String> tradeStateList) {
        this.tradeStateList = tradeStateList;
    }

    public List<String> getCallNodeNameList() {
        return callNodeNameList;
    }

    public void setCallNodeNameList(List<String> callNodeNameList) {
        this.callNodeNameList = callNodeNameList;
    }

    public Map<String, Object> getDynamicQueryMap() {
        return dynamicQueryMap;
    }

    public void setDynamicQueryMap(Map<String, Object> dynamicQueryMap) {
        this.dynamicQueryMap = dynamicQueryMap;
    }
}
