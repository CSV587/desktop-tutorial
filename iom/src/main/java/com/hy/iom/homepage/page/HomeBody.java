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
public class HomeBody {
    private CardData cardData;
    private List<String> warningList;
    private String connectRate;
    private BarData barData;

    @Override
    public String toString() {
        return "HomeBody{" +
            "cardData=" + cardData +
            ", warningList=" + warningList +
            ", connectRate=" + connectRate +
            ", barData=" + barData +
            '}';
    }
}
