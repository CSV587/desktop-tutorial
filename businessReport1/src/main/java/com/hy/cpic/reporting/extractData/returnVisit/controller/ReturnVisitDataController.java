package com.hy.cpic.reporting.extractData.returnVisit.controller;

import com.github.pagehelper.Page;
import com.hy.cpic.base.bean.BeanTools;
import com.hy.cpic.base.excel.ReportExcel;
import com.hy.cpic.base.excel.ReportUtils;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.base.page.TableHeadsFactory;
import com.hy.cpic.reporting.extractData.returnVisit.page.ReturnVisitDataPage;
import com.hy.cpic.reporting.extractData.returnVisit.service.ReturnVisitDataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reporting")
public class ReturnVisitDataController {

    private final ReturnVisitDataService returnVisitDataService;

    public ReturnVisitDataController(ReturnVisitDataService returnVisitDataService) {
        this.returnVisitDataService = returnVisitDataService;
    }

    @RequestMapping(value = "/returnVisitData/index", method = RequestMethod.GET)
    public String index() {
        return "/returnVisitData";
    }

    @ResponseBody
    @PostMapping(value = "/extractData/returnVisit/query")
    public PageResult query(@RequestBody ReturnVisitDataPage returnVisitDataPage) {
        Page result = returnVisitDataService.query(returnVisitDataPage);
        return PageResult.success("success", result, TableHeadsFactory.createReturnVisitDataHead(returnVisitDataPage.getDimension()));
    }

    @ResponseBody
    @PostMapping(value = "/extractData/returnVisit/init")
    public PageResult init() {
        return PageResult.success("success", new Page(), TableHeadsFactory.createReturnVisitDataHead("center"));
    }

    @RequestMapping("/extractData/returnVisit/excel")
    public void excel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> objectMap = request.getParameterMap();
        ReturnVisitDataPage returnVisitDataPage = new ReturnVisitDataPage();
        try {
            BeanTools.transMap2Bean(returnVisitDataPage, objectMap);
            List<ReturnVisitDataPage> page = returnVisitDataService.queryAll(returnVisitDataPage);
            ReportExcel reportExcel = new ReportExcel();
            reportExcel.excelExport(page, "车险回访数据_" + ReportUtils.getTimeString(), ReturnVisitDataPage.class, 1, response, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/extractData/returnVisit/generateUnrealNumberFile")
    public void generateUnrealNumberFile() throws IOException {
        returnVisitDataService.generateFileSchedule();
    }
}
