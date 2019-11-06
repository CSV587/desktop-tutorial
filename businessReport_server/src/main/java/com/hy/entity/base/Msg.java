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
public class Msg {
    /**
     * .
     * 构造函数
     */
    public Msg() {

    }

    /**
     * .
     * 构造函数
     *
     * @param data 加密数据集
     */
    public Msg(final String data) {
        setMsgEntity(data);
    }

    /**
     * .
     * msgEntity
     */
    private String msgEntity;
}
