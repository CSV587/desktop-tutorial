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
@Setter
@Getter
public class MonitorItem {
    private Integer id;
    private String txt;
    private String count;

    public MonitorItem(Integer id, String txt, String count) {
        this.id = id;
        this.txt = txt;
        this.count = count;
    }

    @Override
    public String toString() {
        return "MonitorItem{" +
            "id=" + id +
            ", txt=" + txt +
            ", count=" + count +
            '}';
    }
}
