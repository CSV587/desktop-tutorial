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
public enum PolicyState {
    /**
     * .
     * 通过状态
     */
    @ExcelEnumAnnotation(name = "自动垫缴")
    MatPay("104"),

    /**
     * .
     * 不通过状态
     */
    @ExcelEnumAnnotation(name = "自动缓缴")
    DelayPay("108");

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
    PolicyState(final String val) {
        this.name = val;
    }

    /**
     * .
     * 状态对象
     */
    private static Map<String, PolicyState> stateMap = new HashMap<>();

    static {
        for (PolicyState state : values()) {
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
    public static PolicyState getValue(final String value) {
        return stateMap.get(value);
    }
}
