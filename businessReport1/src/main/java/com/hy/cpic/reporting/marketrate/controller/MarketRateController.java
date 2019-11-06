package com.hy.cpic.reporting.marketrate.controller;

import com.github.pagehelper.Page;
import com.hy.cpic.base.api.IPMApi;
import com.hy.cpic.base.bean.BeanTools;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.base.page.TableHeadsFactory;
import com.hy.cpic.base.utils.ErrorUtil;
import com.hy.cpic.reporting.marketrate.page.MarketRatePage;
import com.hy.cpic.reporting.marketrate.service.MarketRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * .
 * Created by of liaoxg
 * date: 2018/9/8
 * user: lxg
 * package_name: com.hy.cpic.reporting.callbackrate.controller
 */
@Slf4j
@Controller
@RequestMapping("/reporting/marketRate")
public class MarketRateController {
    private final MarketRateService marketRateService;

    private final IPMApi api;

    @Autowired
    public MarketRateController(MarketRateService marketRateService, IPMApi api) {
        this.marketRateService = marketRateService;
        this.api = api;
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "/marketrate";
    }

    @ResponseBody
    @PostMapping(value = "query")
    public PageResult query(@RequestBody MarketRatePage marketRatePage, @RequestParam String token) {
        List<String> projectIdList = api.getUserProjectIdList(token);
        marketRatePage.setProjectList(projectIdList);
        Page result = marketRateService.query(marketRatePage);
        return PageResult.success("success", result, TableHeadsFactory.createMarketRateHead());
    }

    @ResponseBody
    @PostMapping(value = "init")
    public PageResult init() {
        return PageResult.success("success", new Page(), TableHeadsFactory.createMarketRateHead());
    }

    @RequestMapping("/excel")
    public void excel(HttpServletRequest request, HttpServletResponse response, @RequestParam String token) {
        Map<String, String[]> objectMap = request.getParameterMap();
        MarketRatePage marketRatePage = new MarketRatePage();
        try {
            BeanTools.transMap2Bean(marketRatePage, objectMap);
            List<String> projectIdList = api.getUserProjectIdList(token);
            marketRatePage.setProjectList(projectIdList);
            marketRateService.exportExecl(marketRatePage, response, request);
        } catch (Exception e) {
            log.error("{}", ErrorUtil.getStackTrace(e));
        }
    }
}
