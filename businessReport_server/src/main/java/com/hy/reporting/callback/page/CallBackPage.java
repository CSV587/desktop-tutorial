package com.hy.reporting.callback.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.base.excel.ExcelAnnotation;
import com.hy.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-16
 * user: lxg
 * package_name: com.hy.reporting.callback.entities
 */
@Setter
@Getter
public class CallBackPage extends BasePage {
    /**
     * .
     * 客户编号
     */
    @ExcelAnnotation(id = 1, name = "客户编号")
    private String customId;

    /**
     * .
     * 保单号
     */
    @ExcelAnnotation(id = 2, name = "保单号")
    private String polNumber;

    /**
     * .
     * 二级业务类型
     */
    @ExcelAnnotation(id = 3, name = "二级业务类型")
    private String secondSaryType;

    /**
     * .
     * 保单状态
     */
    @ExcelAnnotation(id = 4, name = "保单状态")
    private String polStatus;

    /**
     * .
     * 流程名称
     */
    @ExcelAnnotation(id = 5, name = "流程名称")
    private String flowId;

    /**
     * .
     * 呼叫次数
     */
    @ExcelAnnotation(id = 6, name = "呼叫次数")
    private String callCount;

    /**
     * .
     * 接通状态
     */
    @ExcelAnnotation(id = 7, name = "接通状态")
    private String onState;

    /**
     * .
     * 开始时间
     */
    @ExcelAnnotation(id = 8, name = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp startTime;

    /**
     * .
     * 结束时间
     */
    @ExcelAnnotation(id = 9, name = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp endTime;

    /**
     * .
     * 呼叫结果
     */
    @ExcelAnnotation(id = 10, name = "呼叫结果")
    private String callResult;

    /**
     * .
     * 问题答案
     */
    @ExcelAnnotation(id = 11, name = "问题答案")
    private String queAnswer;

    /**
     * .
     * 结案状态
     */
    @ExcelAnnotation(id = 12, name = "结案状态")
    private String closeStatus;

    /**
     * .
     * 推送标签
     */
    @ExcelAnnotation(id = 13, name = "推送标签")
    private String pushLabel;

    /**
     * .
     * 项目Id
     */
    private String projectId;

    /**
     * .
     * uuid
     */
    private String uuid;
}
