package com.hy.iom.reporting.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hy.iom.base.excel.ExcelAnnotation;
import com.hy.iom.entities.RecordInfo;

public class RecordInfoPage extends RecordInfo {

    private long durationStart;

    private long durationEnd = Long.MAX_VALUE;

    @ExcelAnnotation(id = 1, name = "客户编号")
    @JsonProperty("callnumber")
    private String callNumber;

    @ExcelAnnotation(id = 2, name = "规则名称")
    private String ruleName;

    @ExcelAnnotation(id = 3, name = "开始呼叫时间")
    private String recordStartTime;

    @ExcelAnnotation(id = 4, name = "结束呼叫时间")
    private String recordEndTime;

    @ExcelAnnotation(id = 5, name = "时长")
    private Integer duration = 0;

    @ExcelAnnotation(id = 6, name = "呼叫次数")
    private Integer callCount = 0;

    @ExcelAnnotation(id = 7, name = "接通情况")
    private String onState;

    @ExcelAnnotation(id = 8, name = "流转次数")
    private Integer turnCount;

    @ExcelAnnotation(id = 9, name = "挂机节点")
    private String endNodeName;

    @ExcelAnnotation(id = 10, name = "呼叫结果")
    private String callResult;

    private String callNodeName;

    private String type;

    private String projectName;

    private long successCount;

    private long failCount;

    private long connectCount;

    private long unconnectCount;

    private String successRate;

    private String failRate;

    private String callDate;

    private String dynamicQuery;

    //添加标签结果
    @ExcelAnnotation(id = 11, name = "标签结果")
    private String tagName;

    //添加标签id
    private String tagId;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public RecordInfoPage() {
    }

    public RecordInfoPage(String uuid) {
        super(uuid);
    }

    public long getDurationStart() {
        return durationStart;
    }

    public void setDurationStart(long durationStart) {
        this.durationStart = durationStart;
    }

    public long getDurationEnd() {
        return durationEnd;
    }

    public void setDurationEnd(long durationEnd) {
        this.durationEnd = durationEnd;
    }


    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getCallNodeName() {
        return callNodeName;
    }

    public void setCallNodeName(String callNodeName) {
        this.callNodeName = callNodeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(long successCount) {
        this.successCount = successCount;
    }

    public long getFailCount() {
        return failCount;
    }

    public void setFailCount(long failCount) {
        this.failCount = failCount;
    }

    public String getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(String successRate) {
        this.successRate = successRate;
    }

    public String getFailRate() {
        return failRate;
    }

    public void setFailRate(String failRate) {
        this.failRate = failRate;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public long getConnectCount() {
        return connectCount;
    }

    public void setConnectCount(long connectCount) {
        this.connectCount = connectCount;
    }

    public long getUnconnectCount() {
        return unconnectCount;
    }

    public void setUnconnectCount(long unconnectCount) {
        this.unconnectCount = unconnectCount;
    }

    public String getDynamicQuery() {
        return dynamicQuery;
    }

    public void setDynamicQuery(String dynamicQuery) {
        this.dynamicQuery = dynamicQuery;
    }

    @Override
    public String getCallNumber() {
        return callNumber;
    }

    @Override
    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    @Override
    public String getRecordStartTime() {
        return recordStartTime;
    }

    @Override
    public void setRecordStartTime(String recordStartTime) {
        this.recordStartTime = recordStartTime;
    }

    @Override
    public String getRecordEndTime() {
        return recordEndTime;
    }

    @Override
    public void setRecordEndTime(String recordEndTime) {
        this.recordEndTime = recordEndTime;
    }

    @Override
    public Integer getDuration() {
        return duration;
    }

    @Override
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public Integer getCallCount() {
        return callCount;
    }

    @Override
    public void setCallCount(Integer callCount) {
        this.callCount = callCount;
    }

    @Override
    public String getOnState() {
        return onState;
    }

    @Override
    public void setOnState(String onState) {
        this.onState = onState;
    }

    @Override
    public String getEndNodeName() {
        return endNodeName;
    }

    @Override
    public void setEndNodeName(String endNodeName) {
        this.endNodeName = endNodeName;
    }

    @Override
    public String getCallResult() {
        return callResult;
    }

    @Override
    public void setCallResult(String callResult) {
        this.callResult = callResult;
    }

    @Override
    public Integer getTurnCount() {
        return turnCount;
    }

    @Override
    public void setTurnCount(Integer turnCount) {
        this.turnCount = turnCount;
    }

}
