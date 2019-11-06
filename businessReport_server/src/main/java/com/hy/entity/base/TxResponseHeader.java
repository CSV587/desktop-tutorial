package com.hy.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-12
 * user: lxg
 * package_name: com.hy.reporting.callback.entities
 */
@Setter
@Getter
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "TX_HEADER")
public class TxResponseHeader extends TxHeader {
    /**
     * .
     * 服务接受时间
     */
    @XmlElement(name = "SYS_RECV_TIME")
    private String sysRecvTime;
    /**
     * .
     * 服务响应时间
     */
    @XmlElement(name = "SYS_RESP_TIME")
    private String sysRespTime;
    /**
     * .
     * 服务响应状态
     */
    @XmlElement(name = "SYS_TX_STATUS")
    private String sysTxStatus;
    /**
     * .
     * 服务响应码
     */
    @XmlElement(name = "SYS_RESP_CODE")
    private String sysRespCode;
    /**
     * .
     * 服务响应描述
     */
    @XmlElement(name = "SYS_RESP_DESC")
    private String sysRespDesc;
}
