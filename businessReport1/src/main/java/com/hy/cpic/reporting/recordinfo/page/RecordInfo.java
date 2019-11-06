package com.hy.cpic.reporting.recordinfo.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Id;
import javax.persistence.Table;

@JsonIgnoreProperties(ignoreUnknown = true)
@NameStyle(Style.normal)
@Table(name = "T_IOM_RECORDINFO")
public class RecordInfo {

    @Id
    private String id;
    private String recordEndTime;
    private String channelEndTime;
    private String channelStartTime;
    private String recordStartTime;
    @JsonProperty("callnumber")
    private String callNumber;
    private String uuid;
    private String recordPath;
    private String onState;
    private String projectId;
    private String flowId;
    private String ruleId;
    private Integer callCount = 0;
    private Integer duration = 0;
    private Integer turnCount = 0;
    private String endNodeName;
    private String callResult;
    private String finalNodeName;//当呼叫结果不存在时  取endNodeName
    private String tradeType;
    private String callState;//通话状态 speechOverTime matchError error connectSuccess
    private String tradeState;//交易状态
    private String taskId;

    public RecordInfo() {

    }

    public RecordInfo(String uuid) {
        this.uuid = uuid;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecordEndTime() {
        return recordEndTime;
    }

    public void setRecordEndTime(String recordEndTime) {
        this.recordEndTime = recordEndTime;
    }

    public String getChannelEndTime() {
        return channelEndTime;
    }

    public void setChannelEndTime(String channelEndTime) {
        this.channelEndTime = channelEndTime;
    }

    public String getChannelStartTime() {
        return channelStartTime;
    }

    public void setChannelStartTime(String channelStartTime) {
        this.channelStartTime = channelStartTime;
    }

    public String getRecordStartTime() {
        return recordStartTime;
    }

    public void setRecordStartTime(String recordStartTime) {
        this.recordStartTime = recordStartTime;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRecordPath() {
        return recordPath;
    }

    public void setRecordPath(String recordPath) {
        this.recordPath = recordPath;
    }

    public String getOnState() {
        return onState;
    }

    public void setOnState(String onState) {
        this.onState = onState;
    }

    public Integer getCallCount() {
        return callCount;
    }

    public void setCallCount(Integer callCount) {
        this.callCount = callCount;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getTurnCount() {
        return turnCount;
    }

    public void setTurnCount(Integer turnCount) {
        this.turnCount = turnCount;
    }

    @Override
    public String toString() {
        return "RecordInfo{" +
            "id=" + id +
            ", recordEndTime='" + recordEndTime + '\'' +
            ", channelEndTime='" + channelEndTime + '\'' +
            ", channelStartTime='" + channelStartTime + '\'' +
            ", recordStartTime='" + recordStartTime + '\'' +
            ", callNumber='" + callNumber + '\'' +
            ", uuid='" + uuid + '\'' +
            ", recordPath='" + recordPath + '\'' +
            ", onState='" + onState + '\'' +
            ", projectId='" + projectId + '\'' +
            ", flowId='" + flowId + '\'' +
            ", ruleId ='" + ruleId + '\'' +
            ", callCount=" + callCount +
            ", duration=" + duration +
            ", turnCount=" + turnCount +
            ", endNodeName='" + getEndNodeName() + '\'' +
            ", callResult='" + callResult + '\'' +
            '}';
    }

    public String getEndNodeName() {
        return endNodeName;
    }

    public void setEndNodeName(String endNodeName) {
        this.endNodeName = endNodeName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getCallResult() {
        return this.callResult;
    }

    public void setCallResult(String callResult) {
        this.callResult = callResult;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getCallState() {
        return callState;
    }

    public void setCallState(String callState) {
        this.callState = callState;
    }

    public String getFinalNodeName() {
        return finalNodeName;
    }

    public void setFinalNodeName(String finalNodeName) {
        this.finalNodeName = finalNodeName;
    }

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
