package com.hy.iom.entities;

import com.hy.iom.base.excel.ExcelAnnotation;

public class CallContentMatchResult {

    @ExcelAnnotation(id=1,name="序号")
    private int seq;//序号
    @ExcelAnnotation(id=2,name="当前节点")
    private String currentNode;//当前节点
    @ExcelAnnotation(id=3,name="机器人话术")
    private String aiContent;//机器人话术
    @ExcelAnnotation(id=4,name="客户话术")
    private String customerContent;//客户话术
    @ExcelAnnotation(id=5,name="匹配节点")
    private String matchNode;//匹配节点

    public CallContentMatchResult() {
    }

    public CallContentMatchResult(String currentNode, String aiContent) {
        this.currentNode = currentNode;
        this.aiContent = aiContent;
    }

    public CallContentMatchResult merge(CallContent preNode, CallContent currentNode) {
        this.currentNode = preNode.getNodeName();
        this.aiContent = preNode.getContent();
        this.customerContent = currentNode.getContent();
        return this;
    }

    public String getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(String currentNode) {
        this.currentNode = currentNode;
    }

    public String getAiContent() {
        return aiContent;
    }

    public void setAiContent(String aiContent) {
        this.aiContent = aiContent;
    }

    public String getCustomerContent() {
        return customerContent;
    }

    public void setCustomerContent(String customerContent) {
        this.customerContent = customerContent;
    }

    public String getMatchNode() {
        return matchNode;
    }

    public void setMatchNode(String matchNode) {
        this.matchNode = matchNode;
    }

    @Override
    public String toString() {
        return "CallContentMatchResult{" +
                "currentNode='" + currentNode + '\'' +
                ", aiContent='" + aiContent + '\'' +
                ", customerContent='" + customerContent + '\'' +
                ", matchNode='" + matchNode + '\'' +
                '}';
    }


    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
