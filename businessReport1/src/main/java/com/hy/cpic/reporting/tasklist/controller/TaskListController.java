package com.hy.cpic.reporting.tasklist.controller;

import com.github.pagehelper.Page;
import com.hy.cpic.base.bean.BeanTools;
import com.hy.cpic.base.excel.ReportExcel;
import com.hy.cpic.base.excel.ReportUtils;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.base.page.TableHeadsFactory;
import com.hy.cpic.reporting.tasklist.page.TaskListPage;
import com.hy.cpic.reporting.tasklist.service.TaskListService;
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
 * @ Description ：该报表用于查看每日外呼任务的完成情况，以日期、职场、分公司、团队、片区、车牌号为查询维度，对其外呼情况进行监控。拥有主管权限的用户才能查看该报表。
 * @ Modified By ：
 * @ Version     ：1.0
 */
@Controller
@RequestMapping("/reporting/tasklist")
public class TaskListController {

    private final TaskListService taskListService;

    @Autowired
    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "/calltask";
    }

    @ResponseBody
    @PostMapping(value = "query")
    public PageResult query(@RequestBody TaskListPage taskListPage) {
        Page result = taskListService.query(taskListPage);
        return PageResult.success("success", result, TableHeadsFactory.createTaskListHead());
    }

    @ResponseBody
    @PostMapping(value = "init")
    public PageResult init() {
        return PageResult.success("success", new Page(), TableHeadsFactory.createTaskListHead());
    }

    @RequestMapping("/excel")
    public void excel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> objectMap = request.getParameterMap();
        TaskListPage taskListPage = new TaskListPage();
        try {
            BeanTools.transMap2Bean(taskListPage, objectMap);
            List<TaskListPage> result = taskListService.queryAll(taskListPage);
            ReportExcel reportExcel = new ReportExcel();
            reportExcel.excelExport(result, "外呼明细报表_" + ReportUtils.getTimeString(), TaskListPage.class, 1, response, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
