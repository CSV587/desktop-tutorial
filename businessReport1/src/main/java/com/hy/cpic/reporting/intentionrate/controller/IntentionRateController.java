package com.hy.cpic.reporting.intentionrate.controller;

import com.github.pagehelper.Page;
import com.hy.cpic.base.bean.BeanTools;
import com.hy.cpic.base.excel.ReportExcel;
import com.hy.cpic.base.excel.ReportUtils;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.base.page.TableHeadsFactory;
import com.hy.cpic.reporting.intentionrate.page.IntentionRatePage;
import com.hy.cpic.reporting.intentionrate.service.IntentionRateService;
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
@RequestMapping("/reporting/intentionRate")
public class IntentionRateController {
    private final IntentionRateService intentionRateService;

    @Autowired
    public IntentionRateController(IntentionRateService intentionRateService) {
        this.intentionRateService = intentionRateService;
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "/intentionrate";
    }

    @ResponseBody
    @PostMapping(value = "query")
    public PageResult query(@RequestBody IntentionRatePage intentionRatePage) {
        Page result = intentionRateService.query(intentionRatePage);
        return PageResult.success("success", result, TableHeadsFactory.createIntentionRateHead());
    }

    @ResponseBody
    @PostMapping(value = "init")
    public PageResult init() {
        return PageResult.success("success", new Page(), TableHeadsFactory.createIntentionRateHead());
    }

    @RequestMapping("/excel")
    public void excel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> objectMap = request.getParameterMap();
        IntentionRatePage intentionRatePage = new IntentionRatePage();
        try {
            BeanTools.transMap2Bean(intentionRatePage, objectMap);
            List<IntentionRatePage> page = intentionRateService.queryAll(intentionRatePage);
            ReportExcel reportExcel = new ReportExcel();
            reportExcel.excelExport(page, "投保意向统计报表_" + ReportUtils.getTimeString(), IntentionRatePage.class, 1, response, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
