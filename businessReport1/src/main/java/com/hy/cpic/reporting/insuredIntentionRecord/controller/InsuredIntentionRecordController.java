package com.hy.cpic.reporting.insuredIntentionRecord.controller;

import com.github.pagehelper.Page;
import com.hy.cpic.base.bean.BeanTools;
import com.hy.cpic.base.excel.ReportExcel;
import com.hy.cpic.base.excel.ReportUtils;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.base.page.TableHeadsFactory;
import com.hy.cpic.reporting.insuredIntentionRecord.page.InsuredIntentionRecordPage;
import com.hy.cpic.reporting.insuredIntentionRecord.service.InsuredIntentionRecordService;
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
@RequestMapping("/reporting/insuredIntentionRecord")
public class InsuredIntentionRecordController {

    private final InsuredIntentionRecordService insuredIntentionRecordService;

    public InsuredIntentionRecordController(InsuredIntentionRecordService insuredIntentionRecordService) {
        this.insuredIntentionRecordService = insuredIntentionRecordService;
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "insuredIntentionRecord";
    }

    @ResponseBody
    @PostMapping(value = "/query")
    public PageResult query(@RequestBody InsuredIntentionRecordPage insuredIntentionRecordPage) {
        Page result = insuredIntentionRecordService.query(insuredIntentionRecordPage);
        return PageResult.success("success", result, TableHeadsFactory.createInsuredIntentionRecordHead());
    }

    @ResponseBody
    @PostMapping(value = "/init")
    public PageResult init() {
        return PageResult.success("success", new Page(), TableHeadsFactory.createInsuredIntentionRecordHead());
    }

    @RequestMapping("/excel")
    public void excel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> objectMap = request.getParameterMap();
        InsuredIntentionRecordPage insuredIntentionRecordPage = new InsuredIntentionRecordPage();
        try {
            BeanTools.transMap2Bean(insuredIntentionRecordPage, objectMap);
            List<InsuredIntentionRecordPage> page = insuredIntentionRecordService.queryAll(insuredIntentionRecordPage);
            ReportExcel reportExcel = new ReportExcel();
            reportExcel.excelExport(page, "机器人外呼数据月报" + ReportUtils.getTimeString(), InsuredIntentionRecordPage.class, 1, response, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
