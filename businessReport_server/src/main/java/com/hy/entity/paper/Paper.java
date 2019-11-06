package com.hy.entity.paper;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
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
@XStreamAlias("paper")
public class Paper {
    /**
     * .
     * 任务ID
     */
    private String callTaskId;
    /**
     * .
     * 保单号
     */
    private String contNo;
    /**
     * .
     * 问卷编号
     */
    private String paperId;
    /**
     * .
     * 问卷中文名称
     */
    private String paperName;
    /**
     * .
     * 问题列表
     */
    private List<Questions> questionsList;

    /**
     * .
     * 是否回全部选是
     */
    @XStreamOmitField
    private boolean isAllAccess = false;

    /**
     * .
     * 是否全部勾选
     */
    @XStreamOmitField
    private boolean isAllAnswer = false;
}
