package com.hy.reporting.callback.page;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/8/7.
 * @Description :
 */
public class ChildQuestion {

    private String childId;

    private String questionId;

    private String questionName;

    private String childName;

    private List<LinkedHashMap<String,Object>> condition;

    private String subjectivity;

    private int identifier;

    private int seq = 1;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public List<LinkedHashMap<String, Object>> getCondition() {
        return condition;
    }

    public void setCondition(List<LinkedHashMap<String, Object>> condition) {
        this.condition = condition;
    }

    public String getSubjectivity() {
        return subjectivity;
    }

    public void setSubjectivity(String subjectivity) {
        this.subjectivity = subjectivity;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

}
