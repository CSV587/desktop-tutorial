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
 * @since 2019/3/14.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NameStyle(Style.normal)
@Table(name = "T_CPIC_RETURNVISITDATA")
public class ReturnVisitData {
    @Id
    private String id;

    private String center;

    private String company;

    private String listId;

    private String callNumber;

    private String agentId;

    private String carNumber;

    private String isBlackList;

    private String isWhiteList;

    private String isRepeat;

    private String isReturn;

    private String isConnected;

    private String isTransfer;

    private String endNodeName;

    private String finalNodeName;

    private String resultLevelOne;

    private String resultLevelTwo;

    private String recordingDuration;

    private Date callDate;

    private Date taskGenerateDate;

    private Date fst;

    private String uuid;

}
