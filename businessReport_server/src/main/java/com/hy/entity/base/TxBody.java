package com.hy.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class TxBody {

    /**
     * .
     * 构造函数
     */
    public TxBody() {

    }

    /**
     * .
     * 构造函数
     *
     * @param data 加密数据
     */
    public TxBody(final String data) {
        setMsg(data);
    }

    /**
     * .
     * Msg
     */
    private String msg;
}
