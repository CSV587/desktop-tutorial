package com.hy.iom.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import com.hy.iom.base.excel.ExcelEnumAnnotation;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * .
 * Created by of liaoxg
 * date: 2018-12-10
 * user: lxg
 * package_name: com.hy.iom.entities
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@NameStyle(Style.normal)
@Getter
@Setter
@Table(name = "T_IOM_SCENETHROUGHDETAIL")
public class SceneThroughDetail {
    private String uuid;
    private String nodeName;
    private int seq;
    @Enumerated(EnumType.STRING)
    private ThroughState throughFlag;

    /**
     * .
     * 通过状态枚举类
     */
    public enum ThroughState {
        /**
         * .
         * 通过状态
         */
        @ExcelEnumAnnotation(name = "通过")
        through(0),

        /**
         * .
         * 不通过状态
         */
        @ExcelEnumAnnotation(name = "不通过")
        notThrough(1),

        /**
         * .
         * 不说话状态
         */
        @ExcelEnumAnnotation(name = "不说话")
        noSpeak(2),

        /**
         * .
         * 挂机状态
         */
        @ExcelEnumAnnotation(name = "挂机")
        hangup(3);

        /**
         * .
         * 状态对应值
         */
        private int value;

        /**
         * .
         * 获取状态值
         *
         * @return 状态值
         */
        @JsonValue
        public int getValue() {
            return value;
        }

        /**
         * .
         * 构造函数
         *
         * @param val 状态值
         */
        ThroughState(final int val) {
            this.value = val;
        }

        private static Map<Integer, ThroughState> stateMap = new HashMap<>();

        static {
            for (ThroughState state : values()) {
                stateMap.put(state.value, state);
            }
        }

        public static ThroughState getValue(int value) {
            return stateMap.get(value);
        }
    }

    @Override
    public String toString() {
        return "SceneThroughDetail{" +
            "nodeName='" + nodeName + '\'' +
            ", uuid='" + uuid + '\'' +
            ", seq='" + seq + '\'' +
            ", throughFlag='" + throughFlag + '\'' +
            '}';
    }
}
