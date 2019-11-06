package com.hy.cpic.reporting.ccmonitor.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.cpic.base.utils.DateUtils;
import com.hy.cpic.base.utils.PageUtils;
import com.hy.cpic.mapper.oracle.CallInfoStatisticsMapper;
import com.hy.cpic.reporting.ccmonitor.page.CCMonitorPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 17:37 2018/8/24
 * @ Description ：并发量监控服务类
 * @ Modified By ：
 * @ Version     ：1.0
 */
@Service
public class CCMonitorService {

    @Autowired
    private CallInfoStatisticsMapper callInfoStatisticsMapper;

    public Page query(CCMonitorPage page) {
        PageHelper.startPage(page.getCurrent(),page.getPageSize()); //分页利器
        page.setStartTime(DateUtils.startOfDay(page.getStartTime()));
        page.setEndTime(DateUtils.endOfDay(page.getEndTime()));
        List<CCMonitorPage> ccMonitorPages = callInfoStatisticsMapper.queryMonitor(page);
        PageUtils.randomId(ccMonitorPages);
        return (Page)ccMonitorPages;
    }

    public List<CCMonitorPage> queryAll(CCMonitorPage page) {
        page.setStartTime(DateUtils.startOfDay(page.getStartTime()));
        page.setEndTime(DateUtils.endOfDay(page.getEndTime()));
        List<CCMonitorPage> ccMonitorPages = callInfoStatisticsMapper.queryMonitor(page);
        return ccMonitorPages;
    }
}
