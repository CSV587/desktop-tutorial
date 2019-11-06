package com.hy.iom.homepage.page;

import lombok.Getter;
import lombok.Setter;

/**
 * .
 * Created by of liaoxg
 * date: 2019-03-27
 * user: lxg
 * package_name: com.hy.iom.homepage.page
 */
public class BarItem {
    @Getter
    @Setter
    private String date;
    @Getter
    @Setter
    private String advancedValue;
    @Getter
    @Setter
    private String middleValue;
    @Getter
    @Setter
    private String lowValue;
    @Getter
    @Setter
    private String actualValue;

    private String onDate;

    @Override
    public String toString() {
        return "BarItem{" +
            "date=" + date +
            ", onDate=" + onDate +
            ", advancedValue=" + advancedValue +
            ", middleValue=" + middleValue +
            ", lowValue=" + lowValue +
            ", actualValue=" + actualValue +
            '}';
    }

    public void setOnDate(String onDate) {
        this.onDate = onDate;
        this.date = onDate;
    }

    public String getOnDate() {
        return this.onDate;
    }
}
