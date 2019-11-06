package com.hy.cpic.reporting.marketlist.controller;

import com.github.pagehelper.Page;
import com.hy.cpic.base.api.ICMApi;
import com.hy.cpic.base.bean.BeanTools;
import com.hy.cpic.base.page.PageResultTM;
import com.hy.cpic.base.page.TableHeadsFactory;
import com.hy.cpic.base.utils.ErrorUtil;
import com.hy.cpic.reporting.marketlist.page.MarketListPage;
import com.hy.cpic.reporting.marketlist.service.MarketListService;
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
import java.util.Map;

/**
 * .
 * Created by of liaoxg
 * date: 2019-03-18
 * user: lxg
 * package_name: com.hy.cpic.reporting.marketlist.controller
 */
@Slf4j
@Controller
@RequestMapping("/reporting/hangupSecondNode")
public class MarketListController {
    private final MarketListService marketListService;
    private final ICMApi icmApi;

    @Autowired
    public MarketListController(MarketListService marketListService, ICMApi icmApi) {
        this.marketListService = marketListService;
        this.icmApi = icmApi;
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "/hangUpNode";
    }

    @ResponseBody
    @PostMapping(value = "query")
    public PageResultTM query(@RequestBody MarketListPage marketListPage, @RequestParam int current, @RequestParam int pageSize) {
        marketListPage.setCurrent(current);
        marketListPage.setPageSize(pageSize);
        marketListPage.setRuleName(icmApi.getRuleName(marketListPage.getRuleId()));
        Page result = marketListService.query(marketListPage);
        return PageResultTM.success("success", result, TableHeadsFactory.createMarketListHead());
    }

    @ResponseBody
    @PostMapping(value = "headQuery")
    public PageResultTM init() {
        return PageResultTM.success("success", new Page(), TableHeadsFactory.createMarketListHead());
    }

    @RequestMapping("/excel")
    public void excel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> objectMap = request.getParameterMap();
        MarketListPage marketListPage = new MarketListPage();
        try {
            BeanTools.transMap2Bean(marketListPage, objectMap);
            marketListPage.setRuleName(icmApi.getRuleName(marketListPage.getRuleId()));
            marketListService.exportExecl(marketListPage, response, request);
        } catch (Exception e) {
            log.error("{}", ErrorUtil.getStackTrace(e));
        }
    }
}
