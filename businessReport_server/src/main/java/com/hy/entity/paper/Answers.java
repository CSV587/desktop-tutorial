package com.hy.entity.paper;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-17
 * user: lxg
 * package_name: com.hy.entity.paper
 */
@Setter
@Getter
@XStreamAlias("answers")
public class Answers {
    /**
     * .
     * 答案序号
     */
    private String answerSeqno;
    /**
     * .
     * 答案内容
     */
    private String answerContent;
    /**
     * .
     * 是否选择
     * 0-否；1-是
     */
    private String isSelected;
}
