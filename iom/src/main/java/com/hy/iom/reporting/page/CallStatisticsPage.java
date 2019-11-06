package com.hy.iom.reporting.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * .
 * Created by of liaoxg
 * date: 2018-11-30
 * user: lxg
 * package_name: com.hy.iom.reporting.page
 */
@Setter
@Getter
public class CallStatisticsPage {
    private String projectId;
    private String ruleId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    private String statisticalDimension;
}
