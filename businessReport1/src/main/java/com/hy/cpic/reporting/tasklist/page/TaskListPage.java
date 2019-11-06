package com.hy.cpic.reporting.tasklist.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.cpic.base.excel.ExcelAnnotation;
import com.hy.cpic.base.page.BasePage;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 16:37 2018/8/24
 * @ Description ：外呼任务监控页面文件
 * @ Modified By ：
 * @ Version     ：1.0
 */
public class TaskListPage extends BasePage {

    //表格日期(格式化)
    @ExcelAnnotation(id=1,name="日期")
    private String dateStr;
    //分公司
    @ExcelAnnotation(id=2,name="分公司")
    private String company;
    //职场
    @ExcelAnnotation(id=3,name="职场")
    private String center;
    //片区
    @ExcelAnnotation(id=4,name="片区")
    private String area;
    //团队
    @ExcelAnnotation(id=5,name="团队")
    private String team;
    //座席工号
    @ExcelAnnotation(id=6,name="座席工号")
    private String agentId;
    //车牌号
    @ExcelAnnotation(id=7,name="车牌号")
    private String carNumber;
    //开始日期
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date startTime;
    //结束日期
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date endTime;
    //开始日期
    @ExcelAnnotation(id=12,name="外呼开始时间")
    private String recordStartTime;
    //结束日期
    @ExcelAnnotation(id=13,name="外呼结束时间")
    private String recordEndTime;
    //表格日期
    private Date date;
    //保单号
    @ExcelAnnotation(id=8,name="保单号")
    private String policyNo;
    //业务类型
    @ExcelAnnotation(id=9,name="业务类型")
    private String businessType;
    //产品类型
    @ExcelAnnotation(id=10,name="产品类型")
    private String productType;
    //录音ID
    @ExcelAnnotation(id=11,name="录音ID")
    private String recordId;
    //呼叫总时长
    @ExcelAnnotation(id=14,name="呼叫总时长")
    private long duration;
    //外呼次数
    @ExcelAnnotation(id=15,name="外呼次数")
    private int callCount;
    //外呼结果
    private String callResult;
    //一级外呼结果
    @ExcelAnnotation(id=16,name="一级外呼结果")
    private String callResult1;
    //二级外呼结果
    @ExcelAnnotation(id=17,name="二级外呼结果")
    private String callResult2;

    private String projectId;

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }


    public int getCallCount() {
        return callCount;
    }

    public void setCallCount(int callCount) {
        this.callCount = callCount;
    }

    public String getCallResult() {
        return callResult;
    }

    public void setCallResult(String callResult) {
        this.callResult = callResult;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.dateStr = simpleDateFormat.format(date);
    }

    public void setDateStr(String date) {
        this.dateStr = date;
    }

    public String getRecordStartTime() {
        return recordStartTime;
    }

    public void setRecordStartTime(String recordStartTime) {
        this.recordStartTime = StringUtils.isNotBlank(recordStartTime)?recordStartTime.substring(0,recordStartTime.length()-2):recordStartTime;
    }

    public String getRecordEndTime() {
        return recordEndTime;
    }

    public void setRecordEndTime(String recordEndTime) {
        this.recordEndTime = StringUtils.isNotBlank(recordEndTime)?recordEndTime.substring(0,recordEndTime.length()-2):recordEndTime;
    }

    public String getCallResult1() {
        return callResult1;
    }

    public void setCallResult1(String callResult1) {
        this.callResult1 = callResult1;
    }

    public String getCallResult2() {
        return callResult2;
    }

    public void setCallResult2(String callResult2) {
        this.callResult2 = callResult2;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

}
