package com.hy.iom.entities;

import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@NameStyle(Style.normal)
@Table(name = "T_IOM_CALLSTATISTICS")
public class CallStatistics {
    @Id
    @GeneratedValue(generator = "UUID")
    private String id;
    private String year;
    private String month;
    private String week;
    private String day;
    private String hour;
    private String projectId;
    private String ruleId;
    private String taskId;
    private String flowId;
    private String onState;
    private Integer callCount;
    private Integer cnt;

    public CallStatistics() {
    }

    public CallStatistics(String year, String month, String week, String day, String hour, String projectId, String ruleId, String taskId, String flowId, String onState, Integer callCount) {
        this.year = year;
        this.month = month;
        this.week = week;
        this.day = day;
        this.hour = hour;
        this.setProjectId(projectId);
        this.setRuleId(ruleId);
        this.setTaskId(taskId);
        this.setFlowId(flowId);
        this.onState = onState;
        this.callCount = callCount;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
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

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CallStatistics that = (CallStatistics) o;

        return callCount.equals(that.callCount);
    }

    @Override
    public int hashCode() {
        int result = year.hashCode();
        result = 31 * result + month.hashCode();
        result = 31 * result + week.hashCode();
        result = 31 * result + day.hashCode();
        result = 31 * result + hour.hashCode();
        result = 31 * result + (projectId == null ? "" : projectId).hashCode();
        result = 31 * result + (ruleId == null ? "" : ruleId).hashCode();
        result = 31 * result + (taskId == null ? "" : taskId).hashCode();
        result = 31 * result + (flowId == null ? "" : flowId).hashCode();
        result = 31 * result + onState.hashCode();
        result = 31 * result + callCount.hashCode();
        return result;
    }


    @Override
    public String toString() {
        return "CallStatistics{" +
            "id=" + id +
            ", year='" + year + '\'' +
            ", month='" + month + '\'' +
            ", week='" + week + '\'' +
            ", day='" + day + '\'' +
            ", hour='" + hour + '\'' +
            ", projectId='" + (projectId == null ? "" : projectId) + '\'' +
            ", ruleId='" + (ruleId == null ? "" : ruleId) + '\'' +
            ", taskId='" + (taskId == null ? "" : taskId) + '\'' +
            ", flowId='" + (flowId == null ? "" : flowId) + '\'' +
            ", onState='" + onState + '\'' +
            ", callCount=" + callCount +
            ", cnt=" + cnt +
            '}';
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
