package com.hy.cpic.reporting.intentionrate.page;

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
public class IntentionRatePage extends BasePage {
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
    @ExcelAnnotation(id = 3, name = "呼叫总量")
    private String callTotal;
    @Setter
    @Getter
    @ExcelAnnotation(id = 4, name = "呼叫成功量")
    private String successNum;
    @Setter
    @Getter
    @ExcelAnnotation(id = 5, name = "成功量占比")
    private String successRate;
    @Setter
    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date startTime;
    @Setter
    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date endTime;
    @Setter
    @Getter
    @ExcelAnnotation(id = 6, name = "呼叫失败量")
    private String failNum;
    @Setter
    @Getter
    @ExcelAnnotation(id = 7, name = "失败量占比")
    private String failRate;
    @Setter
    @Getter
    private String projectId;
}
