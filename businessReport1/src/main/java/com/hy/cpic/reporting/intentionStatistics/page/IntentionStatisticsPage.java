package com.hy.cpic.reporting.intentionStatistics.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.cpic.base.excel.ExcelAnnotation;
import com.hy.cpic.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * .
 * Created by of liaoxg
 * date: 2018-12-14
 * user: lxg
 * package_name: com.hy.cpic.reporting.intentionStatistics.page
 */
public class IntentionStatisticsPage extends BasePage {
    @Setter
    @Getter
    @ExcelAnnotation(id = 1, name = "分公司")
    private String company;
    @Setter
    @Getter
    @ExcelAnnotation(id = 2, name = "名单输入量")
    private String sumCount;
    @Setter
    @Getter
    @ExcelAnnotation(id = 3, name = "接通名单量")
    private String sumCon;
    @Setter
    @Getter
    @ExcelAnnotation(id = 4, name = "接通率")
    private String conRate;
    @Setter
    @Getter
    @ExcelAnnotation(id = 5, name = "名单输出")
    private String validCount;
    @Setter
    @Getter
    @ExcelAnnotation(id = 6, name = "输出率")
    private String validRate;
    @Setter
    @Getter
    @ExcelAnnotation(id = 7, name = "未接通")
    private String sumUnCon;
    @Setter
    @Getter
    @ExcelAnnotation(id = 8, name = "接通秒挂")
    private String hangupCount;
    @Setter
    @Getter
    @ExcelAnnotation(id = 9, name = "高级")
    private String highLevelCount;
    @Setter
    @Getter
    @ExcelAnnotation(id = 10, name = "中级")
    private String middleLevelCount;
    @Setter
    @Getter
    @ExcelAnnotation(id = 11, name = "低级")
    private String lowLevelCount;
    @Setter
    @Getter
    @ExcelAnnotation(id = 12, name = "黑名单")
    private String blackCount;
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
    private String projectId;

    @Setter
    @Getter
    private String taskName;
}
