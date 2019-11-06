package com.hy.schedule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.entities.CallInfoStatistics;
import com.hy.error.ErrorUtil;
import com.hy.reporting.questionnaire.service.PaperService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * .
 * 导入数据组件
 */
@Slf4j
@Service
@PropertySource(value = "classpath:scheduler.properties", encoding = "UTF-8")
public class DataImportSchedule {

    /**
     * .
     * 1秒
     */
    private static final short ONE_SECOND = 1000;

    /**
     * .
     * DataImportService
     */
    private final DataImportService dataImportService;


    /**
     * .
     * PaperService
     */
    private final PaperService paperService;

    /**
     * .
     * 项目对应Map
     */
    @Value(value = "${projectMaps}")
    private String projectMaps;

    /**
     * .
     * 构造函数
     *
     * @param service  DataImportService
     * @param service1 PaperService
     */
    public DataImportSchedule(final DataImportService service,
                              final PaperService service1) {
        this.dataImportService = service;
        this.paperService = service1;
    }

    /**
     * .
     * kafka监听
     *
     * @param records 获取记录
     */
    @Transactional(rollbackFor = Exception.class)
    @KafkaListener(topics = "${kafka.topic}",
        groupId = "${kafka.groupId}")
    public void processRecordInfoMessage(final List<ConsumerRecord> records) {
        long startTime = System.currentTimeMillis();
        log.debug("获取数据{}条", records.size());
        JSONArray jsonTextArray = new JSONArray();
        for (ConsumerRecord record : records) {
            jsonTextArray.add(record.value());
        }
        List<CallInfoStatistics> callInfoStatistics
            = processData(jsonTextArray);
        try {
            if (!callInfoStatistics.isEmpty()) {
                dataImportService.insertCallInfoStatistics(callInfoStatistics);
            }
        } catch (Exception e) {
            log.error("{}", ErrorUtil.getStackTrace(e));
            log.error("异常入库数据{}", jsonTextArray);
        }
        log.debug("入库数据{}条", callInfoStatistics.size());
        long endTime = System.currentTimeMillis();
        log.debug("入库时间{}", endTime - startTime);
    }

    /**
     * .
     * 处理数据
     *
     * @param jsonTextArray 字符数组
     * @return 处理完成对象
     */
    private List<CallInfoStatistics> processData(
        final JSONArray jsonTextArray) {
        List<CallInfoStatistics> callInfoStatistics = new LinkedList<>();
        for (int j = 0; j < jsonTextArray.size(); j++) {
            String next = jsonTextArray.getString(j);
            if (!StringUtils.isBlank(next)) {
                log.debug(next);
                CallInfoStatistics callInfoStatistics1
                    = parseDataByProject(next);
                if (callInfoStatistics1 != null) {
                    callInfoStatistics1.setId(UUID.randomUUID().toString());
                    callInfoStatistics.add(callInfoStatistics1);
                }
            }
        }
        return callInfoStatistics;
    }


    /**
     * .
     * 根据项目Id分别调用方法进行处理
     *
     * @param item 原始文本
     * @return 处理结果
     */
    private CallInfoStatistics parseDataByProject(final String item) {
        JSONObject obj = JSONObject.parseObject(item);
        JSONObject callInfo = obj.getJSONObject("callInfo");
        if (callInfo == null) {
            return null;
        }
        String project = callInfo.getString("projectId");
        JSONObject proMaps = JSONObject.parseObject(projectMaps);
        if (proMaps.getString(project) == null) {
            return null;
        }
        CallInfoStatistics callInfoStatistics = null;
        try {
            if ("失效回访".equals(proMaps.getString(project))) {
                callInfoStatistics = parseDataByFailureCallBack(obj);
            } else {
                log.debug("不在范围内，{}", callInfo.getString("metaId"));
            }
        } catch (Exception e) {
            log.error("{}", ErrorUtil.getStackTrace(e));
        }
        return callInfoStatistics;
    }

    /**
     * .
     * 处理建信失效回访数据
     *
     * @param obj 数据对象
     * @return 处理完成对象
     */
    private CallInfoStatistics parseDataByFailureCallBack(
        final JSONObject obj) {
        CallInfoStatistics callInfoStatistics = new CallInfoStatistics();
        initBaseData(callInfoStatistics, obj);
        JSONObject customerInfo = obj.getJSONObject("customerInfo");
        if (customerInfo == null) {
            return null;
        }
        JSONObject callInfo = obj.getJSONObject("callInfo");
        if (callInfo == null) {
            return null;
        }
        String column1 = callInfo.getString("ruleName");
        callInfoStatistics.setColumn1(column1);
        String column2 = callInfo.getString("taskName");
        callInfoStatistics.setColumn2(column2);
        String column3 = customerInfo.getString("回访任务编号");
        callInfoStatistics.setColumn3(column3);
        String column4 = customerInfo.getString("customerId");
        if (column4 != null) {
            callInfoStatistics.setColumn4(column4);
        } else {
            callInfoStatistics.setColumn4(callInfoStatistics.getCallNumber());
        }
        String column5 = customerInfo.getString("保单号");
        callInfoStatistics.setColumn5(column5);
        String column6 = callInfo.getString("projectId");
        callInfoStatistics.setColumn6(column6);
        callInfoStatistics.setColumn7(callInfo.getString("ruleId"));
        callInfoStatistics.setColumn8(callInfo.getString("taskId"));
        String column9 = callInfo.getString("metaId");
        callInfoStatistics.setColumn9(column9);

        String column10 = customerInfo.getString("二级业务类型");
        callInfoStatistics.setColumn10(column10);
        String column18 = customerInfo.getString("一级业务类型");
        callInfoStatistics.setColumn18(column18);

        String column11 = customerInfo.getString("推送任务编号");
        callInfoStatistics.setColumn11(column11);

        String column12 = customerInfo.getString("保单状态");
        callInfoStatistics.setColumn12(column12);

        String column13 = callInfo.getString("callCount");
        callInfoStatistics.setColumn13(column13);

        String column21 = "2";
        JSONArray contents = obj.getJSONArray("content");
        if (contents != null) {
            JSONObject item = contents.getJSONObject(contents.size() - 1);
            if (item.getString("type").equals("hangup")) {
                column21 = "1";
            }
        }

        JSONObject resInfo = obj.getJSONObject("resInfo");
        String column14 = "";
        String column15 = "";
        String column16 = "";
        String column17 = "";

        if (resInfo != null) {
            List<String> answerList = new ArrayList<>();
            for (String key : resInfo.keySet()) {
                if (key.startsWith("问题答案")) {
                    try {
                        Integer.valueOf(key.replace("问题答案", ""));
                    } catch (Exception e) {
                        continue;
                    }
                    answerList.add(key);
                }
            }
            if (!answerList.isEmpty()) {
                StringBuilder buffer = new StringBuilder();
                answerList.sort((str1, str2) -> {
                    int diff = str2.compareTo(str1);
                    if (diff > 0) {
                        return 1;
                    } else if (diff < 0) {
                        return -1;
                    }
                    return 0;
                });
                String key = answerList.get(0);
                String value = resInfo.getString(key);
                if (key.length() > "问题答案".length()) {
                    int index = Integer.parseInt(key.replace("问题答案", ""));
                    for (int i = 0; i < index - 1; i++) {
                        buffer.append("是,");
                    }
                    if (value.equals("是")) {
                        buffer.append("是");
                    } else if (value.equals("否")) {
                        buffer.append("否");
                    }
                    column14 = buffer.toString();
                }
            }
            column15 = resInfo.getString("callResult");
            column16 = resInfo.getString("结案状态");
            column17 = resInfo.getString("推送标签");
        }
        callInfoStatistics.setColumn14(column14);
        callInfoStatistics.setColumn15(column15);
        callInfoStatistics.setColumn16(column16);
        callInfoStatistics.setColumn17(column17);

        callInfoStatistics.setColumn19(callInfo.getString("flowId"));
        callInfoStatistics.setColumn20(callInfo.getString("flowName"));
        callInfoStatistics.setColumn21(column21);
        JSONObject recordInfo = obj.getJSONObject("recordInfo");
        callInfoStatistics.setColumn22(recordInfo.getString("recordPath"));
        //paper是否已发送 待发送为0 已发送为1 找不到对应数据2
        callInfoStatistics.setColumn23("0");
        //录音是否已发送 待发送为0 已发送为1 找不到对应数据2
        callInfoStatistics.setColumn24("0");
        String column25 = customerInfo.getString("唯一标识");
        callInfoStatistics.setColumn25(column25);
        Map<String, String> hashMap = new HashMap<>();
        for (String key : customerInfo.keySet()) {
            hashMap.put(key, customerInfo.getString(key));
        }
        if (resInfo != null) {
            for (String key : resInfo.keySet()) {
                hashMap.put(key, resInfo.getString(key));
            }
        }
        List<String> questionList = paperService.queryChildIdByCondition(
            callInfo.getString("flowId"), hashMap);
        Field f;
        int index = 26;
        try {
            f = callInfoStatistics.getClass().getDeclaredField("column" + index);
            f.setAccessible(true);
            f.set(callInfoStatistics, String.valueOf(questionList.size()));
            for (String qus : questionList) {
                index++;
                f = callInfoStatistics.getClass()
                    .getDeclaredField("column" + index);
                f.setAccessible(true);
                f.set(callInfoStatistics, qus);
            }
        } catch (NoSuchFieldException
            | IllegalAccessException e) {
            log.error("{}", ErrorUtil.getStackTrace(e));
        }

        callInfoStatistics.setProType("失效回访");
        return callInfoStatistics;
    }

    /**
     * .
     * 初始化数据
     *
     * @param data CallInfoStatistics对象
     * @param obj  json对象
     */
    private void initBaseData(final CallInfoStatistics data,
                              final JSONObject obj) {
        JSONObject recordInfo = obj.getJSONObject("recordInfo");
        data.setUuid(recordInfo.getString("uuid"));
        data.setCallNumber(recordInfo.getString("callnumber"));

        try {
            data.setChannelStartTime(
                Timestamp.valueOf(recordInfo.getString("channelStartTime")));
            data.setChannelEndTime(
                Timestamp.valueOf(recordInfo.getString("channelEndTime")));
            if (recordInfo.getString("recordStartTime") == null) {
                data.setRecordStartTime(
                    Timestamp.valueOf(recordInfo.getString("channelStartTime")));
            } else {
                data.setRecordStartTime(
                    Timestamp.valueOf(recordInfo.getString("recordStartTime")));
            }
            if (recordInfo.getString("recordEndTime") == null) {
                data.setRecordEndTime(
                    Timestamp.valueOf(recordInfo.getString("channelEndTime")));
            } else {
                data.setRecordEndTime(
                    Timestamp.valueOf(recordInfo.getString("recordEndTime")));
            }
            if (!recordInfo.getString("onState").equals("connect")) {
                data.setRecordStartTime(data.getRecordEndTime());
            }
            //通话时长
            data.setDuration(
                (data.getRecordEndTime().getTime()
                    - data.getRecordStartTime().getTime())
                    / ONE_SECOND);
        } catch (Exception e) {
            log.error("{}", ErrorUtil.getStackTrace(e));
        }
        data.setIsConnect(recordInfo.getString("onState"));
    }
}
