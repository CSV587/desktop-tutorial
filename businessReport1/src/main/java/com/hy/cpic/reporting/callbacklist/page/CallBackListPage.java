package com.hy.cpic.reporting.callbacklist.page;

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
public class CallBackListPage extends BasePage {
    @Setter
    @Getter
    @ExcelAnnotation(id = 1, name = "任务生成日期")
    private String dateStr;
    @Setter
    @Getter
    @ExcelAnnotation(id = 2, name = "分公司")
    private String company;
    @Setter
    @Getter
    @ExcelAnnotation(id = 3, name = "职场")
    private String center;
    @Setter
    @Getter
    @ExcelAnnotation(id = 4, name = "片区")
    private String area;
    @Setter
    @Getter
    @ExcelAnnotation(id = 5, name = "团队")
    private String team;
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
    @ExcelAnnotation(id = 6, name = "坐席工号")
    private String agentId;
    @Setter
    @Getter
    @ExcelAnnotation(id = 7, name = "坐席姓名")
    private String agentName;
    @Setter
    @Getter
    @ExcelAnnotation(id = 8, name = "车牌")
    private String carNumber;
    @Setter
    @Getter
    @ExcelAnnotation(id = 9, name = "生效日期")
    private String validDateStr;
    @Setter
    @Getter
    @ExcelAnnotation(id = 10, name = "业务类型")
    private String businessType;
    @Setter
    @Getter
    @ExcelAnnotation(id = 11, name = "回访类型")
    private String callBackType;
    @Setter
    @Getter
    @ExcelAnnotation(id = 12, name = "录音开始时间")
    private String recordStartTime;
    @Setter
    @Getter
    @ExcelAnnotation(id = 13, name = "回访一级结果")
    private String callResult1;
    @Setter
    @Getter
    @ExcelAnnotation(id = 14, name = "回访二级结果")
    private String callResult2;
    @Setter
    @Getter
    @ExcelAnnotation(id = 15, name = "通话时长")
    private long duration;
    @Setter
    @Getter
    @ExcelAnnotation(id = 16, name = "问卷一")
    private String answer1;
    @Setter
    @Getter
    @ExcelAnnotation(id = 17, name = "问卷二")
    private String answer2;
    @Setter
    @Getter
    @ExcelAnnotation(id = 18, name = "问卷三")
    private String answer3;
    @Setter
    @Getter
    @ExcelAnnotation(id = 19, name = "问卷三(不满意原因)")
    private String answer3Cause;
    @Setter
    @Getter
    @ExcelAnnotation(id = 20, name = "问卷四")
    private String answer4;
    @Setter
    @Getter
    @ExcelAnnotation(id = 21, name = "问卷五")
    private String answer5;
    @Setter
    @Getter
    @ExcelAnnotation(id = 22, name = "录音Id")
    private String uuid;
    @Setter
    @Getter
    private String projectId;
    @Setter
    @Getter
    @ExcelAnnotation(id = 23, name = "渠道标识")
    private String channeltype;

}
