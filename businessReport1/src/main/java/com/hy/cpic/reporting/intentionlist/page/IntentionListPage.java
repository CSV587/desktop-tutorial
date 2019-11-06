package com.hy.cpic.reporting.intentionlist.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.cpic.base.excel.ExcelAnnotation;
import com.hy.cpic.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * .
 * Created by of liaoxg
 * date: 2018/9/8
 * user: lxg
 * package_name: com.hy.cpic.reporting.callbackrate.page
 */
public class IntentionListPage extends BasePage {
    @Setter
    @Getter
    @ExcelAnnotation(id = 1, name = "分公司")
    private String company;
    @Setter
    @Getter
    @ExcelAnnotation(id = 2, name = "营销活动名称")
    private String activityName;
    @Setter
    @Getter
    @ExcelAnnotation(id = 3, name = "录音开始时间")
    private String recordStartTime;
    @Setter
    @Getter
    @ExcelAnnotation(id = 4, name = "录音结束时间")
    private String recordEndTime;
    @Setter
    @Getter
    @ExcelAnnotation(id = 5, name = "通话时长")
    private String callDuration;
    @Setter
    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;
    @Setter
    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;
    @Setter
    @Getter
    @ExcelAnnotation(id = 6, name = "客户手机号码")
    private String cusNumber;
    @Setter
    @Getter
    @ExcelAnnotation(id = 7, name = "客户姓名")
    private String cusName;
    @Setter
    @Getter
    @ExcelAnnotation(id = 8, name = "车牌号")
    private String carNumber;
    @Setter
    @Getter
    @ExcelAnnotation(id = 9, name = "意向分类")
    private String intentionClass;
    @Setter
    @Getter
    @ExcelAnnotation(id = 10, name = "是否全流程")
    private String allFlow;
    @Setter
    @Getter
    @ExcelAnnotation(id = 11, name = "呼叫结果")
    private String callResult;
    @Setter
    @Getter
    @ExcelAnnotation(id = 12, name = "交互次数")
    private String interactions;
    @Setter
    @Getter
    private String projectId;
    @Setter
    @Getter
    private String taskName;
}
