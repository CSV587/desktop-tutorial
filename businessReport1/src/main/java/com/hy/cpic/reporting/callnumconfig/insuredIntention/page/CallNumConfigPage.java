package com.hy.cpic.reporting.callnumconfig.insuredIntention.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.cpic.base.excel.ExcelAnnotation;
import com.hy.cpic.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * .
 * Created by of wkl
 * date: 2018/12/24
 */
public class CallNumConfigPage extends BasePage {
    @Setter
    @Getter
    @ExcelAnnotation(id = 1, name = "名称")
    private String name;
    @Setter
    @Getter
    @ExcelAnnotation(id = 2, name = "最大量")
    private int callMaxNum;
    @Setter
    @Getter
    @ExcelAnnotation(id = 3, name = "路径")
    private String path;
    @Setter
    @Getter
    @ExcelAnnotation(id = 4, name = "正则")
    private String regexp;
    @Setter
    @Getter
    private String projectId;
    @Setter
    @Getter
    private String ruleId;
    @Setter
    @Getter
    @ExcelAnnotation(id = 5, name = "所属项目")
    private String projectName;
    @Setter
    @Getter
    @ExcelAnnotation(id = 6, name = "所属规则")
    private String ruleName;
    @Setter
    @Getter
    @ExcelAnnotation(id = 7, name = "业务类型")
    private String businessType;
    @Setter
    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date fst;
    @Setter
    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date lmt;
    @Setter
    @Getter
    private String foid;
    @Setter
    @Getter
    private String loid;
    @Setter
    @Getter
    private String foidName;
    @Setter
    @Getter
    private String loidName;

    @Setter
    @Getter
    private String validState;

    @Setter
    @Getter
    private Long totalNum;

    @Setter
    @Getter
    private int repeatNum;

    @Setter
    @Getter
    private String execIp;

    @Setter
    @Getter
    private Date taskTime;
}
