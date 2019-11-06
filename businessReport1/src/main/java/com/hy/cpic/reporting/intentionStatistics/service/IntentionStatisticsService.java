package com.hy.cpic.reporting.intentionStatistics.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.cpic.base.utils.DateUtils;
import com.hy.cpic.base.utils.PageUtils;
import com.hy.cpic.mapper.oracle.CallInfoStatisticsMapper;
import com.hy.cpic.reporting.intentionStatistics.page.IntentionStatisticsPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2018/9/8
 * user: lxg
 * package_name: com.hy.cpic.reporting.callbackrate.service
 */
@Service
public class IntentionStatisticsService {
    private final CallInfoStatisticsMapper callInfoStatisticsMapper;

    @Autowired
    public IntentionStatisticsService(CallInfoStatisticsMapper callInfoStatisticsMapper) {
        this.callInfoStatisticsMapper = callInfoStatisticsMapper;
    }

    public Page query(IntentionStatisticsPage intentionStatisticsPage) {
        PageHelper.startPage(intentionStatisticsPage.getCurrent(), intentionStatisticsPage.getPageSize()); //分页利器
        intentionStatisticsPage.setStartTime(DateUtils.startOfDay(intentionStatisticsPage.getStartTime()));
        intentionStatisticsPage.setEndTime(DateUtils.endOfDay(intentionStatisticsPage.getEndTime()));
        List<IntentionStatisticsPage> intentionStatisticsPages = callInfoStatisticsMapper.queryIntentionStatistics(intentionStatisticsPage);
        PageUtils.randomId(intentionStatisticsPages);
        return (Page) intentionStatisticsPages;
    }

    public List<IntentionStatisticsPage> queryAll(IntentionStatisticsPage intentionStatisticsPage) {
        intentionStatisticsPage.setStartTime(DateUtils.startOfDay(intentionStatisticsPage.getStartTime()));
        intentionStatisticsPage.setEndTime(DateUtils.endOfDay(intentionStatisticsPage.getEndTime()));
        return callInfoStatisticsMapper.queryIntentionStatistics(intentionStatisticsPage);
    }
}
