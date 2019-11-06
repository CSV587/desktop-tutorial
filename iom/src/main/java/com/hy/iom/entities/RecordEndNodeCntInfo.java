package com.hy.iom.entities;

public class RecordEndNodeCntInfo {
    private String endNodeName;
    private String recordDate;
    private Integer cnt;

    public String getEndNodeName() {
        return endNodeName;
    }

    public void setEndNodeName(String endNodeName) {
        this.endNodeName = endNodeName;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }


    @Override
    public String toString() {
        return "RecordEndNodeCntInfo{" +
                "endNodeName='" + endNodeName + '\'' +
                ", recordDate='" + recordDate + '\'' +
                ", cnt=" + cnt +
                '}';
    }
}
