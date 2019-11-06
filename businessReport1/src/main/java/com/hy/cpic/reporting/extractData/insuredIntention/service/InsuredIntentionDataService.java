package com.hy.cpic.reporting.extractData.insuredIntention.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.cpic.base.utils.DateUtils;
import com.hy.cpic.base.utils.PageUtils;
import com.hy.cpic.mapper.oracle.InsuredIntentionDataMapper;
import com.hy.cpic.reporting.extractData.insuredIntention.page.InsuredIntentionDataPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsuredIntentionDataService {

    private final InsuredIntentionDataMapper insuredIntentionDataMapper;

    public InsuredIntentionDataService(InsuredIntentionDataMapper insuredIntentionDataMapper) {
        this.insuredIntentionDataMapper = insuredIntentionDataMapper;
    }

    public Page query(InsuredIntentionDataPage insuredIntentionDataPage) {
        PageHelper.startPage(insuredIntentionDataPage.getCurrent(), insuredIntentionDataPage.getPageSize());
        insuredIntentionDataPage.setStartTime(DateUtils.startOfDay(insuredIntentionDataPage.getStartTime()));
        insuredIntentionDataPage.setEndTime(DateUtils.endOfDay(insuredIntentionDataPage.getEndTime()));
        if (!StringUtils.isEmpty(insuredIntentionDataPage.getActivityName()))
            insuredIntentionDataPage.setActivityName(insuredIntentionDataPage.getActivityName().trim());
        if (!StringUtils.isEmpty(insuredIntentionDataPage.getCompany()))
            insuredIntentionDataPage.setCompany(insuredIntentionDataPage.getCompany().trim());
        List<InsuredIntentionDataPage> insuredIntentionDataPageList = insuredIntentionDataMapper.queryInsuredIntentionData(insuredIntentionDataPage);
        insuredIntentionDataPageList.forEach(data -> {
            int intentionCount = Integer.parseInt(data.getHighLevelCount())
                + Integer.parseInt(data.getMiddleLevelCount())
                + Integer.parseInt(data.getLowLevelCount());
            data.setIntentionCount(Integer.toString(intentionCount));
            if (StringUtils.isEmpty(data.getUnconnectedCount())) {
                data.setUnconnectedCount("0");
            }
        });
        PageUtils.randomId(insuredIntentionDataPageList);
        return (Page) insuredIntentionDataPageList;
    }

    public List<InsuredIntentionDataPage> queryAll(InsuredIntentionDataPage insuredIntentionDataPage) {
        insuredIntentionDataPage.setStartTime(DateUtils.startOfDay(insuredIntentionDataPage.getStartTime()));
        insuredIntentionDataPage.setEndTime(DateUtils.endOfDay(insuredIntentionDataPage.getEndTime()));
        if (!StringUtils.isEmpty(insuredIntentionDataPage.getActivityName()))
            insuredIntentionDataPage.setActivityName(insuredIntentionDataPage.getActivityName().trim());
        if (!StringUtils.isEmpty(insuredIntentionDataPage.getCompany()))
            insuredIntentionDataPage.setCompany(insuredIntentionDataPage.getCompany().trim());
        List<InsuredIntentionDataPage> insuredIntentionDataPageList = insuredIntentionDataMapper.queryInsuredIntentionData(insuredIntentionDataPage);
        insuredIntentionDataPageList.forEach(data -> {
            int intentionCount = Integer.parseInt(data.getHighLevelCount())
                + Integer.parseInt(data.getMiddleLevelCount())
                + Integer.parseInt(data.getLowLevelCount());
            data.setIntentionCount(Integer.toString(intentionCount));
            if (StringUtils.isEmpty(data.getUnconnectedCount())) {
                data.setUnconnectedCount("0");
            }
        });
        return insuredIntentionDataPageList;
    }
}
