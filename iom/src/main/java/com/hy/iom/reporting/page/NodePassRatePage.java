package com.hy.iom.reporting.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * .
 * Created by of liaoxg
 * date: 2018-11-28
 * user: lxg
 * package_name: com.hy.iom.reporting.page
 */
@Setter
@Getter
public class NodePassRatePage {
    private String projectId;
    private String ruleId;
    private String flowId;
    private String statisticalDimension;
    private String statisticalDirection;
    private String nodeName;
    private String date;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
