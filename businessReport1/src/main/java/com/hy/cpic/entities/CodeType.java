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
 * @since 2019/7/8.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NameStyle(Style.normal)
@Table(name = "T_CPIC_CODE_TYPE")
public class CodeType {

    @Id
    private String id;

    private String type;

    private String code;

    private String content;

    private String remark;

    private Date fst;

    private Date lmt;

    private String foid;

    private String loid;
}
