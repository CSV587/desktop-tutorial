package com.hy.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.hy.base.excel.ExcelEnumAnnotation;

import java.util.HashMap;
import java.util.Map;

/**
 * .
 * Created by of liaoxg
 * date: 2019/10/12
 * user: lxg
 * package_name: com.hy.entity
 */
public enum IdType {
    /**
     * .
     * 0	身份证
     * 1	护照
     * 2	军官证
     * 4	出生证明
     * 5	户口簿
     * 6	港澳台通行证
     * 8	其他
     * 9	数据转换证件
     * 3	台胞证
     * 通过状态
     */
    @ExcelEnumAnnotation(name = "身份证")
    ZERO("0"),

    /**
     * .
     * 护照
     */
    @ExcelEnumAnnotation(name = "护照")
    ONE("1"),

    /**
     * .
     * 军官证
     */
    @ExcelEnumAnnotation(name = "军官证")
    TWO("2"),

    /**
     * .
     * 台胞证
     */
    @ExcelEnumAnnotation(name = "台胞证")
    THREE("3"),

    /**
     * .
     * 出生证明
     */
    @ExcelEnumAnnotation(name = "出生证明")
    FOUR("4"),

    /**
     * .
     * 户口簿
     */
    @ExcelEnumAnnotation(name = "户口簿")
    FIVE("5"),

    /**
     * .
     * 港澳通行证
     */
    @ExcelEnumAnnotation(name = "港澳台通行证")
    SIX("6"),

    /**
     * .
     * 其他
     */
    @ExcelEnumAnnotation(name = "其他")
    EIGHT("8"),

    /**
     * .
     * 数据转换证件
     */
    @ExcelEnumAnnotation(name = "数据转换证件")
    NINE("9"),
    ;

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
    IdType(final String val) {
        this.name = val;
    }


    /**
     * .
     * 状态对象
     */
    private static Map<String, IdType> stateMap = new HashMap<>();

    static {
        for (IdType state : values()) {
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
    public static IdType getValue(final String value) {
        return stateMap.get(value);
    }
}
