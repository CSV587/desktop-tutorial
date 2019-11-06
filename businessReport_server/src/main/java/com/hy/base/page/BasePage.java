package com.hy.base.page;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * .
 *
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 16:34 2018/8/24
 * @ Description ：页面基础类 携带分页参数
 * @ Modified By ：
 * @ Version     ：1.0
 */
public class BasePage implements Serializable {

    /**
     * .
     * id
     */
    @Getter
    @Setter
    private String id;
    /**
     * .
     * 当前页
     */
    @Getter
    @Setter
    private int current;
    /**
     * .
     * 每页大小
     */
    @Getter
    @Setter
    private int pageSize;
}
