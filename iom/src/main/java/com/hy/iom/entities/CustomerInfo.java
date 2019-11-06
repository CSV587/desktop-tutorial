package com.hy.iom.entities;

import com.hy.iom.base.excel.ExcelAnnotation;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Table;


@NameStyle(Style.normal)
@Table(name = "T_IOM_CUSTOMERINFO")
public class CustomerInfo {
    private String uuid;
    @ExcelAnnotation(id=1,name="信息类型",width=10000)
    private String name;
    @ExcelAnnotation(id=2,name="值",width=30000)
    private String value;

    public CustomerInfo() {
    }

    public CustomerInfo(String uuid) {
        this.uuid = uuid;
    }

    public CustomerInfo(String uuid, String name, String value) {
        this.uuid = uuid;
        this.name = name;
        this.value = value;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CustomerInfo{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
