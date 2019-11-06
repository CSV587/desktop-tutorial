package com.hy.dao;

import com.hy.entities.CallInfoStatistics;
import com.hy.mapper.oracle.CallInfoStatisticsMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * .
 * 业务数据操作对象
 */
@Repository
public class CallInfoStatisticsDao {

    /**
     * .
     * callInfoStatisticsMapper
     */
    private final CallInfoStatisticsMapper callInfoStatisticsMapper;

    /**
     * .
     * 构造函数
     *
     * @param mapper callInfoStatisticsMapper
     */
    public CallInfoStatisticsDao(final CallInfoStatisticsMapper mapper) {
        this.callInfoStatisticsMapper = mapper;
    }

    /**
     * .
     * 插入数据
     *
     * @param lsCallInfoStatistics CallInfoStatistics列表
     * @return 返回值
     */
    public int insertCallInfoStatisticsList(
        final List<CallInfoStatistics> lsCallInfoStatistics) {
        callInfoStatisticsMapper.save(lsCallInfoStatistics);
        return lsCallInfoStatistics.size();
    }


}
