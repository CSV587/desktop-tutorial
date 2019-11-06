package com.hy.iom.reporting.page;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yuzhiping
 * @date 2019年1月3日 
 * Description：
 * 接通率查询对象
 */
@Setter
@Getter
public class ConnectRate { 
	private String projectId;				//项目id
    private String ruleId;					//规则id
    private String taskId;					//任务id
    private String statisticalDimension;    //统计维度
    private String startTime; 				//开始时间
    private String endTime;					//结束时间
    private String validStartTime; 			//有效开始时间
    private String validEndTime; 			//有效结束时间
    private String date;				    //明细查询日期
    private String time;				    //明细查询时间
}
