package com.hy.iom.mapper.oracle;

import com.hy.iom.entities.CallCntResult;
import com.hy.iom.entities.CallErrorStatistics;
import com.hy.iom.entities.CallStatistics;
import com.hy.iom.mapper.IomMapper;
import com.hy.iom.reporting.page.CallStatisticsPage;

import java.util.List;

public interface CallStatisticsMapper extends IomMapper<CallStatistics> {

    void updateCntByPrimaryKey(CallStatistics callStatistics);

    //按日统计接通与未接通数
    List<CallStatistics> selectConnectedAndNotCntByDay(CallStatistics callStatistics);

    //按周统计接通与未接通数
    List<CallStatistics> selectConnectedAndNotCntByWeek(CallStatistics callStatistics);

    //按月统计接通与未接通数
    List<CallStatistics> selectConnectedAndNotCntByMonth(CallStatistics callStatistics);

    //按年统计接通与未接通数
    List<CallStatistics> selectConnectedAndNotCntByYear(CallStatistics callStatistics);

    //按年统计一次外呼接通与未接通数
    List<CallStatistics> selectOnceConnectedAndNotByYear(CallStatistics callStatistics);

    //按年统计多次接通与未接通数
    List<CallStatistics> selectMultiConnectedAndNotByYear(CallStatistics callStatistics);

    //按月统计一次外呼接通与未接通数
    List<CallStatistics> selectOnceConnectedAndNotByMonth(CallStatistics callStatistics);

    //按月统计多次接通与未接通数
    List<CallStatistics> selectMultiConnectedAndNotByMonth(CallStatistics callStatistics);

    //每日呼叫总量统计
    List<CallStatistics> selectAllCallCnt(CallStatistics callStatistics);

    //每日已接通量/未接通量统计
    List<CallStatistics> selectCallCntByState(CallStatistics callStatistics);

    //每日呼叫总量/已接通量/未接通量/接通率/未接通率统计
    List<CallCntResult> selectCallCnt(CallStatistics callStatistics);

    //总数统计
    List<CallErrorStatistics> selectAllStatistics(CallErrorStatistics callErrorStatistics);

    //统计超时差错
    List<CallErrorStatistics> selectSpeechOverTimeStatistics(CallErrorStatistics callErrorStatistics);

    //统计匹配失败差错
    List<CallErrorStatistics> selectMatchErrorStatistics(CallErrorStatistics callErrorStatistics);

    //统计异常中断
    List<CallErrorStatistics> selectErrorStatistics(CallErrorStatistics callErrorStatistics);

    //匹配出错数量
    List<CallErrorStatistics> selectConnectErrorStatistics(CallErrorStatistics callErrorStatistics);

    //超时差错统计
    List<CallErrorStatistics> selectSpeechOverTimeLine(CallErrorStatistics callErrorStatistics);

    //匹配失败统计
    List<CallErrorStatistics> selectMatchErrorLine(CallErrorStatistics callErrorStatistics);

    //异常中断统计
    List<CallErrorStatistics> selectDisconnectErrorLine(CallErrorStatistics callErrorStatistics);

    //统计所有通话数
    List<CallErrorStatistics> selectAllLine(CallErrorStatistics callErrorStatistics);

    //统计所有出错数
    List<CallErrorStatistics> selectErrorLine(CallErrorStatistics callErrorStatistics);

    //统计所有无异常接通数
    List<CallErrorStatistics> selectConnectLine(CallErrorStatistics callErrorStatistics);

    //统计所有未接通数
    List<CallErrorStatistics> selectunconnectLine(CallErrorStatistics callErrorStatistics);

    List<CallStatistics> getConnectedAndNotCntByYear(CallStatisticsPage page);

    List<CallStatistics> getConnectedAndNotCntByMonth(CallStatisticsPage page);

    List<CallStatistics> getConnectedAndNotCntByWeek(CallStatisticsPage page);

    List<CallStatistics> getConnectedAndNotCntByDay(CallStatisticsPage page);
}
