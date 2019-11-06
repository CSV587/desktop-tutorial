package com.hy.entity.visithistory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * .
 * 单个回访历史节点
 * Created by of liaoxg
 * date: 2019-07-17
 * user: lxg
 * package_name: com.hy.entity.visithistory
 */
@Setter
@Getter
@XStreamAlias("visitHistory")
public class VisitHistory {

    /**
     * .
     * 唯一标识
     */
    @XStreamOmitField
    private String id;

    /**
     * .
     * 保单号
     */
    private String contNo;
    /**
     * .
     * 是否转人工
     * 1-是  0-否
     */
    private String isTransToSeater = "0";
    /**
     * .
     * 处理时间
     * YYYYMMDDHHMISS
     */
    private String handleTm;
    /**
     * .
     * 一级回访类型
     */
    private String calloutType1;
    /**
     * .
     * 二级回访类型
     */
    private String calloutType2;
    /**
     * .
     * 回访方式
     * 4-IVR回访方式 6-智能回访
     */
    private String calloutWay = "4";
    /**
     * .
     * 呼出结果列表
     */
    private List<CallOutResult> callOutResultList;
    /**
     * .
     * 结案状态
     * 1(继续回访(转人工或继续回访时))
     * 9(成功结案(正常结案时))
     */
    private String closeStatus;
    /**
     * .
     * 投保人姓名
     */
    private String appCustName;
    /**
     * .
     * 处理人工号
     */
    private String handlerNo = "ZNWH001";
    /**
     * .
     * 处理人姓名
     */
    private String handlerNm = "ZNWH001";
    /**
     * .
     * 备注
     * 空
     */
    private String remark;
    /**
     * .
     * 修改原因
     * 空
     */
    private String modifyReason;
    /**
     * .
     * 预约方式
     * 空
     */
    private String appointWay;
    /**
     * .
     * 预约时间
     * 空
     */
    private String appointTime;
    /**
     * .
     * 预约原因
     * 空
     */
    private String appointReason;
    /**
     * .
     * 外拨列表
     */
    private List<CallOutInfo> callOutInfoList;
    /**
     * .
     * 关联工单编号
     * 空
     */
    private String workFLowID;
    /**
     * .
     * 关联问卷流水号
     */
    private String questionnaireID;
    /**
     * .
     * 任务ID
     */
    private String callTaskId;
    /**
     * .
     * 问卷勾选统计
     * （是否全部选“是”） 0-否；1-是
     */
    private String qstnaireAnswerSum = "0";
    /**
     * .
     * 问卷是否完成
     * （是否全部勾选）     0-否；1-是
     */
    private String qstnaireFinishFlg = "0";

    /**
     * .
     * 拨打次数
     */
    @XStreamOmitField
    private String callCount;
}
