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
@Table(name = "T_CPIC_RETURNVISIT")
public class ReturnVisitMetaInfo {
    @Id
    private String id;

    private String metaInfo;

    private String center;

    private String branchName;

    private String businessType;

    private String callNumber;

    private String agentId;

    private String listId;

    private String carNumber;

    private String isWhiteList;

    private String isRepeat;

    private Date fst;

    private String callNumConfigId;

}
