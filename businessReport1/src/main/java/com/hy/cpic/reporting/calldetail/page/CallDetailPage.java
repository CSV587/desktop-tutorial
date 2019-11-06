package com.hy.cpic.reporting.calldetail.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.cpic.base.excel.ExcelAnnotation;
import com.hy.cpic.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 16:37 2018/8/24
 * @ Description ：外呼明细报表页面文件
 * @ Modified By ：
 * @ Version     ：1.0
 */
public class CallDetailPage extends BasePage {

    //表格日期(格式化)
    @ExcelAnnotation(id = 1, name = "日期")
    @Getter
    private String dateStr;
    //分公司
    @ExcelAnnotation(id = 2, name = "分公司")
    @Setter
    @Getter
    private String company;
    //职场
    @ExcelAnnotation(id = 3, name = "职场")
    @Setter
    @Getter
    private String center;
    //片区
    @ExcelAnnotation(id = 4, name = "片区")
    @Setter
    @Getter
    private String area;
    //团队
    @ExcelAnnotation(id = 5, name = "团队")
    @Setter
    @Getter
    private String team;
    //开始日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Setter
    @Getter
    private Date startTime;
    //结束日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Setter
    @Getter
    private Date endTime;
    //表格日期
    @Setter
    @Getter
    private Date date;

    //发起外呼任务总量
    @ExcelAnnotation(id = 6, name = "发起外呼任务总量")
    @Setter
    @Getter
    private long dialTaskCount;
    //外呼成功量
    @ExcelAnnotation(id = 7, name = "外呼成功量")
    @Setter
    @Getter
    private long dialSuccessCount;
    //外呼失败量
    @ExcelAnnotation(id = 8, name = "外呼失败量")
    @Setter
    @Getter
    private long dialFailureCount;
    //外呼异常量
    @ExcelAnnotation(id = 9, name = "外呼异常量")
    @Setter
    @Getter
    private long dialExceptionCount;

    @Setter
    @Getter
    private String projectId;

    public void setDateStr(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.dateStr = simpleDateFormat.format(date);
    }
}
