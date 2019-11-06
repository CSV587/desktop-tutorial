package com.hy.iom.homepage.page;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2019-03-27
 * user: lxg
 * package_name: com.hy.iom.homepage.page
 */
@Setter
@Getter
public class HomePage {
    private String barData;
    private String gaugeData;
    private String data;
    private String callCount;
    private String answerCount;
    private String successRate;
    private List<String> projectList;
    private String advancedValue;
    private String middleValue;
    private String lowValue;
    private String actualValue;
    private String gainException;
    private String callException;
}
