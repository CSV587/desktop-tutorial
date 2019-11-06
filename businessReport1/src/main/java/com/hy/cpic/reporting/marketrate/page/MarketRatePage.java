package com.hy.cpic.reporting.marketrate.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.cpic.base.excel.ExcelAnnotation;
import com.hy.cpic.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2018/9/8
 * user: lxg
 * package_name: com.hy.cpic.reporting.callbackrate.page
 */
public class MarketRatePage extends BasePage {
    @Setter
    @Getter
    @ExcelAnnotation(id = 1, name = "呼叫号码")
    private String callNumber;
    @Setter
    @Getter
    @ExcelAnnotation(id = 2, name = "规则名称")
    private String ruleName;
    @Setter
    @Getter
    @ExcelAnnotation(id = 3, name = "最后呼叫时间")
    private String lastCallTime;
    @Setter
    @Getter
    @ExcelAnnotation(id = 4, name = "呼叫次数")
    private String callCount;
    @Setter
    @Getter
    @ExcelAnnotation(id = 5, name = "接通情况")
    private String connectState;
    @Setter
    @Getter
    @ExcelAnnotation(id = 6, name = "接触情况")
    private String contactState;
    @Setter
    @Getter
    @ExcelAnnotation(id = 7, name = "活动意向")
    private String activityDirection;
    @Setter
    @Getter
    @ExcelAnnotation(id = 8, name = "电话意向")
    private String phoneIntention;
    @Setter
    @Getter
    private String hangupNode;
    @Setter
    @Getter
    @ExcelAnnotation(id = 9, name = "客户反馈")
    private String callResult;
    @Setter
    @Getter
    @ExcelAnnotation(id = 11, name = "主流程挂机节点")
    private String masterHangupNode;
    @Setter
    @Getter
    @ExcelAnnotation(id = 12, name = "地址")
    private String site;
    @Setter
    @Getter
    @ExcelAnnotation(id = 13, name = "是否是归属地")
    private String belongTo;
    @Setter
    @Getter
    @ExcelAnnotation(id = 10, name = "匹配失败挂机")
    private String finishMatchError;
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
    private List<String> projectList;
}
