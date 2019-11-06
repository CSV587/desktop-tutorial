package com.hy.cpic.base.page;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 16:34 2018/8/24
 * @ Description ：页面基础类 携带分页参数
 * @ Modified By ：
 * @ Version     ：1.0
 */
@Setter
@Getter
public class BasePage implements Serializable {

    private static final long serialVersionUID = 1L;
    //id
    private String id;

    private int current;

    private int pageSize;
}
