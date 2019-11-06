package com.hy.base.page;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * .
 * page实体
 *
 * @param <T> 泛型对象
 */
public class PageBody<T> {
    /**
     * .
     * 表头
     */
    @Setter
    @Getter
    private List<TableHead> tHeader;
    /**
     * .
     * 值
     */
    @Setter
    @Getter
    private List<T> value;
    /**
     * .
     * 总数
     */
    @Setter
    @Getter
    private Long count;

    /**
     * .
     * 转文本
     *
     * @return 文本
     */
    @Override
    public String toString() {
        return "PageBody{"
            + "tHeader=" + tHeader
            + ", value=" + value
            + ", count=" + count
            + '}';
    }
}
