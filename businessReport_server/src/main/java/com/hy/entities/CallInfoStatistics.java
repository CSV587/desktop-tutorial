package com.hy.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * .
 *
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 18:09 2018/8/31
 * @ Description ：CPIC统计表
 * @ Modified By ：
 * @ Version     ：1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@NameStyle(Style.normal)
@Table(name = "T_BUSINESS_RECORDINFO")
public class CallInfoStatistics {

    /**
     * .
     * id
     */
    @Id
    @Setter
    @Getter
    private String id;
    /**
     * .
     * uuid
     */
    @Setter
    @Getter
    private String uuid;
    /**
     * .
     * 录音开始时间
     */
    @Setter
    @Getter
    private Timestamp recordEndTime;
    /**
     * .
     * 录音结束时间
     */
    @Setter
    @Getter
    private Timestamp recordStartTime;
    /**
     * .
     * 会话开始时间
     */
    @Setter
    @Getter
    private Timestamp channelStartTime;
    /**
     * .
     * 会话结束时间
     */
    @Setter
    @Getter
    private Timestamp channelEndTime;
    /**
     * .
     * 时长
     */
    @Setter
    @Getter
    private long duration;
    /**
     * .
     * 号码
     */
    @Setter
    @Getter
    private String callNumber;
    /**
     * .
     * 是否接通
     */
    @Setter
    @Getter
    private String isConnect;
    /**
     * .
     * 自定义字段1
     */
    @Setter
    @Getter
    private String column1 = "";
    /**
     * .
     * 自定义字段2
     */
    @Setter
    @Getter
    private String column2 = "";
    /**
     * .
     * 自定义字段3
     */
    @Setter
    @Getter
    private String column3 = "";
    /**
     * .
     * 自定义字段4
     */
    @Setter
    @Getter
    private String column4 = "";
    /**
     * .
     * 自定义字段5
     */
    @Setter
    @Getter
    private String column5 = "";
    /**
     * .
     * 自定义字段6
     */
    @Setter
    @Getter
    private String column6 = "";
    /**
     * .
     * 自定义字段7
     */
    @Setter
    @Getter
    private String column7 = "";
    /**
     * .
     * 自定义字段8
     */
    @Setter
    @Getter
    private String column8 = "";
    /**
     * .
     * 自定义字段9
     */
    @Setter
    @Getter
    private String column9 = "";
    /**
     * .
     * 自定义字段10
     */
    @Setter
    @Getter
    private String column10 = "";
    /**
     * .
     * 自定义字段11
     */
    @Setter
    @Getter
    private String column11 = "";
    /**
     * .
     * 自定义字段12
     */
    @Setter
    @Getter
    private String column12 = "";
    /**
     * .
     * 自定义字段13
     */
    @Setter
    @Getter
    private String column13 = "";
    /**
     * .
     * 自定义字段14
     */
    @Setter
    @Getter
    private String column14 = "";
    /**
     * .
     * 自定义字段15
     */
    @Setter
    @Getter
    private String column15 = "";
    /**
     * .
     * 自定义字段16
     */
    @Setter
    @Getter
    private String column16 = "";
    /**
     * .
     * 自定义字段17
     */
    @Setter
    @Getter
    private String column17 = "";
    /**
     * .
     * 自定义字段18
     */
    @Setter
    @Getter
    private String column18 = "";
    /**
     * .
     * 自定义字段19
     */
    @Setter
    @Getter
    private String column19 = "";
    /**
     * .
     * 自定义字段20
     */
    @Setter
    @Getter
    private String column20 = "";
    /**
     * .
     * 自定义字段21
     */
    @Setter
    @Getter
    private String column21 = "";
    /**
     * .
     * 自定义字段22
     */
    @Setter
    @Getter
    private String column22 = "";
    /**
     * .
     * 自定义字段23
     */
    @Setter
    @Getter
    private String column23 = "";
    /**
     * .
     * 自定义字段24
     */
    @Setter
    @Getter
    private String column24 = "";
    /**
     * .
     * 自定义字段25
     */
    @Setter
    @Getter
    private String column25 = "";
    /**
     * .
     * 自定义字段26
     */
    @Setter
    @Getter
    private String column26 = "";
    /**
     * .
     * 自定义字段27
     */
    @Setter
    @Getter
    private String column27 = "";
    /**
     * .
     * 自定义字段28
     */
    @Setter
    @Getter
    private String column28 = "";
    /**
     * .
     * 自定义字段29
     */
    @Setter
    @Getter
    private String column29 = "";
    /**
     * .
     * 自定义字段30
     */
    @Setter
    @Getter
    private String column30 = "";
    /**
     * .
     * 自定义字段31
     */
    @Setter
    @Getter
    private String column31 = "";
    /**
     * .
     * 自定义字段32
     */
    @Setter
    @Getter
    private String column32 = "";
    /**
     * .
     * 自定义字段33
     */
    @Setter
    @Getter
    private String column33 = "";
    /**
     * .
     * 自定义字段34
     */
    @Setter
    @Getter
    private String column34 = "";
    /**
     * .
     * 自定义字段35
     */
    @Setter
    @Getter
    private String column35 = "";
    /**
     * .
     * 自定义字段36
     */
    @Setter
    @Getter
    private String column36 = "";
    /**
     * .
     * 自定义字段37
     */
    @Setter
    @Getter
    private String column37 = "";
    /**
     * .
     * 自定义字段38
     */
    @Setter
    @Getter
    private String column38 = "";
    /**
     * .
     * 自定义字段39
     */
    @Setter
    @Getter
    private String column39 = "";
    /**
     * .
     * 自定义字段40
     */
    @Setter
    @Getter
    private String column40 = "";
    /**
     * .
     * 自定义字段41
     */
    @Setter
    @Getter
    private String column41 = "";
    /**
     * .
     * 自定义字段42
     */
    @Setter
    @Getter
    private String column42 = "";
    /**
     * .
     * 自定义字段43
     */
    @Setter
    @Getter
    private String column43 = "";
    /**
     * .
     * 自定义字段44
     */
    @Setter
    @Getter
    private String column44 = "";
    /**
     * .
     * 自定义字段45
     */
    @Setter
    @Getter
    private String column45 = "";
    /**
     * .
     * 自定义字段46
     */
    @Setter
    @Getter
    private String column46 = "";
    /**
     * .
     * 自定义字段47
     */
    @Setter
    @Getter
    private String column47 = "";
    /**
     * .
     * 自定义字段48
     */
    @Setter
    @Getter
    private String column48 = "";
    /**
     * .
     * 自定义字段49
     */
    @Setter
    @Getter
    private String column49 = "";
    /**
     * .
     * 自定义字段50
     */
    @Setter
    @Getter
    private String column50 = "";
    /**
     * .
     * 业务类型
     */
    @Setter
    @Getter
    private String proType;
}
