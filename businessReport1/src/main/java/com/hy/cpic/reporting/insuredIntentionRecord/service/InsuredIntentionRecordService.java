package com.hy.cpic.reporting.insuredIntentionRecord.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.cpic.base.utils.PageUtils;
import com.hy.cpic.mapper.oracle.InsuredIntentionDataMapper;
import com.hy.cpic.reporting.insuredIntentionRecord.page.InsuredIntentionRecordPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InsuredIntentionRecordService {

    private final InsuredIntentionDataMapper insuredIntentionDataMapper;

    public InsuredIntentionRecordService(InsuredIntentionDataMapper insuredIntentionDataMapper) {
        this.insuredIntentionDataMapper = insuredIntentionDataMapper;
    }

    public Page query(InsuredIntentionRecordPage insuredIntentionRecordPage) {
        PageHelper.startPage(insuredIntentionRecordPage.getCurrent(), insuredIntentionRecordPage.getPageSize());
        if (!StringUtils.isEmpty(insuredIntentionRecordPage.getCompany()))
            insuredIntentionRecordPage.setCompany(insuredIntentionRecordPage.getCompany().trim());
        List<InsuredIntentionRecordPage> insuredIntentionRecordPageList = insuredIntentionDataMapper.queryInsuredIntentionRecord(insuredIntentionRecordPage);
        insuredIntentionRecordPageList.forEach(data -> {
            data.setCalledCount(data.getConnectOnceCount() + data.getConnectTwiceCount() + data.getUnconnectCount());
            data.setCalledTimes(data.getConnectTimes() + data.getUnconnectTimes());
            data.setCalledLength(data.getConnectLength() + data.getUnconnectRingLength());
            data.setConnectChatLength(data.getConnectLength() - data.getConnectRingLength());
            data.setConnectAvgLength(formatInteger(data.getConnectLength(), data.getConnectTimes()));
            data.setConnectChatAvgLength(formatInteger(data.getConnectChatLength(), data.getConnectTimes()));
            data.setConnectAvgTimes(formatIntegerRate(data.getConnectTimes(), data.getCalledTimes()));
            data.setUnconnectRingAvgLength(formatInteger(data.getUnconnectRingLength(), data.getConnectTimes()));
        });
        PageUtils.randomId(insuredIntentionRecordPageList);
        return (Page) insuredIntentionRecordPageList;
    }

    public List<InsuredIntentionRecordPage> queryAll(InsuredIntentionRecordPage insuredIntentionRecordPage) {
        PageHelper.startPage(insuredIntentionRecordPage.getCurrent(), insuredIntentionRecordPage.getPageSize());
        if (!StringUtils.isEmpty(insuredIntentionRecordPage.getCompany()))
            insuredIntentionRecordPage.setCompany(insuredIntentionRecordPage.getCompany().trim());
        List<InsuredIntentionRecordPage> insuredIntentionRecordPageList = insuredIntentionDataMapper.queryInsuredIntentionRecord(insuredIntentionRecordPage);
        insuredIntentionRecordPageList.forEach(data -> {
            data.setCalledCount(data.getConnectOnceCount() + data.getConnectTwiceCount() + data.getUnconnectCount());
            data.setCalledTimes(data.getConnectTimes() + data.getUnconnectTimes());
            data.setCalledLength(data.getConnectLength() + data.getUnconnectRingLength());
            data.setConnectChatLength(data.getConnectLength() - data.getConnectRingLength());
            data.setConnectAvgLength(formatInteger(data.getConnectLength(), data.getConnectTimes()));
            data.setConnectChatAvgLength(formatInteger(data.getConnectChatLength(), data.getConnectTimes()));
            data.setConnectAvgTimes(formatIntegerRate(data.getConnectTimes(), data.getCalledTimes()));
            data.setUnconnectRingAvgLength(formatInteger(data.getUnconnectRingLength(), data.getConnectTimes()));
        });
        return insuredIntentionRecordPageList;
    }

    private String formatIntegerRate(Integer single, Integer total) {
        double rate = 0 == total ? (double) 0 : (double) single / total * 100;
        BigDecimal big = BigDecimal.valueOf(rate);
        return big.setScale(0, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() + "%";
    }

    private Integer formatInteger(Integer single, Integer total) {
        return 0 == total ? 0 : single / total;
    }
}
