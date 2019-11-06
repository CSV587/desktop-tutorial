package com.hy.iom.entities;

public class ResInfo {
    private String uuid;
    private String name;
    private String value;

    public ResInfo(String uuid, String name, String value) {
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
        return "ResInfo{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
