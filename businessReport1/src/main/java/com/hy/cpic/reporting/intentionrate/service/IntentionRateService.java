package com.hy.cpic.reporting.intentionrate.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.cpic.base.utils.DateUtils;
import com.hy.cpic.base.utils.PageUtils;
import com.hy.cpic.mapper.oracle.CallInfoStatisticsMapper;
import com.hy.cpic.reporting.intentionrate.page.IntentionRatePage;
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
public class IntentionRateService {
    private final CallInfoStatisticsMapper callInfoStatisticsMapper;

    @Autowired
    public IntentionRateService(CallInfoStatisticsMapper callInfoStatisticsMapper) {
        this.callInfoStatisticsMapper = callInfoStatisticsMapper;
    }

    public Page query(IntentionRatePage intentionRatePage) {
        PageHelper.startPage(intentionRatePage.getCurrent(), intentionRatePage.getPageSize()); //分页利器
        intentionRatePage.setStartTime(DateUtils.startOfDay(intentionRatePage.getStartTime()));
        intentionRatePage.setEndTime(DateUtils.endOfDay(intentionRatePage.getEndTime()));
        List<IntentionRatePage> intentionRatePages = callInfoStatisticsMapper.queryIntentionRate(intentionRatePage);
        PageUtils.randomId(intentionRatePages);
        return (Page) intentionRatePages;
    }

    public List<IntentionRatePage> queryAll(IntentionRatePage intentionRatePage) {
        intentionRatePage.setStartTime(DateUtils.startOfDay(intentionRatePage.getStartTime()));
        intentionRatePage.setEndTime(DateUtils.endOfDay(intentionRatePage.getEndTime()));
        return callInfoStatisticsMapper.queryIntentionRate(intentionRatePage);
    }
}
