package com.hy.cpic.reporting.intentionlist.controller;

import com.github.pagehelper.Page;
import com.hy.cpic.base.bean.BeanTools;
import com.hy.cpic.base.excel.ReportExcel;
import com.hy.cpic.base.excel.ReportUtils;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.base.page.TableHeadsFactory;
import com.hy.cpic.reporting.intentionlist.page.IntentionListPage;
import com.hy.cpic.reporting.intentionlist.service.IntentionListService;
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
@RequestMapping("/reporting/intentionList")
public class IntentionListController {
    private final IntentionListService intentionListService;

    @Autowired
    public IntentionListController(IntentionListService intentionListService) {
        this.intentionListService = intentionListService;
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "/intentionlist";
    }

    @ResponseBody
    @PostMapping(value = "query")
    public PageResult query(@RequestBody IntentionListPage intentionListPage) {
        Page result = intentionListService.query(intentionListPage);
        return PageResult.success("success", result, TableHeadsFactory.createIntentionListHead());
    }

    @ResponseBody
    @PostMapping(value = "init")
    public PageResult init() {
        return PageResult.success("success", new Page(), TableHeadsFactory.createIntentionListHead());
    }

    @RequestMapping("/excel")
    public void excel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> objectMap = request.getParameterMap();
        IntentionListPage intentionListPage = new IntentionListPage();
        try {
            BeanTools.transMap2Bean(intentionListPage, objectMap);
            List<IntentionListPage> page = intentionListService.queryAll(intentionListPage);
            ReportExcel reportExcel = new ReportExcel();
            reportExcel.excelExport(page, "投保意向明细报表_" + ReportUtils.getTimeString(), IntentionListPage.class, 1, response, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
