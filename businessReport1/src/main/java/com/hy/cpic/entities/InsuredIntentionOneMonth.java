package com.hy.cpic.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author wkl
 * @since 2019/7/3.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NameStyle(Style.normal)
@Table(name = "T_CPIC_INTENTION_MONTH")
public class InsuredIntentionOneMonth {
    @Id
    private String id;

    private String callNumber;

    private Date fst;

    private Integer times;

    private String isConnect;

    private String callResult;

    private String intentionLevel;
}
