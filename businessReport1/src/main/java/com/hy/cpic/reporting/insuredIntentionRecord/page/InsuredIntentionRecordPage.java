package com.hy.cpic.reporting.insuredIntentionRecord.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.cpic.base.excel.ExcelAnnotation;
import com.hy.cpic.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class InsuredIntentionRecordPage extends BasePage {

    @ExcelAnnotation(id = 1, name = "时间范围")
    private String dateRange;

    @ExcelAnnotation(id = 2, name = "总拨打名单量")
    private Integer calledCount;
    @ExcelAnnotation(id = 3, name = "一次拨打接通名单量")
    private Integer connectOnceCount;
    @ExcelAnnotation(id = 4, name = "二次拨打接通名单量")
    private Integer connectTwiceCount;
    @ExcelAnnotation(id = 5, name = "一直未接通名单量")
    private Integer unconnectCount;

    @ExcelAnnotation(id = 6, name = "总通次")
    private Integer calledTimes;
    @ExcelAnnotation(id = 7, name = "接通通次")
    private Integer connectTimes;
    @ExcelAnnotation(id = 8, name = "未接通通次")
    private Integer unconnectTimes;
    @ExcelAnnotation(id = 9, name = "通次接通率")
    private String connectAvgTimes;

    @ExcelAnnotation(id = 10, name = "总时长（秒）")
    private Integer calledLength;
    @ExcelAnnotation(id = 11, name = "接通总时长（振铃+通话）")
    private Integer connectLength;
    @ExcelAnnotation(id = 12, name = "接通振铃时长")
    private Integer connectRingLength;
    @ExcelAnnotation(id = 13, name = "接通通话均时（秒）")
    private Integer connectAvgLength;
    @ExcelAnnotation(id = 14, name = "有效通话时长")
    private Integer connectChatLength;
    @ExcelAnnotation(id = 15, name = "有效接通通均总时长（秒）")
    private Integer connectChatAvgLength;
    @ExcelAnnotation(id = 16, name = "未接通振铃时长")
    private Integer unconnectRingLength;
    @ExcelAnnotation(id = 17, name = "未接通通均振铃时长（秒）")
    private Integer unconnectRingAvgLength;

    private String listType;

    private String company;

    private String expireDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;
}
