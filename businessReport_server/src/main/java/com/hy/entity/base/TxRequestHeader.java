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
public class TxRequestHeader extends TxHeader {
    /**
     * .
     * 服务名
     */
    @XmlElement(name = "SYS_TX_CODE")
    private String sysTxCode;
    /**
     * .
     * 服务版本号
     */
    @XmlElement(name = "SYS_TX_VRSN")
    private String sysTxVrsn = "1.0";
    /**
     * .
     * 预留
     */
    @XmlElement(name = "SYS_RESERVED")
    private String sysReserved;
    /**
     * .
     * 交易剩余处理时间
     */
    @XmlElement(name = "SYS_TIME_LEFT")
    private String sysTimeLeft;
    /**
     * .
     * 发起方交易时间
     */
    @XmlElement(name = "SYS_REQ_TIME")
    private String sysReqTime;
}
