package com.hy.base.page;

import lombok.Getter;
import lombok.Setter;

/**
 * .
 * 表头对象
 */
public class TableHead {
    /**
     * .
     * 唯一key值
     */
    @Setter
    @Getter
    private String key;
    /**
     * .
     * 数据对应标记
     */
    @Setter
    @Getter
    private String dataIndex;
    /**
     * .
     * 显示名称
     */
    @Setter
    @Getter
    private String title;

    /**
     * .
     * 构造函数
     *
     * @param propName 数据下标
     * @param name     显示名称
     */
    public TableHead(final String propName,
                     final String name) {
        this.key = propName;
        this.dataIndex = propName;
        this.title = name;
    }


    /**
     * .
     * 转文本
     *
     * @return 文本
     */
    @Override
    public String toString() {
        return "TableHead{"
            + "key='"
            + key
            + '\''
            + ", dataIndex='" + dataIndex
            + '\''
            + ", title='" + title
            + '\''
            + '}';
    }
}
