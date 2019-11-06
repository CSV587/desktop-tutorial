package com.hy.reporting.questionnaire.page;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/8/7.
 * @Description :
 */
@Getter
@Setter
public class ChildQuestion3 {

    private String childId;

    private String parentId;

    private String questionId;

    private String questionName;

    private String childName;

    private List<Map<String,String>> condition;

    private String subjectivity;

    private int identifier;

    private int seq;

    private String name;
}
