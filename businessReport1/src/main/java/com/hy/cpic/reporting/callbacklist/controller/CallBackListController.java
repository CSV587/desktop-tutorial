package com.hy.cpic.reporting.callbacklist.controller;

import com.github.pagehelper.Page;
import com.hy.cpic.base.bean.BeanTools;
import com.hy.cpic.base.excel.ReportExcel;
import com.hy.cpic.base.excel.ReportUtils;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.base.page.TableHeadsFactory;
import com.hy.cpic.reporting.callbacklist.page.CallBackListPage;
import com.hy.cpic.reporting.callbacklist.service.CallBackListService;
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
@RequestMapping("/reporting/callBackList")
public class CallBackListController {
    private final CallBackListService callBackListService;

    @Autowired
    public CallBackListController(CallBackListService callBackListService) {
        this.callBackListService = callBackListService;
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "/callbacklist";
    }

    @ResponseBody
    @PostMapping(value = "query")
    public PageResult query(@RequestBody CallBackListPage callBackListPage) {
        Page result = callBackListService.query(callBackListPage);
        return PageResult.success("success", result, TableHeadsFactory.createCallBackListHead());
    }

    @ResponseBody
    @PostMapping(value = "init")
    public PageResult init() {
        return PageResult.success("success", new Page(), TableHeadsFactory.createCallBackListHead());
    }

    @RequestMapping("/excel")
    public void excel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> objectMap = request.getParameterMap();
        CallBackListPage callBackListPage = new CallBackListPage();
        try {
            BeanTools.transMap2Bean(callBackListPage, objectMap);
            List<CallBackListPage> page = callBackListService.queryAll(callBackListPage);
            ReportExcel reportExcel = new ReportExcel();
//            String a =;
//            a.rep
            reportExcel.excelExport(page, "回访明细报表_" + ReportUtils.getTimeString(), CallBackListPage.class, 1, response, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
