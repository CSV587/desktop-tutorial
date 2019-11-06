package com.hy.cpic.reporting.callbackrate.controller;

import com.github.pagehelper.Page;
import com.hy.cpic.base.bean.BeanTools;
import com.hy.cpic.base.excel.ReportExcel;
import com.hy.cpic.base.excel.ReportUtils;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.base.page.TableHeadsFactory;
import com.hy.cpic.reporting.callbackrate.page.CallBackRatePage;
import com.hy.cpic.reporting.callbackrate.service.CallBackRateService;
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
@RequestMapping("/reporting/callBackRate")
public class CallBackRateController {
    private final CallBackRateService callBackRateService;

    @Autowired
    public CallBackRateController(CallBackRateService callBackRateService) {
        this.callBackRateService = callBackRateService;
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "/callbackrate";
    }

    @ResponseBody
    @PostMapping(value = "query")
    public PageResult query(@RequestBody CallBackRatePage callBackRatePage) {
        Page result = callBackRateService.query(callBackRatePage);
        return PageResult.success("success", result, TableHeadsFactory.createCallBackRateHead());
    }

    @ResponseBody
    @PostMapping(value = "init")
    public PageResult init() {
        return PageResult.success("success", new Page(), TableHeadsFactory.createCallBackRateHead());
    }

    @RequestMapping("/excel")
    public void excel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> objectMap = request.getParameterMap();
        CallBackRatePage callBackRatePage = new CallBackRatePage();
        try {
            BeanTools.transMap2Bean(callBackRatePage, objectMap);
            List<CallBackRatePage> page = callBackRateService.queryAll(callBackRatePage);
            ReportExcel reportExcel = new ReportExcel();
            reportExcel.excelExport(page, "回访统计报表_" + ReportUtils.getTimeString(), CallBackRatePage.class, 1, response, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
