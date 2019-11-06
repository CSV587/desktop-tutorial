package com.hy.cpic.reporting.intentionStatistics.controller;

import com.github.pagehelper.Page;
import com.hy.cpic.base.bean.BeanTools;
import com.hy.cpic.base.excel.ReportExcel;
import com.hy.cpic.base.excel.ReportUtils;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.base.page.TableHeadsFactory;
import com.hy.cpic.reporting.intentionStatistics.page.IntentionStatisticsPage;
import com.hy.cpic.reporting.intentionStatistics.service.IntentionStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
@Controller
@RequestMapping("/reporting/intentionStatistics")
public class IntentionStatisticsController {
    private final IntentionStatisticsService intentionStatisticsService;

    @Autowired
    public IntentionStatisticsController(IntentionStatisticsService intentionStatisticsService) {
        this.intentionStatisticsService = intentionStatisticsService;
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "/intentionstatistics";
    }

    @ResponseBody
    @PostMapping(value = "query")
    public PageResult query(@RequestBody IntentionStatisticsPage intentionStatisticsPage) {
        Page result = intentionStatisticsService.query(intentionStatisticsPage);
        return PageResult.success("success", result, TableHeadsFactory.createIntentionStatisticsHead());
    }

    @ResponseBody
    @PostMapping(value = "init")
    public PageResult init() {
        return PageResult.success("success", new Page(), TableHeadsFactory.createIntentionStatisticsHead());
    }

    @RequestMapping("/excel")
    public void excel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> objectMap = request.getParameterMap();
        IntentionStatisticsPage intentionStatisticsPage = new IntentionStatisticsPage();
        try {
            BeanTools.transMap2Bean(intentionStatisticsPage, objectMap);
            List<IntentionStatisticsPage> page = intentionStatisticsService.queryAll(intentionStatisticsPage);
            ReportExcel reportExcel = new ReportExcel();
            reportExcel.excelExport(page, "new投保意向统计报表_" + ReportUtils.getTimeString(), IntentionStatisticsPage.class, 1, response, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
