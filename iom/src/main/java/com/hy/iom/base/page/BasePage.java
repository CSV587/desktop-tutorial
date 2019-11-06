package com.hy.iom.base.page;

import lombok.Getter;
import lombok.Setter;

/**
 * .
 * Created by of liaoxg
 * date: 2019-03-20
 * user: lxg
 * package_name: com.hy.iom.base.page
 */
@Setter
@Getter
public class BasePage {
    /**
     * .
     * 当前页
     */
    private int current;

    /**
     * .
     * 当页大小
     */
    private int pageSize;
}
