package com.hy.cpic.reporting.tasklist.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.cpic.base.utils.DateUtils;
import com.hy.cpic.base.utils.PageUtils;
import com.hy.cpic.mapper.oracle.CallInfoStatisticsMapper;
import com.hy.cpic.reporting.tasklist.page.TaskListPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 17:37 2018/8/24
 * @ Description ：外呼任务监控服务类
 * @ Modified By ：
 * @ Version     ：1.0
 */
@Service
public class TaskListService {

    @Autowired
    private CallInfoStatisticsMapper callInfoStatisticsMapper;

    public Page query(TaskListPage page) {
        PageHelper.startPage(page.getCurrent(), page.getPageSize()); //分页利器
        page.setStartTime(DateUtils.startOfDay(page.getStartTime()));
        page.setEndTime(DateUtils.endOfDay(page.getEndTime()));
        List<TaskListPage> taskListPages = callInfoStatisticsMapper.queryTask(page);
        tranCallResult(taskListPages);
        PageUtils.randomId(taskListPages);
        return (Page) taskListPages;
    }

    public List<TaskListPage> queryAll(TaskListPage page) {
        page.setStartTime(DateUtils.startOfDay(page.getStartTime()));
        page.setEndTime(DateUtils.endOfDay(page.getEndTime()));
        List<TaskListPage> taskListPages = callInfoStatisticsMapper.queryTask(page);
        tranCallResult(taskListPages);
        return taskListPages;
    }

    private void tranCallResult(List<TaskListPage> taskListPages) {
        for (TaskListPage page : taskListPages) {
            if ("1".equals(page.getCallResult1())) {
                page.setCallResult1("失败");
            } else if ("0".equals(page.getCallResult1())) {
                page.setCallResult1("成功");
            }

            if (StringUtils.isNotBlank(page.getCallResult2())) {
                switch (page.getCallResult2()) {
                    case "0":
                        page.setCallResult2("确认成功");
                        break;
                    case "1":
                        page.setCallResult2("无人接听");
                        break;
                    case "2":
                        page.setCallResult2("信息有误");
                        break;
                    case "3":
                        page.setCallResult2("确认异常");
                        break;
                    case "4":
                        page.setCallResult2("确认不完整");
                        break;
                    case "5":
                        page.setCallResult2("提及新服务");
                        break;
                    case "102":
                        page.setCallResult2("确认不完整+信息有误");
                        break;
                    case "103":
                        page.setCallResult2("确认不完整+确认异常");
                        break;
                    case "104":
                        page.setCallResult2("确认不完整+提及新服务");
                        break;
                    case "105":
                        page.setCallResult2("确认不完整+提及新服务");
                        break;
                    default:
                        break;
                }
            }
        }
    }

}
