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
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 18:09 2018/8/31
 * @ Description ：CPIC统计表
 * @ Modified By ：
 * @ Version     ：1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@NameStyle(Style.normal)
@Table(name = "T_CPIC_RECORDINFO")
public class CallInfoStatistics {

    @Id
    @Setter
    @Getter
    private String id;
    @Setter
    @Getter
    private String uuid;
    @Setter
    @Getter
    private Date recordEndTime;
    @Setter
    @Getter
    private Date recordStartTime;
    @Setter
    @Getter
    private Date channelStartTime;
    @Setter
    @Getter
    private Date channelEndTime;
    @Setter
    @Getter
    private String callNumber;
    @Setter
    @Getter
    private String isConnect;
    @Setter
    @Getter
    private String column1 = "";
    @Setter
    @Getter
    private String column2 = "";
    @Setter
    @Getter
    private String column3 = "";
    @Setter
    @Getter
    private String column4 = "";
    @Setter
    @Getter
    private String column5 = "";
    @Setter
    @Getter
    private String column6 = "";
    @Setter
    @Getter
    private String column7 = "";
    @Setter
    @Getter
    private String column8 = "";
    @Setter
    @Getter
    private String column9 = "";
    @Setter
    @Getter
    private String column10 = "";
    @Setter
    @Getter
    private String column11 = "";
    @Setter
    @Getter
    private String column12 = "";
    @Setter
    @Getter
    private String column13 = "";
    @Setter
    @Getter
    private String column14 = "";
    @Setter
    @Getter
    private String column15 = "";
    @Setter
    @Getter
    private String column16 = "";
    @Setter
    @Getter
    private String column17 = "";
    @Setter
    @Getter
    private String column18 = "";
    @Setter
    @Getter
    private String column19 = "";
    @Setter
    @Getter
    private String column20 = "";
    @Setter
    @Getter
    private String column21 = "";
    @Setter
    @Getter
    private String column22 = "";
    @Setter
    @Getter
    private String column23 = "";
    @Setter
    @Getter
    private String column24 = "";
    @Setter
    @Getter
    private String column25 = "";
    @Setter
    @Getter
    private String column26 = "";
    @Setter
    @Getter
    private String column27 = "";
    @Setter
    @Getter
    private String column28 = "";
    @Setter
    @Getter
    private String column29 = "";
    @Setter
    @Getter
    private String column30 = "";
    @Setter
    @Getter
    private String column31 = "";
    @Setter
    @Getter
    private String column32 = "";
    @Setter
    @Getter
    private String column33 = "";
    @Setter
    @Getter
    private String column34 = "";
    @Setter
    @Getter
    private String column35 = "";
    @Setter
    @Getter
    private String column36 = "";
    @Setter
    @Getter
    private String column37 = "";
    @Setter
    @Getter
    private String column38 = "";
    @Setter
    @Getter
    private String column39 = "";
    @Setter
    @Getter
    private String column40 = "";
    @Setter
    @Getter
    private String column41 = "";
    @Setter
    @Getter
    private String column42 = "";
    @Setter
    @Getter
    private String column43 = "";
    @Setter
    @Getter
    private String column44 = "";
    @Setter
    @Getter
    private String column45 = "";
    @Setter
    @Getter
    private String column46 = "";
    @Setter
    @Getter
    private String column47 = "";
    @Setter
    @Getter
    private String column48 = "";
    @Setter
    @Getter
    private String column49 = "";
    @Setter
    @Getter
    private String column50 = "";
    @Setter
    @Getter
    private String proType;
}
