package com.hy.cpic.reporting.ccmonitor.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.cpic.base.excel.ExcelAnnotation;
import com.hy.cpic.base.page.BasePage;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 16:37 2018/8/24
 * @ Description ：并发量监控页面文件
 * @ Modified By ：
 * @ Version     ：1.0
 */
public class CCMonitorPage extends BasePage {

    //表格日期(格式化)
    @ExcelAnnotation(id=1,name="日期")
    private String dateStr;
    //分公司
    @ExcelAnnotation(id=2,name="分公司")
    private String company;
    //职场
    @ExcelAnnotation(id=3,name="职场")
    private String center;
    //分区
    @ExcelAnnotation(id=4,name="片区")
    private String area;
    //团队
    @ExcelAnnotation(id=5,name="团队")
    private String team;
    //车牌号
    private String carNumber;
    //开始日期
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date startTime;
    //结束日期
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date endTime;
    //表格日期
    private Date date;
    //时段
    @ExcelAnnotation(id=6,name="时段")
    private String timeSlot;
    //请求量
    @ExcelAnnotation(id=7,name="请求量")
    private long requestCount;
    //实际外呼量
    @ExcelAnnotation(id=8,name="实际外呼量")
    private long realDialCount;
    //超出并发量
    @ExcelAnnotation(id=9,name="超出并发量")
    private long exceedingCount;

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

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(long requestCount) {
        this.requestCount = requestCount;
    }

    public long getRealDialCount() {
        return realDialCount;
    }

    public void setRealDialCount(long realDialCount) {
        this.realDialCount = realDialCount;
    }

    public long getExceedingCount() {
        return exceedingCount;
    }

    public void setExceedingCount(long exceedingCount) {
        this.exceedingCount = exceedingCount;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

}
