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
public class BarData {
    private List<BarItem> list;
    private String today;

    @Override
    public String toString() {
        return "BarData{" +
            "list=" + list +
            ", today=" + today +
            '}';
    }
}
