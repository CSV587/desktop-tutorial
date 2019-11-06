package com.hy.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-11
 * user: lxg
 * package_name: com.hy.reporting.callback.entities
 */
@Setter
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "TX")
@XmlType
public class Tx {
    /**
     * .
     * 报文头
     */
    @XmlElementRef
    private TxHeader txHeader;

    /**
     * .
     * 报文体
     */
    @XmlElement(name = "TX_BODY")
    private TxBody txBody;
}
