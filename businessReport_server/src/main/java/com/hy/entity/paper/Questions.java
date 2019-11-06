package com.hy.entity.paper;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-17
 * user: lxg
 * package_name: com.hy.entity.paper
 */
@Setter
@Getter
@XStreamAlias("questions")
public class Questions {
    /**
     * .
     * 问题序号
     */
    private String seqno;
    /**
     * .
     * 问题内容
     */
    private String questionContent;
    /**
     * .
     * 应对话术
     */
    private String handleTerm;
    /**
     * .
     * 操作提示
     */
    private String operateTips;
    /**
     * .
     * 答案列表
     */
    private List<Answers> answersList;
}
