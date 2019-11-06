package com.hy.reporting.reconciliation.page;

import com.hy.base.excel.ExcelAnnotation;
import com.hy.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-26
 * user: lxg
 * package_name: com.hy.reporting.reconciliation.page
 */
@Getter
@Setter
public class ReconciliationPage extends BasePage {
    /**
     * .
     * 任务Id
     */
    @ExcelAnnotation(id = 1, name = "任务Id")
    private String taskId;
    /**
     * .
     * 总数
     */
    @ExcelAnnotation(id = 2, name = "总数")
    private String sum;
    /**
     * .
     * 已完成
     */
    @ExcelAnnotation(id = 3, name = "已完成")
    private String finished;
    /**
     * .
     * 未开始
     */
    @ExcelAnnotation(id = 4, name = "未开始")
    private String noStart;
    /**
     * .
     * 正在回访
     */
    @ExcelAnnotation(id = 5, name = "正在回访")
    private String notEnd;
    /**
     * .
     * 回访失败
     */
    @ExcelAnnotation(id = 6, name = "回访失败")
    private String retFailure;
    /**
     * .
     * 回访成功
     */
    @ExcelAnnotation(id = 7, name = "回访成功")
    private String retSuccess;
    /**
     * .
     * 状态
     */
    @ExcelAnnotation(id = 8, name = "状态")
    private String status;
    /**
     * .
     * 项目Id
     */
    private String projectId;
    /**
     * .
     * 规则Id
     */
    private String ruleId;

    /**
     * .
     * 导入时间
     */
    private String searchTime;
}
