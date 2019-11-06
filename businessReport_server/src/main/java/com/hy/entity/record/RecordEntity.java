package com.hy.entity.record;

import lombok.Data;
import org.springframework.core.annotation.Order;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-19
 * user: lxg
 * package_name: com.hy.entity.record
 */
@Data
public class RecordEntity {
    /**
     * .
     * 主键值
     */
    @Order(value = 1)
    private String keyStr;
    /**
     * .
     * 虚中心
     */
    @Order(value = 2)
    private String vCid = "101";
    /**
     * .
     * 呼叫流水号
     */
    @Order(value = 3)
    private String connectionId;
    /**
     * .
     * 话务员工号
     */
    @Order(value = 4)
    private String agentId = "801714";
    /**
     * .
     * 话务员小话机号码
     */
    @Order(value = 5)
    private String agentPhone = "021123123";
    /**
     * .
     * 主叫号码
     */
    @Order(value = 6)
    private String callingNumber = "021123123";
    /**
     * .
     * 被叫号码
     */
    @Order(value = 7)
    private String calledNumber;
    /**
     * .
     * 呼叫日
     * yyyy.mm.dd
     */
    @Order(value = 8)
    private String strCallDay;
    /**
     * .
     */
    @Order(value = 9)
    private String recordType = "1";
    /**
     * .
     * 录音开始时间
     * yyyy.mm.dd hh:MM:ss
     */
    @Order(value = 10)
    private String recordStartTime;
    /**
     * .
     * 录音时长
     */
    @Order(value = 11)
    private String durTime;
    /**
     * .
     * 录音相对路径
     */
    @Order(value = 12)
    private String recordPath;
    /**
     * .
     * 为空
     */
    @Order(value = 13)
    private String skillId;
    /**
     * .
     * 被叫号码
     */
    @Order(value = 14)
    private String oriCalled;
    /**
     * .
     * 被叫号码
     */
    @Order(value = 15)
    private String accessCode;
    /**
     * .
     * 呼叫类型
     */
    @Order(value = 16)
    private String callType = "IVROutboundCall";
    /**
     * .
     * 呼叫结束类型
     */
    @Order(value = 17)
    private String callEndType;
    /**
     * .
     * 通话开始时间
     * yyyy.mm.dd hh:MM:ss
     */
    @Order(value = 18)
    private String ansTime;
    /**
     * .
     * 呼叫结束时间
     * yyyy.mm.dd hh:MM:ss
     */
    @Order(value = 19)
    private String callEndTime;
    /**
     * .
     * nfs设备号
     * 为空
     */
    @Order(value = 20)
    private String nfsDvcId;
    /**
     * .
     * nfs录音初始地址
     * 为空
     */
    @Order(value = 21)
    private String nfsInitPath;
    /**
     * .
     * 入库时间
     * 为空
     */
    @Order(value = 22)
    private String upDateTime;
    /**
     * .
     * 坐席班组号
     * 为空
     */
    @Order(value = 23)
    private String groupId;
    /**
     * .
     * 扩展信息
     * 为空
     */
    @Order(value = 24)
    private String exInfo;
    /**
     * .
     * 1 webCall类型
     */
    @Order(value = 25)
    private String webCallType = "0";
    /**
     * .
     * 用户nfs录音初始地址
     * 为空
     */
    @Order(value = 26)
    private String usrNfsInitPath;
    /**
     * .
     * 用户录音路径
     * 为空
     */
    @Order(value = 27)
    private String usrRecordPath;
}
