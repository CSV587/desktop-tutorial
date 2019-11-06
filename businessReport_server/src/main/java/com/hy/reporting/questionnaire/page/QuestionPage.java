package com.hy.reporting.questionnaire.page;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/8/7.
 * @Description :
 */
@Getter
@Setter
public class QuestionPage {

    /**
     * .
     * id
     */
    private String id;

    /**
     * .
     * 问题id
     */
    private String questionId;

    /**
     * .
     * 创建人
     */
    private String loid;

    /**
     * .
     * 问题名称
     */
    private String questionName;

    /**
     * .
     * 创建时间
     */
    private String createDate;

    /**
     * .
     * 问题标识编号
     */
    private int identifier;

    /**
     * .
     * 对应问卷id
     */
    private String paperId;

    /**
     * .
     * 对应包含子问题
     */
    List<ChildQuestion2> data;

}
