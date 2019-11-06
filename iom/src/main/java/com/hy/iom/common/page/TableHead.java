package com.hy.iom.common.page;

public class TableHead {
    private String key;
    private String dataIndex;
    private String title;

    public TableHead() {
    }

    public TableHead(String propName, String title) {
        this.key = propName;
        this.dataIndex = propName;
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return "TableHead{" +
                "key='" + key + '\'' +
                ", dataIndex='" + dataIndex + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
