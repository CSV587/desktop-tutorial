package com.hy.iom.homepage.controller;

import com.hy.iom.base.api.IPMApi;
import com.hy.iom.homepage.page.BarResult;
import com.hy.iom.homepage.page.GaugeResult;
import com.hy.iom.homepage.page.HomePage;
import com.hy.iom.homepage.page.HomeResult;
import com.hy.iom.homepage.service.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2019-03-27
 * user: lxg
 * package_name: com.hy.iom.homepage.controller
 */
@RestController
@RequestMapping("/homepage")
public class HomePageController {
    private final HomePageService pageService;
    private final IPMApi ipmApi;

    @Autowired
    public HomePageController(HomePageService pageService, IPMApi ipmApi) {
        this.pageService = pageService;
        this.ipmApi = ipmApi;
    }

    /**
     * 主页数据获取
     *
     * @param homePage 请求参数接受
     * @return 结果
     */
    @ResponseBody
    @PostMapping("/queryPage")
    public HomeResult queryPage(@RequestBody HomePage homePage, @RequestParam String token) {
        List<String> projectIdList = ipmApi.getUserProjectIdList(token);
        homePage.setProjectList(projectIdList);
        return pageService.queryPage(homePage);
    }

    /**
     * 柱状图数据获取
     *
     * @param homePage 请求参数接受
     * @return 结果
     */
    @ResponseBody
    @PostMapping("/queryBar")
    public BarResult queryBar(@RequestBody HomePage homePage, @RequestParam String token) {
        List<String> projectIdList = ipmApi.getUserProjectIdList(token);
        homePage.setProjectList(projectIdList);
        return pageService.queryBar(homePage);
    }

    /**
     * 仪表图数据获取
     *
     * @param homePage 请求参数接受
     * @return 结果
     */
    @ResponseBody
    @PostMapping("/queryGauge")
    public GaugeResult queryGauge(@RequestBody HomePage homePage, @RequestParam String token) {
        List<String> projectIdList = ipmApi.getUserProjectIdList(token);
        homePage.setProjectList(projectIdList);
        return pageService.queryGauge(homePage);
    }
}
