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
 * @author zxd
 * @since 2019/1/3.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NameStyle(Style.normal)
@Table(name = "T_CPIC_INSUREDINTENTION")
public class InsuredIntentionMetaInfo {
    @Id
    private String id;

    private String metaInfo;

    private String branchName;

    private String businessType;

    private String callNumConfigId;

    private Date expireDate;

    private Date fst;

    private String activityName;

    private String callNumber;

    private String callNumber1;

    private String callNumber2;
}
