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
public class CardData {
    private List<CardItem> cardList;
    private MonitorData monitorData;

    @Override
    public String toString() {
        return "CardData{" +
            "cardList=" + cardList +
            ", monitorData=" + monitorData +
            '}';
    }
}
