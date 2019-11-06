package com.hy.cpic.reporting.callbackrate.page;

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
public class CallBackRatePage extends BasePage {
    @Setter
    @Getter
    @ExcelAnnotation(id = 1, name = "回访任务总量")
    private String callBackTaskNum;
    @Setter
    @Getter
    @ExcelAnnotation(id = 2, name = "语音回访成功量")
    private String callBackTaskSuccessNum;
    @Setter
    @Getter
    @ExcelAnnotation(id = 3, name = "成功占比")
    private String successRate;
    @Setter
    @Getter
    @ExcelAnnotation(id = 4, name = "语音回访失败量")
    private String callBackTaskFailNum;
    @Setter
    @Getter
    @ExcelAnnotation(id = 5, name = "失败占比")
    private String failRate;
    @Setter
    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;
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
    @ExcelAnnotation(id = 6, name = "转人工量")
    private String transFerNum;
    @Setter
    @Getter
    @ExcelAnnotation(id = 7, name = "转人工占比")
    private String transFerRate;
    @Setter
    @Getter
    private String projectId;
}
