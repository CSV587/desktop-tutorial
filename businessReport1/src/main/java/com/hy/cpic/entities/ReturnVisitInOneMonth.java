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
 * @since 2019/3/5.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NameStyle(Style.normal)
@Table(name = "T_CPIC_RETURNVISITINONEMONTH")
public class ReturnVisitInOneMonth {
    @Id
    private String id;

    private String callNumber;

    private Date fst;
}
