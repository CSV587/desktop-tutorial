package com.hy.cpic.dao;

import com.hy.cpic.entities.CallInfoStatistics;
import com.hy.cpic.mapper.oracle.CallInfoStatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class CallInfoStatisticsDao {

    private final CallInfoStatisticsMapper callInfoStatisticsMapper;

    @Autowired
    public CallInfoStatisticsDao(CallInfoStatisticsMapper callInfoStatisticsMapper) {
        this.callInfoStatisticsMapper = callInfoStatisticsMapper;
    }

    public int insertCallInfoStatisticsList(List<CallInfoStatistics> ls_callInfoStatistics) throws SQLException {
        for (CallInfoStatistics item : ls_callInfoStatistics) {
            callInfoStatisticsMapper.save(item);
        }
        return ls_callInfoStatistics.size();
    }


    public int updateCallInfo(CallInfoStatistics callInfoStatistic) {
        return callInfoStatisticsMapper.updateCallInfo(callInfoStatistic);
    }

    public int queryCallCount(String callNumber) {
        return callInfoStatisticsMapper.queryCallCount(callNumber) + 1;
    }
}
