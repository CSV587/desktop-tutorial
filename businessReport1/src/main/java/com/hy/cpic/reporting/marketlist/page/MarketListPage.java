package com.hy.cpic.reporting.marketlist.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.cpic.base.excel.ExcelAnnotation;
import com.hy.cpic.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * .
 * Created by of liaoxg
 * date: 2019-03-18
 * user: lxg
 * package_name: com.hy.cpic.reporting.marketlist.page
 */
@Setter
@Getter
public class MarketListPage extends BasePage {

    @ExcelAnnotation(id = 1, name = "所属规则")
    private String ruleName;
    @ExcelAnnotation(id = 2, name = "所属任务")
    private String taskName;
    @ExcelAnnotation(id = 3, name = "客户编号")
    private String callNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelAnnotation(id = 4, name = "开始时间")
    private Timestamp startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelAnnotation(id = 5, name = "结束时间")
    private Timestamp endTime;
    @ExcelAnnotation(id = 6, name = "呼叫次数")
    private String callCount;
    @ExcelAnnotation(id = 7, name = "呼叫结果")
    private String callResult;
    @ExcelAnnotation(id = 8, name = "挂机节点")
    private String nodName;
    @ExcelAnnotation(id = 9, name = "挂机秒数")
    private String hangupSec;
    private String uuid;
    private String projectId;
    private String ruleId;
    private String taskId;
}
