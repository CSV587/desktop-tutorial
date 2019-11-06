package com.hy.iom.mapper.oracle;

import com.hy.iom.entities.CallErrorStatistics;
import com.hy.iom.entities.RecordEndNodeCntInfo;
import com.hy.iom.entities.RecordInfo;
import com.hy.iom.entities.TradeStatistics;
import com.hy.iom.mapper.IomMapper;
import com.hy.iom.quality.averagetime.page.AverageTimePage;
import com.hy.iom.quality.basepage.QualityPage;
import com.hy.iom.quality.connectionrate.page.ConnectionRatePage;
import com.hy.iom.reporting.page.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RecordInfoMapper extends IomMapper<RecordInfo> {

    List<RecordInfo> getRecordInfoByUUID(String uuid);

    List<RecordInfoPage> selectConnectTmp(CallErrorStatistics callErrorStatistics);

    List<RecordInfoPage> selectUnconnectTmp(CallErrorStatistics callErrorStatistics);

    List<RecordInfoPage> selectByErrorTypeTmp(CallErrorStatistics callErrorStatistics);

    List<RecordInfoPage> selectConnect(CallErrorStatistics callErrorStatistics);

    List<RecordInfoPage> selectUnconnect(CallErrorStatistics callErrorStatistics);

    List<RecordInfoPage> selectByErrorType(CallErrorStatistics callErrorStatistics);

    int selectConnectCount(CallErrorStatistics callErrorStatistics);

    int selectUnconnectCount(CallErrorStatistics callErrorStatistics);

    int selectByErrorTypeCount(CallErrorStatistics callErrorStatistics);

    List<RecordEndNodeCntInfo> getRecordEndNodeCntByDate(RecordInfo recordInfo);

    List<TradeInfoPage> selectTradeStatisticsTmp(TradeStatistics tradeStatistics);

    List<TradeInfoPage> selectTradeStatistics(TradeStatistics tradeStatistics);

    int selectTradeStatisticsCount(TradeStatistics tradeStatistics);

    List<TradeInfoPage> selectTimeSectionTradeStatisticsTmp(TradeStatistics tradeStatistics);

    List<TradeInfoPage> selectTimeSectionTradeStatistics(TradeStatistics tradeStatistics);

    int selectTimeSectionTradeStatisticsCount(TradeStatistics tradeStatistics);

    List<Map<String, Object>> selectPassNodeRateByDayTmp(NodePassRatePage page);

    List<Map<String, Object>> selectPassNodeRateByWeekTmp(NodePassRatePage page);

    List<Map<String, Object>> selectPassNodeRateByMonthTmp(NodePassRatePage page);

    List<Map<String, Object>> selectPassNodeRateByYearTmp(NodePassRatePage page);

    List<Map<String, Object>> selectPassNodeRateByDay(NodePassRatePage page);

    List<Map<String, Object>> selectPassNodeRateByWeek(NodePassRatePage page);

    List<Map<String, Object>> selectPassNodeRateByMonth(NodePassRatePage page);

    List<Map<String, Object>> selectPassNodeRateByYear(NodePassRatePage page);

    int selectPassNodeRateByDayCount(NodePassRatePage page);

    int selectPassNodeRateByWeekCount(NodePassRatePage page);

    int selectPassNodeRateByMonthCount(NodePassRatePage page);

    int selectPassNodeRateByYearCount(NodePassRatePage page);

    List<Map<String, Object>> selectCallContentDetailByUuid(String uuid);

    List<Map<String, Object>> selectHangupNodeByYearTmp(EndNodeNamePage endNodeNamePage);

    List<Map<String, Object>> selectHangupNodeByMonthTmp(EndNodeNamePage endNodeNamePage);

    List<Map<String, Object>> selectHangupNodeByWeekTmp(EndNodeNamePage endNodeNamePage);

    List<Map<String, Object>> selectHangupNodeByDayTmp(EndNodeNamePage endNodeNamePage);

    List<Map<String, Object>> selectHangupNodeByYear(EndNodeNamePage endNodeNamePage);

    List<Map<String, Object>> selectHangupNodeByMonth(EndNodeNamePage endNodeNamePage);

    List<Map<String, Object>> selectHangupNodeByWeek(EndNodeNamePage endNodeNamePage);

    List<Map<String, Object>> selectHangupNodeByDay(EndNodeNamePage endNodeNamePage);

    int selectHangupNodeByDayCount(EndNodeNamePage endNodeNamePage);

    int selectHangupNodeByWeekCount(EndNodeNamePage endNodeNamePage);

    int selectHangupNodeByMonthCount(EndNodeNamePage endNodeNamePage);

    int selectHangupNodeByYearCount(EndNodeNamePage endNodeNamePage);

    List<RecordInfoPage> selectPassNodeRateDetailTmp(NodePassRatePage nodePassRatePage);

    List<RecordInfoPage> selectPassNodeRateDetail(NodePassRatePage nodePassRatePage);

    int selectPassNodeRateDetailCount(NodePassRatePage nodePassRatePage);

    List<RecordInfoPage> selectHangupNodeDetailTmp(EndNodeNamePage endNodeNamePage);

    List<RecordInfoPage> selectHangupNodeDetail(EndNodeNamePage endNodeNamePage);

    int selectHangupNodeDetailCount(EndNodeNamePage endNodeNamePage);

    List<Map<String, Object>> selectSceneThroughDeatilTmp(SceneThroughPage sceneThroughPage);

    List<Map<String, Object>> selectSceneThroughDeatil(SceneThroughPage sceneThroughPage);

    int selectSceneThroughDeatilCount(SceneThroughPage sceneThroughPage);

    List<Map<String, Object>> selectConnectionRateByYearTmp(ConnectRate connectRate);

    List<Map<String, Object>> selectConnectionRateByMonthTmp(ConnectRate connectRate);

    List<Map<String, Object>> selectConnectionRateByWeekTmp(ConnectRate connectRate);

    List<Map<String, Object>> selectConnectionRateByDayTmp(ConnectRate connectRate);

    List<Map<String, Object>> selectConnectionRateByYear(ConnectRate connectRate);

    List<Map<String, Object>> selectConnectionRateByMonth(ConnectRate connectRate);

    List<Map<String, Object>> selectConnectionRateByWeek(ConnectRate connectRate);

    List<Map<String, Object>> selectConnectionRateByDay(ConnectRate connectRate);

    int selectConnectionRateByDayCount(ConnectRate connectRate);

    int selectConnectionRateByWeekCount(ConnectRate connectRate);

    int selectConnectionRateByMonthCount(ConnectRate connectRate);

    int selectConnectionRateByYearCount(ConnectRate connectRate);

    List<RecordInfoPage> selectConnectionRateDetailTmp(ConnectRate connectRate);

    List<RecordInfoPage> selectConnectionRateDetail(ConnectRate connectRate);

    int selectConnectionRateDetailCount(ConnectRate connectRate);

    List<Map<String, Object>> selectHangupRateByYearTmp(EndNodeNamePage endNodeNamePage);

    List<Map<String, Object>> selectHangupRateByYear(EndNodeNamePage endNodeNamePage);

    int selectHangupRateByYearCount(EndNodeNamePage endNodeNamePage);

    List<Map<String, Object>> selectHangupRateByMonthTmp(EndNodeNamePage endNodeNamePage);

    List<Map<String, Object>> selectHangupRateByMonth(EndNodeNamePage endNodeNamePage);

    int selectHangupRateByMonthCount(EndNodeNamePage endNodeNamePage);

    List<Map<String, Object>> selectHangupRateByWeekTmp(EndNodeNamePage endNodeNamePage);

    List<Map<String, Object>> selectHangupRateByWeek(EndNodeNamePage endNodeNamePage);

    int selectHangupRateByWeekCount(EndNodeNamePage endNodeNamePage);

    List<Map<String, Object>> selectHangupRateByDayTmp(EndNodeNamePage endNodeNamePage);

    List<Map<String, Object>> selectHangupRateByDay(EndNodeNamePage endNodeNamePage);

    int selectHangupRateByDayCount(EndNodeNamePage endNodeNamePage);

    List<RecordInfoPage> selectHangupRateDetailTmp(EndNodeNamePage endNodeNamePage);

    List<RecordInfoPage> selectHangupRateDetail(EndNodeNamePage endNodeNamePage);

    int selectHangupRateDetailCount(EndNodeNamePage endNodeNamePage);

    List<AverageTimePage> queryAverageTime(AverageTimePage page);

    int addAverageTime(AverageTimePage page);

    int editQuality(QualityPage page);

    int delQuality(QualityPage page);

    List<ConnectionRatePage> queryConnectionRate(ConnectionRatePage page);

    int addConnectionRate(ConnectionRatePage page);

    List<Map<String, Object>> selectByConditionTmp(@Param("rec") RecordInfoPage2 recordInfoPage);

    List<Map<String, Object>> selectByCondition(@Param("rec") RecordInfoPage2 recordInfoPage);

    int selectByConditionCount(@Param("rec") RecordInfoPage2 recordInfoPage);

    List<Map<String, Object>> selectCallContentDetailByConditionTmp(@Param("rec") RecordInfoPage2 recordInfoPage);

    List<Map<String, Object>> selectCallContentDetailByCondition(@Param("rec") RecordInfoPage2 recordInfoPage);

    int selectCallContentCountByCondition(@Param("rec") RecordInfoPage2 recordInfoPage);

    List<Map<String, Object>> countByCondition(@Param("rec") RecordInfoPage2 recordInfoPage);

}
