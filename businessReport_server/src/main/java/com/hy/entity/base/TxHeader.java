package com.hy.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-11
 * user: lxg
 * package_name: com.hy.reporting.callback.entities
 */
@Setter
@Getter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({TxRequestHeader.class, TxResponseHeader.class})
public class TxHeader {
    /**
     * .
     * 报文头长度
     */
    @XmlElement(name = "SYS_HDR_LEN")
    private String sysHdrLen;
    /**
     * .
     * 报文格式版本号
     */
    @XmlElement(name = "SYS_PKG_VRSN")
    private String sysPkgVrsn = "1.0";
    /**
     * .
     * 报文总长度
     */
    @XmlElement(name = "SYS_TTL_LEN")
    private String sysTtlLen;
    /**
     * .
     * 发起方安全节点编号
     */
    @XmlElement(name = "SYS_REQ_SEC_ID")
    private String sysReqSecId;
    /**
     * .
     * 发送方标识信息
     */
    @XmlElement(name = "SYS_SND_SEC_ID")
    private String sysSndSecId;
    /**
     * .
     * 服务种类
     */
    @XmlElement(name = "SYS_TX_TYPE")
    private String sysTxType;
    /**
     * .
     * 全局事件跟踪号
     */
    @XmlElement(name = "SYS_EVT_TRACE_ID")
    private String sysEvtTraceId;
    /**
     * .
     * 子交易序号
     */
    @XmlElement(name = "SYS_SND_SERIAL_NO")
    private String sysSndSerialNo;
    /**
     * .
     * 报文体格式类型
     */
    @XmlElement(name = "SYS_PKG_TYPE")
    private String sysPkgType;
    /**
     * .
     * 报文体长度
     */
    @XmlElement(name = "SYS_MSG_LEN")
    private String sysMsgLen;
    /**
     * .
     * 报文体是否加密
     */
    @XmlElement(name = "SYS_IS_ENCRYPTED")
    private String sysIsEncrypted = "1";
    /**
     * .
     * 报文体加密方式
     */
    @XmlElement(name = "SYS_ENCRYPT_TYPE")
    private String sysEncryptType;
    /**
     * .
     * 报文体压缩方式
     */
    @XmlElement(name = "SYS_COMPRESS_TYPE")
    private String sysCompressType;
    /**
     * .
     * 捎带报文长度
     */
    @XmlElement(name = "SYS_EMB_MSG_LEN")
    private String sysEmbMsgLen;
    /**
     * .
     * 报文状态类型
     */
    @XmlElement(name = "SYS_PKG_STS_TYPE")
    private String sysPkgStsType;
}
