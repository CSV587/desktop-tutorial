package com.hy.iom.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.iom.base.api.IPMApi;
import com.hy.iom.common.page.PageParam;
import com.hy.iom.entities.CallCntResult;
import com.hy.iom.entities.CallErrorStatistics;
import com.hy.iom.entities.CallStatistics;
import com.hy.iom.entities.DailyCount;
import com.hy.iom.entities.RecordInfo;
import com.hy.iom.mapper.oracle.CallStatisticsMapper;
import com.hy.iom.reporting.page.CallStatisticsPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Service
public class CallStatisticsService {

    private final CallStatisticsMapper callStatisticsMapper;
    private final IPMApi icmApi;

    public CallStatisticsService(CallStatisticsMapper callStatisticsMapper, IPMApi icmApi) {
        this.callStatisticsMapper = callStatisticsMapper;
        this.icmApi = icmApi;
    }

    public void updateByRecordInfos(List<RecordInfo> recordInfoList) {
        long startTime = System.currentTimeMillis();
        Map<CallStatistics, Integer> statisticsMap = countRecordInfo(recordInfoList);

        for (CallStatistics statistics : statisticsMap.keySet()) {
            //查询statistics是否存在
            List<CallStatistics> callStatistics = callStatisticsMapper.select(statistics);
            if ((callStatistics != null) && (callStatistics.size() > 0)) { //存在则更新
                CallStatistics callStatistics1 = callStatistics.get(0);
                callStatistics1.setCnt(statisticsMap.get(statistics));
                callStatisticsMapper.updateCntByPrimaryKey(callStatistics1);
            } else { //不存在则插入
                statistics.setCnt(statisticsMap.get(statistics));
                callStatisticsMapper.insert(statistics);
            }
        }
        long endTime = System.currentTimeMillis();
        log.debug("更新统计数据时间为{}", endTime - startTime);
    }

    public List<CallStatistics> getConnectedAndNotCntByDay(CallStatistics callStatistics) {
        return callStatisticsMapper.selectConnectedAndNotCntByDay(callStatistics);
    }

    public List<CallStatistics> getConnectedAndNotCntByWeek(CallStatistics callStatistics) {
        return callStatisticsMapper.selectConnectedAndNotCntByWeek(callStatistics);
    }

    public List<CallStatistics> getConnectedAndNotCntByMonth(CallStatistics callStatistics) {
        return callStatisticsMapper.selectConnectedAndNotCntByMonth(callStatistics);
    }

    public List<CallStatistics> getConnectedAndNotCntByYear(CallStatistics callStatistics) {
        return callStatisticsMapper.selectConnectedAndNotCntByYear(callStatistics);
    }

    public List<CallStatistics> getOnceConnectedAndNotByMonth(CallStatistics callStatistics) {
        return callStatisticsMapper.selectOnceConnectedAndNotByMonth(callStatistics);
    }

    public List<CallStatistics> getMultiConnectedAndNotByMonth(CallStatistics callStatistics) {
        return callStatisticsMapper.selectMultiConnectedAndNotByMonth(callStatistics);
    }

    public List<CallStatistics> getOnceConnectedAndNotByYear(CallStatistics callStatistics) {
        return callStatisticsMapper.selectOnceConnectedAndNotByYear(callStatistics);
    }

    public List<CallStatistics> getMultiConnectedAndNotByYear(CallStatistics callStatistics) {
        return callStatisticsMapper.selectMultiConnectedAndNotByYear(callStatistics);
    }

    public List<CallStatistics> getAllCallCnt(CallStatistics callStatistics) {
        return callStatisticsMapper.selectAllCallCnt(callStatistics);
    }

    public List<CallStatistics> getCallCntByState(CallStatistics callStatistics) {
        return callStatisticsMapper.selectCallCntByState(callStatistics);
    }

    public Map<String, LinkedList<DailyCount>> formatDailyCount(List<CallStatistics> list) {
        String monthUnit = "月";
        String dayUnit = "日";
        String monthTmp = "";
        Map<String, LinkedList<DailyCount>> linkedListMap = new LinkedHashMap<>();
        LinkedList<DailyCount> dailyCounts = new LinkedList<>();
        ;
        for (CallStatistics cs : list) {
            String date = cs.getDay();
            Integer month = getDateFromStr(date, Calendar.MONDAY) + 1;
            Integer day = getDateFromStr(date, Calendar.DAY_OF_MONTH);
            if (!monthTmp.equals(String.valueOf(month))) {
                dailyCounts = new LinkedList<>();
            }
            dailyCounts.add(new DailyCount(day + dayUnit, cs.getCnt()));
            linkedListMap.put(month + monthUnit, dailyCounts);
            monthTmp = String.valueOf(month);
        }
        return linkedListMap;
    }

    /**
     * 呼叫量统计
     *
     * @param callStatistics
     * @param pageParam
     * @return
     */
    public Page getCallCntByPage(CallStatistics callStatistics, PageParam pageParam) {
        PageHelper.startPage(pageParam.getCurrent(), pageParam.getPageSize());
        List<CallCntResult> results = callStatisticsMapper.selectCallCnt(callStatistics);
        if (results != null && results.size() > 0) {
            for (CallCntResult callCntResult : results) {
                callCntResult.setProjectName(icmApi.getProjectName(callCntResult.getProjectId()));
            }
        }
        return (Page) results;
    }


    private Map<CallStatistics, Integer> countRecordInfo(List<RecordInfo> recordInfoList) {
        Map<CallStatistics, Integer> statisticsMap = new HashMap<>();
        if (null != recordInfoList) {
            for (RecordInfo record : recordInfoList) {
                String channelStartTime = record.getChannelStartTime();
                String year = channelStartTime.substring(0, 4);
                String month = channelStartTime.substring(0, 7);
                String day = channelStartTime.substring(0, 10);
                String week = "";
                try {
                    week = getWeekOfYear(day);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String hour = channelStartTime.substring(11, 13);
                String projectId = record.getProjectId();
                String ruleId = record.getRuleId();
                String taskId = record.getTaskId();
                String flowID = record.getFlowId();
                String onState = record.getOnState();
                Integer callCount = record.getCallCount();
                CallStatistics callStatistics = new CallStatistics(year, month, week, day, hour, projectId, ruleId, taskId, flowID, onState, callCount);
                if (statisticsMap.containsKey(callStatistics)) {
                    statisticsMap.put(callStatistics, statisticsMap.get(callStatistics) + 1);
                } else {
                    statisticsMap.put(callStatistics, 1);
                }
            }
        }
        return statisticsMap;
    }

    private String getWeekOfYear(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        date = format.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(4);
        calendar.setTime(date);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("-").append(calendar.get(Calendar.WEEK_OF_YEAR)).append("周");
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        calendar.add(Calendar.DATE, -day_of_week + 4);
        stringBuilder.insert(0, calendar.get(Calendar.YEAR));
        return stringBuilder.toString();
    }

    private Integer getDateFromStr(String date, int field) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar instance = Calendar.getInstance();
        try {
            instance.setTime(sdf.parse(date));
            return instance.get(field);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, Object> setValueMap(Object... objects) {
        Map valueMap = new HashMap<>();
        for (int i = 0; i < objects.length - 1; i += 2) {
            String key = (String) objects[i];
            Object value = objects[i + 1];
            valueMap.put(key, value);
        }
        return valueMap;
    }


    public void getCallErrorStatistics(Map<String, LinkedList<Map<String, Object>>> map, CallErrorStatistics callErrorStatistics) {
        LinkedList<Map<String, Object>> detail = new LinkedList<>();
        LinkedList<Map<String, Object>> sum = new LinkedList<>();

        List<CallErrorStatistics> transErrorStatistics = callStatisticsMapper.selectSpeechOverTimeStatistics(callErrorStatistics);
        long transError = (transErrorStatistics != null && transErrorStatistics.size() > 0) ? transErrorStatistics.get(0).getValue() : 0;
        if (transError != 0) detail.add(setValueMap("name", "识别超时", "type", "speechOverTime", "value", transError));

        List<CallErrorStatistics> matchErrorStatistics = callStatisticsMapper.selectMatchErrorStatistics(callErrorStatistics);
        long matchError = (matchErrorStatistics != null && matchErrorStatistics.size() > 0) ? matchErrorStatistics.get(0).getValue() : 0;
        if (matchError != 0) detail.add(setValueMap("name", "匹配失败", "type", "matchError", "value", matchError));

        List<CallErrorStatistics> disconnectErrorStatistics = callStatisticsMapper.selectErrorStatistics(callErrorStatistics);
        long error = (disconnectErrorStatistics != null && disconnectErrorStatistics.size() > 0) ? disconnectErrorStatistics.get(0).getValue() : 0;
        if (error != 0) detail.add(setValueMap("name", "异常中断", "type", "disconnectError", "value", error));


        List<CallErrorStatistics> allStatistics = callStatisticsMapper.selectAllStatistics(callErrorStatistics);
        long allCount = allStatistics.get(0).getValue();

        List<CallErrorStatistics> connectErrorStatistics = callStatisticsMapper.selectConnectErrorStatistics(callErrorStatistics);
        long connectErrorCount = connectErrorStatistics.get(0).getValue();

        long connectSuccessCount = allCount - connectErrorCount;
        if (connectSuccessCount != 0)
            detail.add(setValueMap("name", "通话成功", "type", "connectSuccess", "value", connectSuccessCount));

        map.put("detail", detail);


        if (connectErrorCount != 0) sum.add(setValueMap("name", "通话差错", "value", connectErrorCount));
        if (connectSuccessCount != 0) sum.add(setValueMap("name", "通话成功", "value", connectSuccessCount));

        map.put("sum", sum);
    }

    public void getCallErrorLineStatistics(Map<String, LinkedList<Map<String, Object>>> map, CallErrorStatistics callErrorStatistics) {
        LinkedList<String> timeLine;
        LinkedList<Map<String, Object>> times = new LinkedList<>();
        LinkedList<Map<String, Object>> detail = new LinkedList<>();
        Set<String> timeSet = new TreeSet<>();

        List<CallErrorStatistics> speechOverTimeStatistics = callStatisticsMapper.selectSpeechOverTimeLine(callErrorStatistics);
        List<CallErrorStatistics> matchErrorStatistics = callStatisticsMapper.selectMatchErrorLine(callErrorStatistics);
        List<CallErrorStatistics> disconnectErrorStatistics = callStatisticsMapper.selectDisconnectErrorLine(callErrorStatistics);
        List<CallErrorStatistics> allStatistics = callStatisticsMapper.selectAllLine(callErrorStatistics);
        List<CallErrorStatistics> connectCountStatistics = callStatisticsMapper.selectConnectLine(callErrorStatistics);
        List<CallErrorStatistics> unconnectCountStatistics = callStatisticsMapper.selectunconnectLine(callErrorStatistics);

        addRecordTimeToSet(allStatistics, timeSet);

        timeLine = new LinkedList<>(timeSet);

        if (timeLine != null && timeLine.size() > 0) {
            Map<String, Object>[][] array = new HashMap[5][timeLine.size()];
            for (int i = 0; i < timeLine.size(); i++) {
                String time = timeLine.get(i);
                long speechOverTimeCount = getStatisticsCount(speechOverTimeStatistics, time);
                long matchErrorCount = getStatisticsCount(matchErrorStatistics, time);
                long disconnectErrorCount = getStatisticsCount(disconnectErrorStatistics, time);
                long allCount = getStatisticsCount(allStatistics, time);
                long connectCount = getStatisticsCount(connectCountStatistics, time);
                long unconnectCount = getStatisticsCount(unconnectCountStatistics, time);

                array[0][i] = setValueMap("value", String.format("%.2f", 1.0 * speechOverTimeCount / allCount), "projectId", callErrorStatistics.getProjectId(), "ruleId", callErrorStatistics.getRuleId(), "count", speechOverTimeCount, "type", "speechOverTime");//识别超时
                array[1][i] = setValueMap("value", String.format("%.2f", 1.0 * disconnectErrorCount / allCount), "projectId", callErrorStatistics.getProjectId(), "ruleId", callErrorStatistics.getRuleId(), "count", disconnectErrorCount, "type", "error");//异常中断
                array[2][i] = setValueMap("value", String.format("%.2f", 1.0 * matchErrorCount / allCount), "projectId", callErrorStatistics.getProjectId(), "ruleId", callErrorStatistics.getRuleId(), "count", matchErrorCount, "type", "matchError");//匹配失败
                array[3][i] = setValueMap("value", String.format("%.2f", 1.0 * connectCount / allCount), "projectId", callErrorStatistics.getProjectId(), "ruleId", callErrorStatistics.getRuleId(), "count", connectCount, "type", "connectSuccess");//通话成功
                array[4][i] = setValueMap("value", String.format("%.2f", 1.0 * unconnectCount / allCount), "projectId", callErrorStatistics.getProjectId(), "ruleId", callErrorStatistics.getRuleId(), "count", unconnectCount, "type", "unconnect");//未接通

            }
            Map<String, Object>[] speechOverTime = array[0];
            Map<String, Object>[] disconnectError = array[1];
            Map<String, Object>[] matchError = array[2];
            Map<String, Object>[] successCount = array[3];
            Map<String, Object>[] unconnectCount = array[4];

            detail.add(setValueMap("name", "识别超时", "type", "bar", "errorType", "speechOverTime", "data", speechOverTime));
            detail.add(setValueMap("name", "匹配失败", "type", "bar", "errorType", "matchError", "data", matchError));
            detail.add(setValueMap("name", "异常中断", "type", "bar", "errorType", "error", "data", disconnectError));
            detail.add(setValueMap("name", "通话成功", "type", "bar", "errorType", "connectSuccess", "data", successCount));
            detail.add(setValueMap("name", "未接通", "type", "bar", "errorType", "unconnect", "data", unconnectCount));

        }

        times.add(setValueMap("format", "YYYY-MM-DD", "value", timeLine));

        map.put("timeLine", times);
        map.put("detail", detail);

    }

    private long getStatisticsCount(List<CallErrorStatistics> list, String time) {
        if (list != null && list.size() > 0) {
            for (CallErrorStatistics statistics : list) {
                if (statistics.getRecordTime().equals(time)) {
                    return statistics.getValue();
                }
            }
        }
        return 0L;
    }


    private void addRecordTimeToSet(List<CallErrorStatistics> list, Set<String> timeSet) {
        for (CallErrorStatistics callErrorStatistics : list) {
            timeSet.add(callErrorStatistics.getRecordTime());
        }
    }


    public List<CallCntResult> getCallCnt(CallStatistics callStatistics) {
        List<CallCntResult> results = callStatisticsMapper.selectCallCnt(callStatistics);
        if (results != null && results.size() > 0) {
            for (CallCntResult callCntResult : results) {
                callCntResult.setProjectName(icmApi.getProjectName(callCntResult.getProjectId()));
                if (callCntResult.getConnectCount() == null) {
                    callCntResult.setConnectCount(0);
                }
                if (callCntResult.getUnconnectCount() == null) {
                    callCntResult.setUnconnectCount(0);
                }
                if (callCntResult.getSuccessRate() != null) {
                    callCntResult.setSuccessRateStr(String.format("%.1f", callCntResult.getSuccessRate() * 100) + "%");
                } else {
                    callCntResult.setSuccessRateStr("0.0%");
                }
                if (callCntResult.getFailRate() != null) {
                    callCntResult.setFailRateStr(String.format("%.1f", callCntResult.getFailRate() * 100) + "%");
                } else {
                    callCntResult.setFailRateStr("0.0%");
                }
            }
        }
        return results;
    }

    public List<CallStatistics> getConnectedAndNotCntByYear(CallStatisticsPage page) {
        return callStatisticsMapper.getConnectedAndNotCntByYear(page);
    }

    public List<CallStatistics> getConnectedAndNotCntByMonth(CallStatisticsPage page) {
        return callStatisticsMapper.getConnectedAndNotCntByMonth(page);
    }

    public List<CallStatistics> getConnectedAndNotCntByWeek(CallStatisticsPage page) {
        return callStatisticsMapper.getConnectedAndNotCntByWeek(page);
    }

    public List<CallStatistics> getConnectedAndNotCntByDay(CallStatisticsPage page) {
        return callStatisticsMapper.getConnectedAndNotCntByDay(page);
    }
}
