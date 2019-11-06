package com.hy.cpic.reporting.callbackrate.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.cpic.base.utils.DateUtils;
import com.hy.cpic.base.utils.PageUtils;
import com.hy.cpic.mapper.oracle.CallInfoStatisticsMapper;
import com.hy.cpic.reporting.callbackrate.page.CallBackRatePage;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2018/9/8
 * user: lxg
 * package_name: com.hy.cpic.reporting.callbackrate.service
 */
@Service
public class CallBackRateService {
    private final CallInfoStatisticsMapper callInfoStatisticsMapper;

    @Autowired
    public CallBackRateService(CallInfoStatisticsMapper callInfoStatisticsMapper) {
        this.callInfoStatisticsMapper = callInfoStatisticsMapper;
    }

    public Page query(CallBackRatePage callBackRatePage) {
        PageHelper.startPage(callBackRatePage.getCurrent(), callBackRatePage.getPageSize()); //分页利器
        callBackRatePage.setStartTime(DateUtils.startOfDay(callBackRatePage.getDate()));
        callBackRatePage.setEndTime(DateUtils.endOfDay(callBackRatePage.getDate()));
        List<CallBackRatePage> callBackRatePages = callInfoStatisticsMapper.queryCallBackRate(callBackRatePage);
        if (!CollectionUtils.isEmpty(callBackRatePages)) {
            callBackRatePages.forEach(data -> {
                data.setSuccessRate(formatInteger(data.getCallBackTaskNum(), data.getCallBackTaskSuccessNum()));
                data.setFailRate(formatInteger(data.getCallBackTaskNum(), data.getCallBackTaskFailNum()));
                data.setTransFerRate(formatInteger(data.getCallBackTaskNum(), data.getTransFerNum()));
            });
        }
        PageUtils.randomId(callBackRatePages);
        return (Page) callBackRatePages;
    }

    public List<CallBackRatePage> queryAll(CallBackRatePage callBackRatePage) {
        callBackRatePage.setStartTime(DateUtils.startOfDay(callBackRatePage.getDate()));
        callBackRatePage.setEndTime(DateUtils.endOfDay(callBackRatePage.getDate()));
        List<CallBackRatePage> callBackRatePages = callInfoStatisticsMapper.queryCallBackRate(callBackRatePage);
        if (!CollectionUtils.isEmpty(callBackRatePages)) {
            callBackRatePages.forEach(data -> {
                data.setSuccessRate(formatInteger(data.getCallBackTaskNum(), data.getCallBackTaskSuccessNum()));
                data.setFailRate(formatInteger(data.getCallBackTaskNum(), data.getCallBackTaskFailNum()));
                data.setTransFerRate(formatInteger(data.getCallBackTaskNum(), data.getTransFerNum()));
            });
        }
        return callBackRatePages;
    }

    private String formatInteger(String total, String single) {
        double rate = "0".equals(total) ? (double) 0 : (double) Integer.valueOf(single) / Integer.valueOf(total) * 100;
        BigDecimal big = BigDecimal.valueOf(rate);
        return big.setScale(0, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() + "%";
    }
}
