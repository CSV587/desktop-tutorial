package com.hy.iom.entities;

import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Table;


@NameStyle(Style.normal)
@Table(name = "T_IOM_INFOREFLEX")
public class InfoReflex {
    private String projectId;
    private String name;
    private String state;

    public InfoReflex() {
    }

    @Override
    public String toString() {
        return "CustomerInfo{" +
                "projectId='" + getProjectId() + '\'' +
                ", name='" + getName() + '\'' +
                ", state='" + getState() + '\'' +
                '}';
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
