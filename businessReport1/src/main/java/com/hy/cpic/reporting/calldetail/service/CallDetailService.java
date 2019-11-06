package com.hy.cpic.reporting.calldetail.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.cpic.base.utils.DateUtils;
import com.hy.cpic.base.utils.PageUtils;
import com.hy.cpic.mapper.oracle.CallInfoStatisticsMapper;
import com.hy.cpic.reporting.calldetail.page.CallDetailPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 17:37 2018/8/24
 * @ Description ：呼叫详情服务类
 * @ Modified By ：
 * @ Version     ：1.0
 */
@Service
public class CallDetailService {

    private final CallInfoStatisticsMapper callInfoStatisticsMapper;

    @Autowired
    public CallDetailService(CallInfoStatisticsMapper callInfoStatisticsMapper) {
        this.callInfoStatisticsMapper = callInfoStatisticsMapper;
    }

    public Page query(CallDetailPage callDetailPage) {
        PageHelper.startPage(callDetailPage.getCurrent(), callDetailPage.getPageSize()); //分页利器
        callDetailPage.setStartTime(DateUtils.startOfDay(callDetailPage.getStartTime()));
        callDetailPage.setEndTime(DateUtils.endOfDay(callDetailPage.getEndTime()));
        List<CallDetailPage> callDetailPages = callInfoStatisticsMapper.query(callDetailPage);
        PageUtils.randomId(callDetailPages);
        return (Page) callDetailPages;
    }

    public List<CallDetailPage> queryAll(CallDetailPage callDetailPage) {
        callDetailPage.setStartTime(DateUtils.startOfDay(callDetailPage.getStartTime()));
        callDetailPage.setEndTime(DateUtils.endOfDay(callDetailPage.getEndTime()));
        return callInfoStatisticsMapper.query(callDetailPage);
    }
}
