package com.hy.cpic.reporting.codetype.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.cpic.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class CodeTypePage extends BasePage {

    private String type;

    private String code;

    private String content;

    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp fst;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp lmt;

    private String foid;

    private String loid;

    private String foidName;

    private String loidName;
}
