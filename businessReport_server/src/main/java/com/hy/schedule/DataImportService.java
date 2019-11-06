package com.hy.schedule;

import com.hy.dao.CallInfoStatisticsDao;
import com.hy.entities.CallInfoStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * .
 * 导入数据Service
 */
@Slf4j
@Service
public class DataImportService {

    /**
     * .
     * CallInfoStatisticsDao
     */
    private final CallInfoStatisticsDao callInfoStatisticsDao;

    /**
     * .
     * 构造函数
     *
     * @param dao CallInfoStatisticsDao
     */
    @Autowired
    public DataImportService(final CallInfoStatisticsDao dao) {
        this.callInfoStatisticsDao = dao;
    }

    /**
     * .
     * 批量插入数据
     *
     * @param lsCallInfoStatistics CallInfoStatistics 列表
     */
    @Transactional
    public void insertCallInfoStatistics(
        final List<CallInfoStatistics> lsCallInfoStatistics) {
        int size = callInfoStatisticsDao
            .insertCallInfoStatisticsList(lsCallInfoStatistics);
        log.info("success import callInfoStatistics【" + size + "】");
    }
}
