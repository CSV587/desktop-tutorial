package com.hy.cpic.reporting.calldetail.controller;

import com.github.pagehelper.Page;
import com.hy.cpic.base.bean.BeanTools;
import com.hy.cpic.base.excel.ReportExcel;
import com.hy.cpic.base.excel.ReportUtils;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.base.page.TableHeadsFactory;
import com.hy.cpic.reporting.calldetail.page.CallDetailPage;
import com.hy.cpic.reporting.calldetail.service.CallDetailService;
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
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 16:03 2018/8/24
 * @ Description ：外呼明细报表 用于统计查看每天外呼的总体情况，以日期、职场、分公司、团队、片区为查询维度，对每日外呼情况做出统计，拥有主管权限的用户才能查看该报表
 * @ Modified By ：
 * @ Version     ：1.0
 */
@Controller
@RequestMapping("/reporting/callDetail")
public class CallDetailController {

    private final CallDetailService callDetailService;

    @Autowired
    public CallDetailController(CallDetailService callDetailService) {
        this.callDetailService = callDetailService;
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "/calldetail";
    }

    @ResponseBody
    @PostMapping(value = "query")
    public PageResult query(@RequestBody CallDetailPage callDetailPage) {
        Page result = callDetailService.query(callDetailPage);
        return PageResult.success("success", result, TableHeadsFactory.createCallDetailHead());
    }

    @ResponseBody
    @PostMapping(value = "init")
    public PageResult init() {
        return PageResult.success("success", new Page(), TableHeadsFactory.createCallDetailHead());
    }

    @RequestMapping("/excel")
    public void excel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> objectMap = request.getParameterMap();
        CallDetailPage callDetailPage = new CallDetailPage();
        try {
            BeanTools.transMap2Bean(callDetailPage, objectMap);
            List<CallDetailPage> page = callDetailService.queryAll(callDetailPage);
            ReportExcel reportExcel = new ReportExcel();
            reportExcel.excelExport(page, "外呼任务监控报表_" + ReportUtils.getTimeString(), CallDetailPage.class, 1, response, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
