package com.hy.iom.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CallDetails {

//    private Flows flows;

    private RecordInfo recordInfo;

    private CallInfo callInfo;

//    private CustomerInfo customerInfo;

//    private ResInfo resInfo;

    private List<CallContent> content;


    public RecordInfo getRecordInfo() {
        return recordInfo;
    }

    public void setRecordInfo(RecordInfo recordInfo) {
        this.recordInfo = recordInfo;
    }

    public CallInfo getCallInfo() {
        return callInfo;
    }

    public void setCallInfo(CallInfo callInfo) {
        this.callInfo = callInfo;
    }

    public List<CallContent> getContent() {
        return content;
    }

    public void setContent(List<CallContent> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CallDetails{" +
                ", recordInfo=" + recordInfo +
                ", content=" + content +
                '}';
    }
}
