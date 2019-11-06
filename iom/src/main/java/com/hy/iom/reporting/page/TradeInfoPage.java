package com.hy.iom.reporting.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hy.iom.base.excel.ExcelAnnotation;
import com.hy.iom.entities.RecordInfo;

public class TradeInfoPage extends RecordInfo {

    private long durationStart;

    private long durationEnd;

    @JsonProperty("callnumber")
    private String callNumber;

    @ExcelAnnotation(id = 2, name = "规则名称")
    private String ruleName;

    private String recordStartTime;

    private String recordEndTime;

    private Integer duration = 0;

    private Integer callCount = 0;

    private String onState;

    private String endNodeName;

    private String callResult;

    private String callNodeName;

    private String type;
    @ExcelAnnotation(id = 1, name = "项目名称")
    private String projectName;
    @ExcelAnnotation(id = 6, name = "成功量")
    private long successCount;
    @ExcelAnnotation(id = 7, name = "失败量")
    private long failCount;
    @ExcelAnnotation(id = 4, name = "接通量")
    private long connectCount;
    @ExcelAnnotation(id = 5, name = "未接通量")
    private long unconnectCount;
    @ExcelAnnotation(id = 8, name = "成功率")
    private String successRate;
    @ExcelAnnotation(id = 9, name = "失败率")
    private String failRate;
    @ExcelAnnotation(id = 3, name = "时间")
    private String callDate;

    private String hiddenDate;

    private String dynamicQuery;


    public TradeInfoPage() {
    }

    public TradeInfoPage(String uuid) {
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

    public String getHiddenDate() {
        return hiddenDate;
    }

    public void setHiddenDate(String hiddenDate) {
        this.hiddenDate = hiddenDate;
    }
}
