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
public class CardItem {
    private Integer id;
    private String name;
    private String unit;
    private String count;

    public CardItem(Integer id, String name, String unit, String count) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.count = count;
    }

    @Override
    public String toString() {
        return "CardItem{" +
            "id=" + id +
            ", name=" + name +
            ", unit=" + unit +
            ", count=" + count +
            '}';
    }
}
