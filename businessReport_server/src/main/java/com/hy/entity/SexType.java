package com.hy.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.hy.base.excel.ExcelEnumAnnotation;

import java.util.HashMap;
import java.util.Map;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-18
 * user: lxg
 * package_name: com.hy.entity
 */
public enum SexType {
    /**
     * .
     * 通过状态
     */
    @ExcelEnumAnnotation(name = "先生")
    Man("1"),

    /**
     * .
     * 不通过状态
     */
    @ExcelEnumAnnotation(name = "女士")
    Woman("2"),

    /**
     * .
     * 不通过状态
     */
    @ExcelEnumAnnotation(name = "")
    Unknown("3");

    /**
     * .
     * 状态对应值
     */
    private String name;

    /**
     * .
     * 获取状态值
     *
     * @return 状态值
     */
    @JsonValue
    public String getName() {
        return name;
    }

    /**
     * .
     * 构造函数
     *
     * @param val 状态值
     */
    SexType(final String val) {
        this.name = val;
    }


    /**
     * .
     * 状态对象
     */
    private static Map<String, SexType> stateMap = new HashMap<>();

    static {
        for (SexType state : values()) {
            stateMap.put(state.getName(), state);
        }
    }

    /**
     * .
     * 根据值获取对象枚举类
     *
     * @param value 值
     * @return 枚举对象
     */
    public static SexType getValue(final String value) {
        return stateMap.get(value);
    }
}
