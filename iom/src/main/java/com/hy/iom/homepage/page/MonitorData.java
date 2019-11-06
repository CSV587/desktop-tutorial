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
public class MonitorData {
    private String name;
    private String unit;
    private List<MonitorItem> monitorList;

    @Override
    public String toString() {
        return "MonitorData{" +
            "name=" + name +
            ", unit=" + unit +
            ", monitorList=" + monitorList +
            '}';
    }
}
