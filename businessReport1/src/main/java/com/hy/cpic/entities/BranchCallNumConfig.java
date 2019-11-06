package com.hy.cpic.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wkl
 * @since 2018/12/26.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@NameStyle(Style.normal)
@Table(name = "T_CPIC_BRANCHCALLNUMCONFIG")
public class BranchCallNumConfig {

    @Id
    @Setter
    @Getter
    private String branchId;

    @Setter
    @Getter
    private String callNumConfigId;

    @Setter
    @Getter
    private String branchName;

    @Setter
    @Getter
    private Integer callNum;

    @Setter
    @Getter
    private Integer callMaxNum;

    @Setter
    @Getter
    private Date fst;

    @Setter
    @Getter
    private Date lmt;

    @Setter
    @Getter
    private String foid;

    @Setter
    @Getter
    private String loid;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private ValidState validState = ValidState.valid;

    public enum ValidState {
        valid(0),
        unValid(1),

        UNKNOWN(-1);

        private int value;

        public int getValue() {
            return value;
        }

        private static Map<Integer, ValidState> values = new HashMap<>();

        static {
            for (ValidState type : values()) {
                values.put(type.value, type);
            }
        }

        ValidState(int value) {
            this.value = value;
        }

        public static ValidState getValue(int value) {
            ValidState msgType = values.get(value);
            if (msgType == null) {
                return UNKNOWN;
            }
            return values.get(value);
        }
    }
}
