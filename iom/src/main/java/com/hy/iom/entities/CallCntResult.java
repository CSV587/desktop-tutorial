package com.hy.iom.entities;

import com.hy.iom.base.excel.ExcelAnnotation;

public class CallCntResult {

    private String projectId;
    @ExcelAnnotation(id=1,name="项目名称")
    private String projectName;
    @ExcelAnnotation(id=2,name="呼叫日期")
    private String callDate;
    @ExcelAnnotation(id=3,name="已呼叫量")
    private Integer dialCount=0;
    @ExcelAnnotation(id=4,name="接通量")
    private Integer connectCount=0;
    @ExcelAnnotation(id=5,name="未接通量")
    private Integer unconnectCount=0;

    private Float successRate;

    private Float failRate;
    @ExcelAnnotation(id=6,name="成功率")
    private String successRateStr;
    @ExcelAnnotation(id=7,name="失败率")
    private String failRateStr;



    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public Integer getDialCount() {
        return dialCount;
    }

    public void setDialCount(Integer dialCount) {
        this.dialCount = dialCount;
    }

    public Integer getConnectCount() {
        return connectCount;
    }

    public void setConnectCount(Integer connectCount) {
        this.connectCount = connectCount;
    }

    public Integer getUnconnectCount() {
        return unconnectCount;
    }

    public void setUnconnectCount(Integer unconnectCount) {
        this.unconnectCount = unconnectCount;
    }

    public Float getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Float successRate) {
        this.successRate = successRate;
    }

    public Float getFailRate() {
        return failRate;
    }

    public void setFailRate(Float failRate) {
        this.failRate = failRate;
    }

    @Override
    public String toString() {
        return "CallCntResult{" +
                "projectId='" + projectId + '\'' +
                ", callDate='" + callDate + '\'' +
                ", dialCount=" + dialCount +
                ", connectCount=" + connectCount +
                ", unconnectCount=" + unconnectCount +
                ", successRate=" + successRate +
                ", failRate=" + failRate +
                '}';
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSuccessRateStr() {
        return successRateStr;
    }

    public void setSuccessRateStr(String successRateStr) {
        this.successRateStr = successRateStr;
    }

    public String getFailRateStr() {
        return failRateStr;
    }

    public void setFailRateStr(String failRateStr) {
        this.failRateStr = failRateStr;
    }
}
