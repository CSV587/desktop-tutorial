package com.hy.reporting.callcyclemanage.page;

import com.hy.base.excel.ExcelAnnotation;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/4.
 * @Description :
 */

@Getter
@Setter
public class CallCyclePage {

    private String id;

    @ExcelAnnotation(id = 1, name = "推送任务编号")
    private String pushTaskId;

    @ExcelAnnotation(id = 2, name = "回访任务编号")
    private String returnTaskId;

    @ExcelAnnotation(id = 3, name = "任务总量")
    private long taskTotal;

    private int taskState;

    @ExcelAnnotation(id = 4, name = "任务状态")
    private String taskStateName;

    @ExcelAnnotation(id = 5, name = "任务开始时间")
    private String startDate;

    @ExcelAnnotation(id = 6, name = "任务结束时间")
    private String endDate;

    @ExcelAnnotation(id = 7, name = "新开始时间")
    private String newStartDate;

    @ExcelAnnotation(id = 8, name = "新结束时间")
    private String newEndDate;

    @ExcelAnnotation(id = 9, name = "最后修改人")
    private String editor;

    @ExcelAnnotation(id = 10, name = "最后修改时间")
    private String editDate;

    private int callState;

    private int current;

    private int pageSize;

}
