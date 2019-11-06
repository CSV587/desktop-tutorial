package com.hy.cpic.reporting.extractData.insuredIntention.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.cpic.base.excel.ExcelAnnotation;
import com.hy.cpic.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class InsuredIntentionDataPage extends BasePage {

    @ExcelAnnotation(id = 1, name = "分公司")
    private String company;
    @ExcelAnnotation(id = 2, name = "营销活动")
    private String activityName;
//    @ExcelAnnotation(id = 3, name = "接收名单时间")
//    private String acceptDate;
//    @ExcelAnnotation(id = 4, name = "接收名单量")
//    private String acceptCount;
    @ExcelAnnotation(id = 5, name = "已拨打名单量")
    private String calledCount;
    @ExcelAnnotation(id = 7, name = "剩余未拨名单量")
    private String unCalledCount;
    @ExcelAnnotation(id = 8, name = "意向反馈名单量-高意向")
    private String highLevelCount;
    @ExcelAnnotation(id = 9, name = "意向反馈名单量-中意向")
    private String middleLevelCount;
    @ExcelAnnotation(id = 10, name = "意向反馈名单量-低意向")
    private String lowLevelCount;
    @ExcelAnnotation(id = 11, name = "意向反馈名单量-黑名单")
    private String blackCount;
    @ExcelAnnotation(id = 12, name = "意向反馈名单量-接通且无匹配等级")
    private String noMatchCount;
    @ExcelAnnotation(id = 13, name = "意向反馈名单量-接通且秒挂")
    private String hangupCount;
    @ExcelAnnotation(id = 14, name = "意向反馈名单量-说话且无识别结果")
    private String noRecognitionCount;
    @ExcelAnnotation(id = 15, name = "意向反馈名单量-未接通")
    private String unconnectedCount;
    @ExcelAnnotation(id = 16, name = "高级、中级、低级名单总量")
    private String intentionCount;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;
}
