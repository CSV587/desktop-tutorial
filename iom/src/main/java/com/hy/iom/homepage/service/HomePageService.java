package com.hy.iom.homepage.service;

import com.hy.iom.homepage.page.BarData;
import com.hy.iom.homepage.page.BarItem;
import com.hy.iom.homepage.page.BarResult;
import com.hy.iom.homepage.page.CardData;
import com.hy.iom.homepage.page.CardItem;
import com.hy.iom.homepage.page.GaugeResult;
import com.hy.iom.homepage.page.HomeBody;
import com.hy.iom.homepage.page.HomePage;
import com.hy.iom.homepage.page.HomeResult;
import com.hy.iom.homepage.page.MonitorData;
import com.hy.iom.homepage.page.MonitorItem;
import com.hy.iom.mapper.oracle.HomePageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2019-03-27
 * user: lxg
 * package_name: com.hy.iom.homepage.service
 */
@Service
public class HomePageService {
    private final HomePageMapper homePageMapper;

    @Autowired
    public HomePageService(HomePageMapper mapper) {
        this.homePageMapper = mapper;
    }

    public HomeResult queryPage(HomePage homePage) {
        HomePage page = homePageMapper.querySummaryData(homePage);
        String answerCount = page.getAnswerCount();
        String callCount = page.getCallCount();
        String successRate = page.getSuccessRate();
        CardItem answerItem = new CardItem(1, "呼叫总量", "通", callCount);
        CardItem callItem = new CardItem(2, "接听总量", "通", answerCount);
        CardItem successItem = new CardItem(3, "成功率", "百分比", successRate);
        List<CardItem> items = new ArrayList<>();
        items.add(answerItem);
        items.add(callItem);
        items.add(successItem);
        MonitorData monitorData = new MonitorData();
        monitorData.setName("调用接口");
        monitorData.setUnit("通");
        MonitorItem getError = new MonitorItem(1, "获取数据失败", page.getGainException());
        MonitorItem sendError = new MonitorItem(2, "执行交易失败", page.getCallException());
        List<MonitorItem> monitorItems = new ArrayList<>();
        monitorItems.add(getError);
        monitorItems.add(sendError);
        monitorData.setMonitorList(monitorItems);
        CardData data = new CardData();
        data.setCardList(items);
        data.setMonitorData(monitorData);
        HomeBody homeBody = new HomeBody();
        homeBody.setCardData(data);
        homeBody.setBarData(queryAve(homePage));
        HomePage newPage = homePageMapper.queryConnect(homePage);
        List<String> warnList = new ArrayList<>();
        warnList.add(newPage.getAdvancedValue());
        warnList.add(newPage.getMiddleValue());
        warnList.add(newPage.getLowValue());
        homeBody.setWarningList(warnList);
        homeBody.setConnectRate(newPage.getActualValue());
        return HomeResult.success(homeBody);
    }

    private BarData queryAve(HomePage homePage) {
        List<BarItem> barItemList = homePageMapper.queryAveDuration(homePage);
        BarData barData = new BarData();
        barData.setList(barItemList);
        barData.setToday(new SimpleDateFormat("MM-dd").format(new Date()));
        return barData;
    }

    public BarResult queryBar(HomePage homePage) {
        return BarResult.success(queryAve(homePage));
    }

    public GaugeResult queryGauge(HomePage homePage) {
        HomePage page = homePageMapper.queryConnect(homePage);
        List<String> items = new ArrayList<>();
        items.add(page.getAdvancedValue());
        items.add(page.getMiddleValue());
        items.add(page.getLowValue());
        return GaugeResult.success(items, page.getActualValue());
    }
}
