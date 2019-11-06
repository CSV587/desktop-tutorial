package com.hy.cpic.reporting.callnumconfig.insuredIntention.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.cpic.base.excel.ExcelAnnotation;
import com.hy.cpic.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author wkl
 * @since 2018/12/26.
 */
public class BranchCallNumConfigPage extends BasePage {

    @Setter
    @Getter
    private String branchId;

    @Setter
    @Getter
    @ExcelAnnotation(id = 1, name = "分公司名称")
    private String branchName;

    @Setter
    @Getter
    @ExcelAnnotation(id = 2, name = "呼叫量")
    private int callNum;

    @Setter
    @Getter
    @ExcelAnnotation(id = 3, name = "最大量")
    private int callMaxNum;

    @Setter
    @Getter
    @ExcelAnnotation(id = 4, name = "剩余量")
    private int surplusNum;

    @Setter
    @Getter
    @ExcelAnnotation(id = 5, name = "所属业务")
    private String callNumConfigName;

    @Setter
    @Getter
    private String callNumConfigId;

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
    private String activityName;
}
