package com.hy.iom.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Calendar;
import java.util.Date;

public class CallErrorStatistics {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date recordStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date recordEndTime;

    private String recordTime;

    private String projectName;

    private String projectId;

    private String ruleId;

    private String taskId;

    private String type;

    private Long value;

    private String name;


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRecordStartTime() {
        return recordStartTime;
    }

    public void setRecordStartTime(Date recordStartTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(recordStartTime);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        this.recordStartTime = calendar.getTime();
    }

    public Date getRecordEndTime() {
        return recordEndTime;
    }

    public void setRecordEndTime(Date recordEndTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(recordEndTime);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        this.recordEndTime = calendar.getTime();
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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
