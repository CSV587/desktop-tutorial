package com.hy.reporting.questionnaire.page;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/8/7.
 * @Description :
 */
@Getter
@Setter
public class ChildQuestion {

    /**
     * .
     * 子问题id
     */
    private String childId;

    /**
     * .
     * 对应父问题id
     */
    private String parentId;

    /**
     * .
     * 对应父问题id
     */
    private String questionId;

    /**
     * .
     * 对应父问题名称
     * 此字段根据identifier字段确定，数据库中不含该字段
     */
    private String questionName;

    /**
     * .
     * 子问题名称
     */
    private String childName;

    /**
     * .
     * 条件list
     */
    private List<LinkedHashMap<String,Object>> condition;

    /**
     * .
     * 主话术
     */
    private String subjectivity;

    /**
     * .
     * 标识编号
     */
    private int identifier;

    /**
     * .
     * 序号
     */
    private int seq;

}
