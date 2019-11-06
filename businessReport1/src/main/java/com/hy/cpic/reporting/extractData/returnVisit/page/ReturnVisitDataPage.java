package com.hy.cpic.reporting.extractData.returnVisit.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.cpic.base.excel.ExcelAnnotation;
import com.hy.cpic.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReturnVisitDataPage extends BasePage {

    @ExcelAnnotation(id = 1, name = "呼叫日期")
    private String callDate;
    @ExcelAnnotation(id = 2, name = "职场")
    private String center;
    @ExcelAnnotation(id = 3, name = "分公司")
    private String company;
    @ExcelAnnotation(id = 4, name = "任务总量")
    private String taskNum;
    @ExcelAnnotation(id = 5, name = "已回访量")
    private String returnNum;
    @ExcelAnnotation(id = 6, name = "覆盖率")
    private String coverRate;
    @ExcelAnnotation(id = 7, name = "未回访量")
    private String unReturnNum;
    @ExcelAnnotation(id = 8, name = "黑名单量")
    private String blackListNum;
    @ExcelAnnotation(id = 9, name = "白名单量")
    private String whiteListNum;
    @ExcelAnnotation(id = 10, name = "除重名单量")
    private String repeatNum;
    @ExcelAnnotation(id = 11, name = "转人工量")
    private String conversionNum;
    @ExcelAnnotation(id = 12, name = "接通量")
    private String connectedNum;
    @ExcelAnnotation(id = 13, name = "接通率")
    private String connectedRate;
    @ExcelAnnotation(id = 14, name = "通话时长")
    private String recordingDuration;
    @ExcelAnnotation(id = 15, name = "未接通量")
    private String unconnectedNum;
    @ExcelAnnotation(id = 16, name = "问卷1回访量")
    private String answerOneNum;
    @ExcelAnnotation(id = 17, name = "问卷1占比")
    private String answerOneRate;
    @ExcelAnnotation(id = 18, name = "问卷2回访量")
    private String answerTwoNum;
    @ExcelAnnotation(id = 19, name = "问卷2占比")
    private String answerTwoRate;
    @ExcelAnnotation(id = 20, name = "问卷3回访量")
    private String answerThreeNum;
    @ExcelAnnotation(id = 21, name = "问卷3占比")
    private String answerThreeRate;
    @ExcelAnnotation(id = 22, name = "问卷4回访量")
    private String answerFourNum;
    @ExcelAnnotation(id = 23, name = "问卷4占比")
    private String answerFourRate;
    @ExcelAnnotation(id = 24, name = "问卷5回访量")
    private String answerFiveNum;
    @ExcelAnnotation(id = 25, name = "问卷5占比")
    private String answerFiveRate;
    @ExcelAnnotation(id = 26, name = "结束量")
    private String overNum;
    @ExcelAnnotation(id = 27, name = "全流程占比")
    private String overRate;
    @ExcelAnnotation(id = 28, name = "非真实号码量")
    private String virtualNumberNum;
    @ExcelAnnotation(id = 29, name = "非真实号码占比")
    private String virtualNumberRate;
    @ExcelAnnotation(id = 30, name = "线上引流量")
    private String onlineNumber;
    @ExcelAnnotation(id = 31, name = "线上引流率")
    private String onlineRate;


    private String dimension;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;
}
