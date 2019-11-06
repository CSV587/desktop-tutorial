package com.hy.reporting.questionnaire.page;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/8/6.
 * @Description :
 */
@Getter
@Setter
public class PaperPage {

    private String id;

    private String sort;

    private int seq;

    private String name;

    private String loid;

    private String createDate;

    private String projectId;

    private String flowId;

    private int current;

    private int pageSize;
}
