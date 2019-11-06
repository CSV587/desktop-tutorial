package com.hy.cpic.reporting.ccmonitor.controller;

import com.github.pagehelper.Page;
import com.hy.cpic.base.bean.BeanTools;
import com.hy.cpic.base.excel.ReportExcel;
import com.hy.cpic.base.excel.ReportUtils;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.base.page.TableHeadsFactory;
import com.hy.cpic.reporting.ccmonitor.page.CCMonitorPage;
import com.hy.cpic.reporting.ccmonitor.service.CCMonitorService;
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
 * @ Description ：该报表用于对并发量进行统计，以日期、职场、分公司、团队、片区、车牌号为查询维度，统计其时段外呼请求量、实际外呼量以及超出并发量。拥有主管权限的用户才能查看该报表。
 * @ Modified By ：
 * @ Version     ：1.0
 */
@Controller
@RequestMapping("/reporting/ccmonitor")
public class CCMonitorController {

    private final CCMonitorService ccMonitorService;

    @Autowired
    public CCMonitorController(CCMonitorService ccMonitorService) {
        this.ccMonitorService = ccMonitorService;
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "/ccmonitor";
    }

    @ResponseBody
    @PostMapping(value = "query")
    public PageResult query(@RequestBody CCMonitorPage ccMonitorPage) {
        Page result = ccMonitorService.query(ccMonitorPage);
        return PageResult.success("success", result, TableHeadsFactory.createCCMonitorHead());
    }

    @ResponseBody
    @PostMapping(value = "init")
    public PageResult init() {
        return PageResult.success("success", new Page(), TableHeadsFactory.createCCMonitorHead());
    }

    @RequestMapping("/excel")
    public void excel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> objectMap = request.getParameterMap();
        CCMonitorPage ccMonitorPage = new CCMonitorPage();
        try {
            BeanTools.transMap2Bean(ccMonitorPage, objectMap);
            List<CCMonitorPage> result = ccMonitorService.queryAll(ccMonitorPage);
            ReportExcel reportExcel = new ReportExcel();
            reportExcel.excelExport(result, "并发量管理报表_" + ReportUtils.getTimeString(), CCMonitorPage.class, 1, response, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
