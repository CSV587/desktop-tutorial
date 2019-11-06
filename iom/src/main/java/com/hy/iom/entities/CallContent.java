package com.hy.iom.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;
import javax.persistence.Table;

@JsonIgnoreProperties(ignoreUnknown = true)
@NameStyle(Style.normal)
@Table(name = "T_IOM_CALLCONTENT")
public class CallContent {
    private String uuid;
    private String  nodeName;
    private String  type;
    private int  seq;
    private String  content;
    private int intervalSeconds;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(int intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    @Override
    public String toString() {
        return "CallContent{" +
                "nodeName='" + nodeName + '\'' +
                ", type='" + type + '\'' +
                ", seq='" + seq + '\'' +
                ", content='" + content + '\'' +
                ", intervalSeconds='" + intervalSeconds + '\'' +
                '}';
    }
}
