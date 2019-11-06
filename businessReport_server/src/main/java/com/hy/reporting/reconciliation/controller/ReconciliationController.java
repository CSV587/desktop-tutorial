package com.hy.reporting.reconciliation.controller;

import com.github.pagehelper.Page;
import com.hy.base.bean.BeanTools;
import com.hy.base.excel.ReportExcel;
import com.hy.base.page.PageBody;
import com.hy.base.page.ResponseResult;
import com.hy.base.utils.PageUtils;
import com.hy.reporting.reconciliation.page.ReconciliationPage;
import com.hy.reporting.reconciliation.service.ReconciliationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-26
 * user: lxg
 * package_name: com.hy.reporting.reconciliation.controller
 */
@Controller
@RequestMapping("/reporting/reconciliation")
public class ReconciliationController {
    /**
     * .
     * ReconciliationService
     */
    private final ReconciliationService reconciliationService;

    /**
     * .
     * 构造函数
     *
     * @param service ReconciliationService
     */
    public ReconciliationController(
        final ReconciliationService service) {
        this.reconciliationService = service;
    }


    /**
     * .
     * index页面
     *
     * @return 对应前端文件路径
     */
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "/returnLog";
    }


    /**
     * .
     * 查询数据
     *
     * @param reconciliationPage ReconciliationPage
     * @return 返回指定页数据
     */
    @ResponseBody
    @PostMapping(value = "query")
    public ResponseResult query(
        @RequestBody final ReconciliationPage reconciliationPage) {
        Page page = reconciliationService.query(reconciliationPage);
        PageBody body = new PageBody();
        body.setValue(page.getResult());
        body.setCount(page.getTotal());
        body.setTHeader(PageUtils.getExcelTitle(ReconciliationPage.class));
        return ResponseResult.success("success", body);
    }

    /**
     * .
     * 初始化字段头
     *
     * @return 结果
     */
    @ResponseBody
    @PostMapping(value = "init")
    public ResponseResult init() {
        PageBody body = new PageBody();
        body.setTHeader(PageUtils.getExcelTitle(ReconciliationPage.class));
        body.setValue(new ArrayList());
        body.setCount((long) 0);
        return ResponseResult.success("success", body);
    }

    /**
     * .
     * Excel导出
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping("/execl")
    public void excel(final HttpServletRequest request,
                      final HttpServletResponse response) {
        Map<String, String[]> objectMap = request.getParameterMap();
        ReconciliationPage page = new ReconciliationPage();
        try {
            BeanTools.transMap2Bean(page, objectMap);
            List<ReconciliationPage> pages
                = reconciliationService.queryAll(page);
            ReportExcel.excelExport(pages,
                "对账统计报表",
                ReconciliationPage.class,
                1,
                response,
                request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
