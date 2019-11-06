package com.hy.cpic.schedule;

import com.hy.cpic.dao.CallInfoStatisticsDao;
import com.hy.cpic.entities.CallInfoStatistics;
import com.hy.cpic.entities.InsuredIntentionOneMonth;
import com.hy.cpic.mapper.oracle.InsuredIntentionOneMonthMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
public class DataImportService {

    private static Logger log = LoggerFactory.getLogger(DataImportService.class);

    private final CallInfoStatisticsDao callInfoStatisticsDao;

    private final InsuredIntentionOneMonthMapper insuredIntentionOneMonthMapper;

    @Autowired
    public DataImportService(CallInfoStatisticsDao callInfoStatisticsDao, InsuredIntentionOneMonthMapper insuredIntentionOneMonthMapper) {
        this.callInfoStatisticsDao = callInfoStatisticsDao;
        this.insuredIntentionOneMonthMapper = insuredIntentionOneMonthMapper;
    }

    @Transactional
    public void insertCallInfoStatistics(List<CallInfoStatistics> ls_callInfoStatistics) throws SQLException {
        int size = callInfoStatisticsDao.insertCallInfoStatisticsList(ls_callInfoStatistics);
        log.info("success import callInfoStatistics【" + size + "】");
    }

    @Transactional
    public void updateCallInfo(CallInfoStatistics callInfoStatistic) {
        int size = callInfoStatisticsDao.updateCallInfo(callInfoStatistic);
        log.info("update record {}", size);
    }


    int queryCallCount(String callNumber) {
        if (callNumber == null) {
            return 0;
        }
        return callInfoStatisticsDao.queryCallCount(callNumber);
    }

    @Transactional
    public int updateTimes(InsuredIntentionOneMonth insuredIntentionOneMonth) {
        int size = insuredIntentionOneMonthMapper.updateTimes(insuredIntentionOneMonth);
        log.info("update tbyx record {}", size);
        return size;
    }

    @Transactional
    public void saveInsuredIntention(InsuredIntentionOneMonth insuredIntentionOneMonth) {
        insuredIntentionOneMonthMapper.save(insuredIntentionOneMonth);
    }

}
