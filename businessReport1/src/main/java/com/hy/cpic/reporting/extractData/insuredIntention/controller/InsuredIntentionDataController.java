package com.hy.cpic.reporting.extractData.insuredIntention.controller;

import com.github.pagehelper.Page;
import com.hy.cpic.base.bean.BeanTools;
import com.hy.cpic.base.excel.ReportExcel;
import com.hy.cpic.base.excel.ReportUtils;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.base.page.TableHeadsFactory;
import com.hy.cpic.reporting.extractData.insuredIntention.page.InsuredIntentionDataPage;
import com.hy.cpic.reporting.extractData.insuredIntention.service.InsuredIntentionDataService;
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

@Controller
@RequestMapping("/reporting")
public class InsuredIntentionDataController {

    private final InsuredIntentionDataService insuredIntentionDataService;

    public InsuredIntentionDataController(InsuredIntentionDataService insuredIntentionDataService) {
        this.insuredIntentionDataService = insuredIntentionDataService;
    }

    @RequestMapping(value = "/insuredIntentionData/index", method = RequestMethod.GET)
    public String index() {
        return "insuredIntentionData";
    }

    @ResponseBody
    @PostMapping(value = "/extractData/insuredIntentionData/query")
    public PageResult query(@RequestBody InsuredIntentionDataPage insuredIntentionDataPage) {
        Page result = insuredIntentionDataService.query(insuredIntentionDataPage);
        return PageResult.success("success", result, TableHeadsFactory.createInsuredIntentionDataHead());
    }

    @ResponseBody
    @PostMapping(value = "/extractData/insuredIntentionData/init")
    public PageResult init() {
        return PageResult.success("success", new Page(), TableHeadsFactory.createInsuredIntentionDataHead());
    }

    @RequestMapping("/extractData/insuredIntentionData/excel")
    public void excel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> objectMap = request.getParameterMap();
        InsuredIntentionDataPage returnVisitDataPage = new InsuredIntentionDataPage();
        try {
            BeanTools.transMap2Bean(returnVisitDataPage, objectMap);
            List<InsuredIntentionDataPage> page = insuredIntentionDataService.queryAll(returnVisitDataPage);
            ReportExcel reportExcel = new ReportExcel();
            reportExcel.excelExport(page, "投保意向数据_" + ReportUtils.getTimeString(), InsuredIntentionDataPage.class, 1, response, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
