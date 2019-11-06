package com.hy.iom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.iom.base.api.ICMApi;
import com.hy.iom.base.api.IPMApi;
import com.hy.iom.common.page.TableHead;
import com.hy.iom.entities.CallErrorStatistics;
import com.hy.iom.entities.RecordEndNodeCntInfo;
import com.hy.iom.entities.RecordInfo;
import com.hy.iom.entities.TradeStatistics;
import com.hy.iom.mapper.oracle.RecordInfoMapper;
import com.hy.iom.mapper.oracle.TagMapper;
import com.hy.iom.reporting.excel.CallContents;
import com.hy.iom.reporting.page.*;
import com.hy.iom.utils.BeanTools;
import com.hy.iom.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RecordInfoService {

    private static Logger log = LoggerFactory.getLogger(RecordInfoService.class);

    private final RecordInfoMapper recordInfoMapper;

    private final TagMapper tagMapper;

    private final IPMApi ipmApi;

    private final ICMApi icmApi;

    @Autowired
    public RecordInfoService(RecordInfoMapper recordInfoMapper, TagMapper tagMapper, IPMApi ipmApi, ICMApi icmApi) {
        this.recordInfoMapper = recordInfoMapper;
        this.tagMapper = tagMapper;
        this.ipmApi = ipmApi;
        this.icmApi = icmApi;
    }

    //因后期需要动态字段 动态条件
    public Page selectPageByCondition(RecordInfoPage recordInfoPage, int current, int pageSize) throws ParseException {
        Page page = new Page();
        RecordInfoPage2 re2 = new RecordInfoPage2(recordInfoPage);
        List<RecordInfoPage> list = new LinkedList<>();
        Page<Map<String, Object>> p = selectPageByConditionTmp(re2, current, pageSize);
        page.setTotal(p.getTotal());
        if(!DateUtils.withinAMonth(re2.getRecordStartTime())){
            Long lSum = Long.valueOf(p.getTotal());
            if(lSum < current * pageSize){
                p = selectPageByCondition(re2, current, pageSize);
            }
            page.setTotal(recordInfoMapper.selectByConditionCount(re2));
        }
        Map<String, String> ruleMap = new HashMap<>();
        Map<String, String> tagMap = new HashMap<>();
        if (p.getResult().size() > 0) {
            for (Map<String, Object> map : p.getResult()) {
                RecordInfoPage recordInfo = BeanTools.map2Bean(map, RecordInfoPage.class);
                handleRuleName_TagName(ruleMap, tagMap, recordInfo);
                list.add(recordInfo);
            }
        }
        page.addAll(list);
        return page;
    }

    private void handleRuleName_TagName(Map<String, String> ruleMap, Map<String, String> tagMap, RecordInfoPage recordInfo) {
        if (StringUtils.isNotBlank(recordInfo.getRuleId())) {
            String ruleName = ruleMap.get(recordInfo.getRuleId());
            if (ruleName == null) {
                ruleName = icmApi.getRuleName(recordInfo.getRuleId(), recordInfo.getProjectId());
                ruleMap.put(recordInfo.getRuleId(), ruleName);
            }
            recordInfo.setRuleName(ruleName);
        }
        //通过tagId来获取来获取tagName
        if (StringUtils.isNotBlank(recordInfo.getTagId())) {
            String tagName = null;
            try {
                String[] tagIds = recordInfo.getTagId().split(",");
                tagName = getTagNames(tagIds, tagMap);
            } catch (Exception e) {
                log.info("获取tagId失败!" + e);
            }
            recordInfo.setTagName(tagName);
        }
    }

    public Page<Map<String, Object>> selectPageByConditionTmp(RecordInfoPage2 re2, int current, int pageSize){
        PageHelper.startPage(current, pageSize);
        return (Page) recordInfoMapper.selectByConditionTmp(re2);
    }

    public Page<Map<String, Object>> selectPageByCondition(RecordInfoPage2 re2, int current, int pageSize){
        PageHelper.startPage(current, pageSize);
        return (Page) recordInfoMapper.selectByCondition(re2);
    }

    //共用方法通过tagId获取tagName
    private String getTagNames(String[] tagIds, Map<String, String> tagMap) {
        StringBuilder tagName = new StringBuilder();
        for (int i = 0; i < tagIds.length; i++) {
            try {
                String name = tagMap.get(tagIds[i]);
                if (name == null) {
                    name = tagMapper.getTagNameById(tagIds[i]);
                    tagMap.put(tagIds[i], name);
                }
                if (i == tagIds.length - 1) {
                    if (name != null) {
                        tagName.append(name);
                    }
                } else {
                    if (name != null) {
                        tagName.append(name).append(",");
                    }
                }
            } catch (Exception e) {
                log.info("SQL-通过id查询标签失败！" + e);
            }
        }
        return tagName.toString();
    }

    public List<RecordInfoPage> selectByCondition(RecordInfoPage recordInfoPage) {
        List<RecordInfoPage> list = new LinkedList<>();
        List<Map<String, Object>> result = recordInfoMapper.selectByCondition(new RecordInfoPage2(recordInfoPage));
        Map<String, String> ruleMap = new HashMap<>();
        Map<String, String> tagMap = new HashMap<>();
        if (result != null && result.size() > 0) {
            for (Map<String, Object> map : result) {
                map.put("callResult", map.get("FINALNODENAME"));
                RecordInfoPage recordInfo = BeanTools.map2Bean(map, RecordInfoPage.class);
                handleRuleName_TagName(ruleMap, tagMap, recordInfo);
                if (StringUtils.isNotBlank(recordInfo.getOnState())) {
                    if ("connect".equals(recordInfo.getOnState())) {
                        recordInfo.setOnState("接通");
                    } else if ("unconnect".equals(recordInfo.getOnState())) {
                        recordInfo.setOnState("未接通");
                    } else if ("blacklist".equals(recordInfo.getOnState())) {
                        recordInfo.setOnState("黑名单");
                    }
                }
                list.add(recordInfo);
            }
        }
        return list;
    }

    public List<RecordInfo> selectByUuid(RecordInfo recordInfo) {
        return recordInfoMapper.getRecordInfoByUUID(recordInfo.getUuid());
    }


    public Page getCallErrorStatisticsDetail(CallErrorStatistics callErrorStatistics, int current, int pageSize) throws ParseException {
        Page p = getCallErrorStatisticsDetailTmp(callErrorStatistics, current, pageSize);
        if(DateUtils.withinAMonth(callErrorStatistics.getRecordStartTime())){
            return p;
        }
        Long lSum = Long.valueOf(p.getTotal());
        if(lSum < current * pageSize) {
            PageHelper.startPage(current, pageSize);
            return getCallErrorStatisticsDetail2(callErrorStatistics);
        }
        p.setTotal(getCallErrorStatisticsDetailCount(callErrorStatistics));
        return p;
    }

    int getCallErrorStatisticsDetailCount(CallErrorStatistics callErrorStatistics){
        int count = 0;
        if ("connectSuccess".equals(callErrorStatistics.getType()))
            count = recordInfoMapper.selectConnectCount(callErrorStatistics);
        else if ("unconnect".equals(callErrorStatistics.getType())) {
            count = recordInfoMapper.selectUnconnectCount(callErrorStatistics);
        } else {
            count = recordInfoMapper.selectByErrorTypeCount(callErrorStatistics);
        }
        return count;
    }

    public Page<RecordInfoPage> getCallErrorStatisticsDetailTmp(CallErrorStatistics callErrorStatistics, int current, int pageSize) {
        Page<RecordInfoPage> recordInfoPage;
        PageHelper.startPage(current, pageSize);
        if ("connectSuccess".equals(callErrorStatistics.getType()))
            recordInfoPage = (Page) recordInfoMapper.selectConnectTmp(callErrorStatistics);
        else if ("unconnect".equals(callErrorStatistics.getType())) {
            recordInfoPage = (Page) recordInfoMapper.selectUnconnectTmp(callErrorStatistics);
        } else {
            recordInfoPage = (Page) recordInfoMapper.selectByErrorTypeTmp(callErrorStatistics);
        }
        if (recordInfoPage != null && recordInfoPage.size() > 0) {
            for (RecordInfoPage page : recordInfoPage) {
                page.setType(callErrorStatistics.getType());
                page.setRuleName(icmApi.getRuleName(page.getRuleId(), page.getProjectId()));
            }
        }
        return recordInfoPage;
    }

    public Page<RecordInfoPage> getCallErrorStatisticsDetail2(CallErrorStatistics callErrorStatistics) {
        Page<RecordInfoPage> recordInfoPage;
        if ("connectSuccess".equals(callErrorStatistics.getType()))
            recordInfoPage = (Page)recordInfoMapper.selectConnect(callErrorStatistics);
        else if ("unconnect".equals(callErrorStatistics.getType())) {
            recordInfoPage = (Page)recordInfoMapper.selectUnconnect(callErrorStatistics);
        } else {
            recordInfoPage = (Page)recordInfoMapper.selectByErrorType(callErrorStatistics);
        }
        if (recordInfoPage != null && recordInfoPage.size() > 0) {
            for (RecordInfoPage page : recordInfoPage) {
                page.setType(callErrorStatistics.getType());
                page.setRuleName(icmApi.getRuleName(page.getRuleId(), page.getProjectId()));
            }
        }
        return recordInfoPage;
    }

    public List<RecordInfoPage> getCallErrorStatisticsDetail(CallErrorStatistics callErrorStatistics) {
        List<RecordInfoPage> recordInfoPage;
        if ("connectSuccess".equals(callErrorStatistics.getType()))
            recordInfoPage = recordInfoMapper.selectConnect(callErrorStatistics);
        else if ("unconnect".equals(callErrorStatistics.getType())) {
            recordInfoPage = recordInfoMapper.selectUnconnect(callErrorStatistics);
        } else {
            recordInfoPage = recordInfoMapper.selectByErrorType(callErrorStatistics);
        }
        if (recordInfoPage != null && recordInfoPage.size() > 0) {
            for (RecordInfoPage page : recordInfoPage) {
                page.setType(callErrorStatistics.getType());
                page.setRuleName(icmApi.getRuleName(page.getRuleId(), page.getProjectId()));
            }
        }
        return recordInfoPage;
    }

    public Page getTradeStatistics(TradeStatistics tradeStatistics, int current, int pageSize) throws ParseException {
        Page p = getTradeStatisticsTmp(tradeStatistics, current, pageSize);
        if(DateUtils.withinAMonth(tradeStatistics.getRecordStartTime())){
            return p;
        }
        Long lSum = Long.valueOf(p.getTotal());
        if(lSum < current * pageSize){
            return getTradeInfoPages(getTradeStatistics2(tradeStatistics, current, pageSize));
        }
        p.setTotal(recordInfoMapper.selectTradeStatisticsCount(tradeStatistics));
        return p;
    }

    public Page<TradeInfoPage> getTradeStatisticsTmp(TradeStatistics tradeStatistics, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        return (Page<TradeInfoPage>) recordInfoMapper.selectTradeStatisticsTmp(tradeStatistics);
    }

    public Page<TradeInfoPage> getTradeStatistics2(TradeStatistics tradeStatistics, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        return (Page<TradeInfoPage>) recordInfoMapper.selectTradeStatistics(tradeStatistics);
    }

    public List<TradeInfoPage> getTradeStatistics(TradeStatistics tradeStatistics) {
        return getTradeInfoPages((Page)recordInfoMapper.selectTradeStatistics(tradeStatistics)).getResult();
    }

    public Page getTimeSectionTradeStatistics(TradeStatistics tradeStatistics, int current, int pageSize) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        tradeStatistics.setRecordStartTime(format.parse(tradeStatistics.getRecordTime()));
        tradeStatistics.setRecordEndTime(format.parse(tradeStatistics.getRecordTime()));

        Page p = getTimeSectionTradeStatisticsTmp(tradeStatistics, current, pageSize);
        if(DateUtils.withinAMonth(tradeStatistics.getRecordStartTime())){
            return getTradeInfoPages(p);
        }
        Long lSum = Long.valueOf(p.getTotal());
        if(lSum < current * pageSize){
            return getTradeInfoPages(getTimeSectionTradeStatistics2(tradeStatistics, current, pageSize));
        }
        p.setTotal(recordInfoMapper.selectTimeSectionTradeStatisticsCount(tradeStatistics));
        return getTradeInfoPages(p);
    }

    public Page<TradeInfoPage> getTimeSectionTradeStatisticsTmp(TradeStatistics tradeStatistics, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        return (Page) recordInfoMapper.selectTimeSectionTradeStatisticsTmp(tradeStatistics);
    }

    public Page<TradeInfoPage> getTimeSectionTradeStatistics2(TradeStatistics tradeStatistics, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        return (Page) recordInfoMapper.selectTimeSectionTradeStatistics(tradeStatistics);
    }

    public List<TradeInfoPage> getTimeSectionTradeStatistics(TradeStatistics tradeStatistics) {
        return getTradeInfoPages((Page)recordInfoMapper.selectTimeSectionTradeStatistics(tradeStatistics)).getResult();
    }

    private Page<TradeInfoPage> getTradeInfoPages(Page<TradeInfoPage> recordInfoPage) {
        if (recordInfoPage != null && recordInfoPage.size() > 0) {
            int seq = 0;
            for (TradeInfoPage page : recordInfoPage) {
                seq++;
                page.setId(String.valueOf(seq));
                page.setRuleName(icmApi.getRuleName(page.getRuleId(), page.getProjectId()));
                page.setProjectName(ipmApi.getProjectName(page.getProjectId()));
                page.setSuccessRate(new DecimalFormat("0.00%").format(Float.parseFloat(page.getSuccessRate())));
                page.setFailRate(new DecimalFormat("0.00%").format(Float.parseFloat(page.getFailRate())));
            }
        }
        return recordInfoPage;
    }


    public String selectProjectIdByUuid(String uuid) {
        List<RecordInfo> recordInfos = selectByUuid(new RecordInfo(uuid));
        if (recordInfos != null && recordInfos.size() > 0) {
            return recordInfos.get(0).getProjectId();
        }
        return "";
    }

    public Page<Map<String, Object>> selectCallContentDetailByCondition2(RecordInfoPage recordInfoPage, int current, int pageSize){
        PageHelper.startPage(current, pageSize);
        return (Page<Map<String, Object>>) recordInfoMapper.selectCallContentDetailByCondition(new RecordInfoPage2(recordInfoPage));
    }

    public Page<Map<String, Object>> selectCallContentDetailByConditionTmp(RecordInfoPage recordInfoPage, int current, int pageSize){
        PageHelper.startPage(current, pageSize);
        return (Page<Map<String, Object>>) recordInfoMapper.selectCallContentDetailByConditionTmp(new RecordInfoPage2(recordInfoPage));
    }


    public List<CallContents> selectCallContentDetailByCondition(RecordInfoPage recordInfoPage, int current, int pageSize) throws IOException, SQLException, ParseException {
        List<CallContents> list = new LinkedList<>();
        Page<Map<String, Object>> p = selectCallContentDetailByConditionTmp(recordInfoPage, current, pageSize);
        if(!DateUtils.withinAMonth(recordInfoPage.getRecordStartTime())){
            Long lSum = Long.valueOf(p.getTotal());
            if(lSum < current * pageSize){
                p = selectCallContentDetailByCondition2(recordInfoPage, current, pageSize);
            }
        }
        if ( p!= null && p.size() > 0) {
            for (Map<String, Object> map : p) {
                String uuid = String.valueOf(map.get("UUID") != null ? map.get("UUID") : "");
                String recordPath = String.valueOf(map.get("RECORDPATH") != null ? map.get("RECORDPATH") : "");
                String callContent = map.get("RECORDPATH") != null ? ClobToString((Clob) map.get("CALLCONTENT")) : "[]";
                String customInfo = map.get("CUSTOMINFO") != null ? ClobToString((Clob) map.get("CUSTOMINFO")) : "[]";
                String callNumber = String.valueOf(map.get("CALLNUMBER") != null ? map.get("CALLNUMBER") : "[]");
                Date recordStartTime = (Date) (map.get("RECORDSTARTTIME"));
                CallContents ccs = new CallContents(uuid);
                ccs.setCallNumber(callNumber);
                ccs.setRecordStartTime(recordStartTime);
                if (StringUtils.isNotBlank(recordPath)) {
                    File file = new File(recordPath);
                    if (file.exists()) {
                        ccs.setVoiceFile(file);
                    }
                }
                if (StringUtils.isNotBlank(callContent)) {
                    ccs.setCallContent(callContent);
                }
                if (StringUtils.isNotBlank(customInfo)) {
                    ccs.setCustomInfo(customInfo);
                }
                list.add(ccs);
            }
        }
        return list;
    }

    public int countByCondition(RecordInfoPage recordInfoPage) {
        int count = 0;
        List<Map<String, Object>> result = recordInfoMapper.countByCondition(new RecordInfoPage2(recordInfoPage));
        if (result != null && result.size() > 0) {
            Map<String, Object> map = result.get(0);
            count = Integer.parseInt(String.valueOf(map.get("COUNT")));
        }
        return count;
    }

    private List<RecordEndNodeCntInfo> selectEndNodeCntByDate(RecordInfo recordInfo) {
        return recordInfoMapper.getRecordEndNodeCntByDate(recordInfo);
    }

    public Map<String, Object> getEndNodeCntResult(String projectId, String ruleId, String recordStartTime, String recordEndTime) {
        Map<String, Object> resMap = new HashMap<>();
        RecordInfo recordInfo = new RecordInfo();
        recordInfo.setProjectId(projectId);
        recordInfo.setRuleId(ruleId);
        recordInfo.setRecordStartTime(recordStartTime);
        recordInfo.setRecordEndTime(recordEndTime);

        List<RecordEndNodeCntInfo> list = selectEndNodeCntByDate(recordInfo);
        System.out.println("************************************");
        System.out.println("list大小" + list.size());
        System.out.println("************************************");
        log.info("Page Total == " + list);

        //获取所有的结束节点名称
        Set<String> nodeSet = new HashSet<>();
        //获取所有的日期
        Set<String> daySet = new LinkedHashSet<>();
        for (RecordEndNodeCntInfo node : list) {
            nodeSet.add(node.getEndNodeName());
            daySet.add(node.getRecordDate());
        }
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("recordDate", "日期"));
        Map<String, String> headMap = new HashMap<>();
        formatAllNodeName(tableHeads, nodeSet, headMap);

        Collection<String> values = headMap.values();
        ObjectMapper mapper = new ObjectMapper();
        List<ObjectNode> array = new ArrayList<>();
        Map<String, Map<String, Integer>> map = new HashMap<>();
        int i = 0;
        ObjectNode row = null;
        for (String day : daySet) {//一行
            row = mapper.createObjectNode();
            row.put("recordDate", day);
            for (String name : values) {
                if (null == name) {
                    name = "";
                }
                String title = "";
                for (TableHead tableHead : tableHeads) {
                    if (tableHead.getDataIndex().equals(name)) {
                        title = tableHead.getTitle();
                        break;
                    }
                }
                ObjectNode objectNode = new ObjectMapper().createObjectNode();
                objectNode.put("type", name);
                objectNode.put("name", title);
                objectNode.put("value", 0);
                row.put(name, objectNode);
            }
            for (; i < list.size(); i++) {
                RecordEndNodeCntInfo node = list.get(i);
                if (day.equals(node.getRecordDate())) {
                    String headKey = headMap.get(node.getEndNodeName());
                    String title = "";
                    for (TableHead tableHead : tableHeads) {
                        if (tableHead.getDataIndex().equals(headKey)) {
                            title = tableHead.getTitle();
                            break;
                        }
                    }
                    ObjectNode objectNode = new ObjectMapper().createObjectNode();
                    objectNode.put("type", headKey);
                    objectNode.put("name", title);
                    objectNode.put("value", node.getCnt());
                    row.put(headKey, objectNode);
                } else {
                    array.add(row);
                    break;
                }
            }
        }
        if (row != null)
            array.add(row);
        resMap.put("list", array);
        resMap.put("tableHeads", tableHeads);
        return resMap;
    }

    public Map<String, Object> getNodePassRateResult(NodePassRatePage nodePassRatePage, int current, int pageSize) throws ParseException {
        PageHelper.startPage(current, pageSize);
        Map<String, Object> p = getNodePassRateResultTmp(nodePassRatePage,true);
        if(DateUtils.withinAMonth(nodePassRatePage.getStartTime())){
            return p;
        }
        Long lSum = (Long) p.get("total");
        if(lSum < current * pageSize){
            PageHelper.startPage(current, pageSize);
            return getNodePassRateResult(nodePassRatePage, true);
        }
        p.replace("total", getNodePassRateResultCount(nodePassRatePage));
        return p;
    }

    public int getNodePassRateResultCount(NodePassRatePage nodePassRatePage){
        int count = 0;
        switch (nodePassRatePage.getStatisticalDimension()) {
            case "year":
                count = recordInfoMapper.selectPassNodeRateByYearCount(nodePassRatePage);
                break;
            case "month":
                count = recordInfoMapper.selectPassNodeRateByMonthCount(nodePassRatePage);
                break;
            case "week":
                count = recordInfoMapper.selectPassNodeRateByWeekCount(nodePassRatePage);
                break;
            default:
                count = recordInfoMapper.selectPassNodeRateByDayCount(nodePassRatePage);
        }
        return count;
    }

    public Map<String, Object> getNodePassRateResultTmp(NodePassRatePage nodePassRatePage, boolean flag) {
        Page<Map<String, Object>> nodePassRateResults;
        switch (nodePassRatePage.getStatisticalDimension()) {
            case "year":
                nodePassRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectPassNodeRateByYearTmp(nodePassRatePage);
                break;
            case "month":
                nodePassRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectPassNodeRateByMonthTmp(nodePassRatePage);
                break;
            case "week":
                nodePassRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectPassNodeRateByWeekTmp(nodePassRatePage);
                break;
            default:
                nodePassRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectPassNodeRateByDayTmp(nodePassRatePage);
        }
        long total = 0;
        if (flag) {
            total = nodePassRateResults.getTotal();
        }
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("date", "时间"));
        tableHeads.add(new TableHead("ruleId", "规则名称"));
        tableHeads.add(new TableHead("flowId", "流程名称"));
        tableHeads.add(new TableHead("sumCount", "总通数"));
        List<ObjectNode> array = handleNodePassResult(nodePassRatePage, nodePassRateResults, tableHeads);
        return getStringObjectMap(total, tableHeads, array);
    }

    private List<ObjectNode> handleNodePassResult(NodePassRatePage nodePassRatePage, Page<Map<String, Object>> nodePassRateResults, List<TableHead> tableHeads) {
        Set<String> nodeNameSet = new HashSet<>();
        Map<String, String> headMap = new HashMap<>();
        for (Map<String, Object> result : nodePassRateResults.getResult()) {
            String nodeNamesStr = result.get("NODENAMES").toString();
            String[] nodeNames = nodeNamesStr.split(",");
            nodeNameSet.addAll(Arrays.asList(nodeNames));
        }
        formatAllNodeName(tableHeads, nodeNameSet, headMap);
        ObjectNode row;
        ObjectMapper mapper = new ObjectMapper();
        Collection<String> values = headMap.values();
        List<ObjectNode> array = new ArrayList<>();
        for (Map<String, Object> result : nodePassRateResults.getResult()) {
            row = mapper.createObjectNode();
            String projectId = String.valueOf(result.get("PROJECTID"));
            String ruleId = String.valueOf(result.get("RULEID"));
            row.put("ruleId", icmApi.getRuleName(ruleId, projectId));
            String flowId = String.valueOf(result.get("FLOWID"));
            row.put("flowId", icmApi.getFlowName(flowId, projectId));
            String date = String.valueOf(result.get("DAY"));
            row.put("date", date);
            long sumCount = Long.valueOf(String.valueOf(result.get("SUMCOUNT")));
            row.put("sumCount", sumCount);
            for (String name : values) {
                if (null == name) {
                    name = "";
                }
                String title = "";
                for (TableHead tableHead : tableHeads) {
                    if (tableHead.getDataIndex().equals(name)) {
                        title = tableHead.getTitle();
                        break;
                    }
                }
                ObjectNode objectNode = new ObjectMapper().createObjectNode();
                objectNode.put("value", "0.00%");
                objectNode.put("ruleId", ruleId);
                objectNode.put("flowId", flowId);
                objectNode.put("date", date);
                objectNode.put("nodeName", title);
                objectNode.put("statisticalDimension", nodePassRatePage.getStatisticalDimension());
                objectNode.put("statisticalDirection", nodePassRatePage.getStatisticalDirection());
                row.put(name, objectNode);
            }
            String nodeNamesStr = result.get("NODENAMES").toString();
            String[] nodeNames = nodeNamesStr.split(",");
            String nodeCountsStr = result.get("NODECOUNTS").toString();
            String[] nodeCounts = nodeCountsStr.split(",");
            for (int i = 0; i < nodeNames.length; i++) {
                String name = headMap.get(nodeNames[i]);
                ObjectNode nodeObj = mapper.createObjectNode();
                nodeObj.put("value", new DecimalFormat("0.00%").format(Double.valueOf(nodeCounts[i]) / sumCount));
                nodeObj.put("ruleId", ruleId);
                nodeObj.put("flowId", flowId);
                nodeObj.put("date", date);
                nodeObj.put("nodeName", nodeNames[i]);
                nodeObj.put("statisticalDimension", nodePassRatePage.getStatisticalDimension());
                nodeObj.put("statisticalDirection", nodePassRatePage.getStatisticalDirection());
                row.put(name, nodeObj);
            }
            array.add(row);
        }
        return array;
    }

    public Map<String, Object> getNodePassRateResult(NodePassRatePage nodePassRatePage, boolean flag) {
        Page<Map<String, Object>> nodePassRateResults;
        switch (nodePassRatePage.getStatisticalDimension()) {
            case "year":
                nodePassRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectPassNodeRateByYear(nodePassRatePage);
                break;
            case "month":
                nodePassRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectPassNodeRateByMonth(nodePassRatePage);
                break;
            case "week":
                nodePassRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectPassNodeRateByWeek(nodePassRatePage);
                break;
            default:
                nodePassRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectPassNodeRateByDay(nodePassRatePage);
        }
        long total = 0;
        if (flag) {
            total = nodePassRateResults.getTotal();
        }
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("date", "时间"));
        tableHeads.add(new TableHead("ruleId", "规则名称"));
        tableHeads.add(new TableHead("flowId", "流程名称"));
        tableHeads.add(new TableHead("sumCount", "总通数"));
        List<ObjectNode> array = handleNodePassResult(nodePassRatePage, nodePassRateResults, tableHeads);
        return getStringObjectMap(total, tableHeads, array);
    }

    private void formatAllNodeName(List<TableHead> tableHeads, Set<String> nodeNameSet, Map<String, String> headMap) {
        int n = 1;
        for (String nodeName : nodeNameSet) {
            headMap.put(nodeName, "cnt" + n);
            tableHeads.add(new TableHead("cnt" + n, nodeName));
            n++;
        }
    }

    private String ClobToString(Clob clob) throws SQLException, IOException {
        String ret = "";
        Reader read = clob.getCharacterStream();
        BufferedReader br = new BufferedReader(read);
        String s = br.readLine();
        StringBuilder sb = new StringBuilder();
        while (s != null) {
            sb.append(s);
            s = br.readLine();
        }
        ret = sb.toString();
        br.close();
        read.close();
        return ret;
    }


    public CallContents selectCallContentDetailByUuid(String uuid) throws IOException, SQLException {
        List<Map<String, Object>> result = recordInfoMapper.selectCallContentDetailByUuid(uuid);
        if (result != null && result.size() > 0) {
            Map<String, Object> map = result.get(0);
            String callContent = ClobToString((Clob) map.get("CALLCONTENT"));
            String customInfo = ClobToString((Clob) map.get("CUSTOMINFO"));
            CallContents ccs = new CallContents(uuid);
            if (StringUtils.isNotBlank(callContent)) {
                ccs.setCallContent(callContent);
            }
            if (StringUtils.isNotBlank(customInfo)) {
                ccs.setCustomInfo(customInfo);
            }
            return ccs;
        }
        return null;
    }

    public Map<String, Object> getHangupNodeNameStatistical(EndNodeNamePage endNodeNamePage, int current, int pageSize) throws ParseException {
        PageHelper.startPage(current, pageSize);
        Map<String, Object> m = getHangupNodeNameStatisticalTmp(endNodeNamePage,true);
        if(DateUtils.withinAMonth(endNodeNamePage.getStartTime())){
            return m;
        }
        Long lSum = (Long) m.get("total");
        if(lSum < current * pageSize){
            PageHelper.startPage(current, pageSize);
            return getHangupNodeNameStatistical(endNodeNamePage, true);
        }
        m.replace("total", getHangupNodeNameStatisticalCount(endNodeNamePage));
        return m;
    }

    public int getHangupNodeNameStatisticalCount(EndNodeNamePage endNodeNamePage){
        int count = 0;
        switch (endNodeNamePage.getStatisticalDimension()) {
            case "year":
                count = recordInfoMapper.selectHangupNodeByYearCount(endNodeNamePage);
                break;
            case "month":
                count = recordInfoMapper.selectHangupNodeByMonthCount(endNodeNamePage);
                break;
            case "week":
                count = recordInfoMapper.selectHangupNodeByWeekCount(endNodeNamePage);
                break;
            default:
                count = recordInfoMapper.selectHangupNodeByDayCount(endNodeNamePage);
        }
        return count;
    }

    public Map<String, Object> getHangupNodeNameStatisticalTmp(EndNodeNamePage endNodeNamePage, boolean flag) {
        Page<Map<String, Object>> hangupNodeResults;
        switch (endNodeNamePage.getStatisticalDimension()) {
            case "year":
                hangupNodeResults = (Page<Map<String, Object>>) recordInfoMapper.selectHangupNodeByYearTmp(endNodeNamePage);
                break;
            case "month":
                hangupNodeResults = (Page<Map<String, Object>>) recordInfoMapper.selectHangupNodeByMonthTmp(endNodeNamePage);
                break;
            case "week":
                hangupNodeResults = (Page<Map<String, Object>>) recordInfoMapper.selectHangupNodeByWeekTmp(endNodeNamePage);
                break;
            default:
                hangupNodeResults = (Page<Map<String, Object>>) recordInfoMapper.selectHangupNodeByDayTmp(endNodeNamePage);
        }
        long total = 0;
        if (flag) {
            total = hangupNodeResults.getTotal();
        }
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("date", "时间"));
        tableHeads.add(new TableHead("flowId", "流程名称"));
        tableHeads.add(new TableHead("sumCount", "总通数"));
        List<ObjectNode> array = handleHangupNodeNameStatistical(endNodeNamePage, hangupNodeResults, tableHeads);
        return getStringObjectMap(total, tableHeads, array);
    }

    private List<ObjectNode> handleHangupNodeNameStatistical(EndNodeNamePage endNodeNamePage, Page<Map<String, Object>> hangupNodeResults, List<TableHead> tableHeads) {
        Set<String> nodeNameSet = new HashSet<>();
        Map<String, String> headMap = new HashMap<>();
        for (Map<String, Object> result : hangupNodeResults.getResult()) {
            String nodeNamesStr = result.get("NODENAMES").toString();
            String[] nodeNames = nodeNamesStr.split(",");
            nodeNameSet.addAll(Arrays.asList(nodeNames));
        }
        formatAllNodeName(tableHeads, nodeNameSet, headMap);
        ObjectNode row;
        ObjectMapper mapper = new ObjectMapper();
        Collection<String> values = headMap.values();
        List<ObjectNode> array = new ArrayList<>();
        for (Map<String, Object> result : hangupNodeResults.getResult()) {
            row = mapper.createObjectNode();
            String projectId = String.valueOf(result.get("PROJECTID"));
            String flowId = String.valueOf(result.get("FLOWID"));
            row.put("flowId", icmApi.getFlowName(flowId, projectId));
            String date = String.valueOf(result.get("DAY"));
            row.put("date", date);
            long sumCount = Long.valueOf(String.valueOf(result.get("SUMCOUNT")));
            row.put("sumCount", sumCount);
            for (String name : values) {
                if (null == name) {
                    name = "";
                }
                String title = "";
                for (TableHead tableHead : tableHeads) {
                    if (tableHead.getDataIndex().equals(name)) {
                        title = tableHead.getTitle();
                        break;
                    }
                }
                ObjectNode objectNode = new ObjectMapper().createObjectNode();
                objectNode.put("value", "0.00%");
                objectNode.put("flowId", flowId);
                objectNode.put("date", date);
                objectNode.put("endNodeName", title);
                objectNode.put("statisticalDimension", endNodeNamePage.getStatisticalDimension());
                row.put(name, objectNode);
            }
            String nodeNamesStr = result.get("NODENAMES").toString();
            String[] nodeNames = nodeNamesStr.split(",");
            String nodeCountsStr = result.get("NODECOUNTS").toString();
            String[] nodeCounts = nodeCountsStr.split(",");
            for (int i = 0; i < nodeNames.length; i++) {
                String name = headMap.get(nodeNames[i]);
                ObjectNode nodeObj = mapper.createObjectNode();
                nodeObj.put("value", new DecimalFormat("0.00%").format(Double.valueOf(nodeCounts[i]) / sumCount));
                nodeObj.put("flowId", flowId);
                nodeObj.put("date", date);
                nodeObj.put("endNodeName", nodeNames[i]);
                nodeObj.put("statisticalDimension", endNodeNamePage.getStatisticalDimension());
                row.put(name, nodeObj);
            }
            array.add(row);
        }
        return array;
    }

    public Map<String, Object> getHangupNodeNameStatistical(EndNodeNamePage endNodeNamePage, boolean flag) {
        Page<Map<String, Object>> hangupNodeResults;
        switch (endNodeNamePage.getStatisticalDimension()) {
            case "year":
                hangupNodeResults = (Page<Map<String, Object>>) recordInfoMapper.selectHangupNodeByYear(endNodeNamePage);
                break;
            case "month":
                hangupNodeResults = (Page<Map<String, Object>>) recordInfoMapper.selectHangupNodeByMonth(endNodeNamePage);
                break;
            case "week":
                hangupNodeResults = (Page<Map<String, Object>>) recordInfoMapper.selectHangupNodeByWeek(endNodeNamePage);
                break;
            default:
                hangupNodeResults = (Page<Map<String, Object>>) recordInfoMapper.selectHangupNodeByDay(endNodeNamePage);
        }
        long total = 0;
        if (flag) {
            total = hangupNodeResults.getTotal();
        }
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("date", "时间"));
        tableHeads.add(new TableHead("flowId", "流程名称"));
        tableHeads.add(new TableHead("sumCount", "总通数"));
        List<ObjectNode> array = handleHangupNodeNameStatistical(endNodeNamePage, hangupNodeResults, tableHeads);
        return getStringObjectMap(total, tableHeads, array);
    }

    private Map<String, Object> getStringObjectMap(long total, List<TableHead> tableHeads, List<ObjectNode> array) {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("total", total);
        resMap.put("list", array);
        resMap.put("tableHeads", tableHeads);
        return resMap;
    }

    public List<RecordInfoPage> selectPageByNodePassCondition(NodePassRatePage nodePassRatePage) {
        List<RecordInfoPage> recordInfos = recordInfoMapper.selectPassNodeRateDetail(nodePassRatePage);
        return forMatRuleName((Page<RecordInfoPage>) recordInfos);
    }

    public Page selectPageByNodePassCondition(NodePassRatePage nodePassRatePage, int current, int pageSize) throws ParseException {
        Page p = selectPageByNodePassConditionTmp(nodePassRatePage, current, pageSize);
        if(DateUtils.withinAMonth(nodePassRatePage.getStartTime())){
            return p;
        }
        Long lSum = Long.valueOf(p.getTotal());
        if(lSum < current * pageSize){
            return selectPageByNodePassCondition2(nodePassRatePage, current, pageSize);
        }
        p.setTotal(recordInfoMapper.selectPassNodeRateDetailCount(nodePassRatePage));
        return p;
    }

    public Page<RecordInfoPage> selectPageByNodePassConditionTmp(NodePassRatePage nodePassRatePage, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        return forMatRuleName((Page<RecordInfoPage>) recordInfoMapper.selectPassNodeRateDetailTmp(nodePassRatePage));
    }

    public Page<RecordInfoPage> selectPageByNodePassCondition2(NodePassRatePage nodePassRatePage, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        return forMatRuleName((Page<RecordInfoPage>) recordInfoMapper.selectPassNodeRateDetail(nodePassRatePage));
    }

    private Page<RecordInfoPage> forMatRuleName(Page<RecordInfoPage> recordInfos) {
        for (RecordInfoPage info : recordInfos) {
            if (StringUtils.isNotBlank(info.getRuleId())) {
                info.setRuleName(icmApi.getRuleName(info.getRuleId(), info.getProjectId()));
            }
            if (StringUtils.isNotBlank(info.getOnState())) {
                info.setOnState(info.getOnState().equals("connect") ? "接通" : "未接通");
            }
            if (StringUtils.isNotBlank(info.getFinalNodeName())) {
                info.setCallResult(info.getFinalNodeName());
            }
        }
        return recordInfos;
    }

    public List<RecordInfoPage> selectPageByHangupNodeCondition(EndNodeNamePage endNodeNamePage) {
        List<RecordInfoPage> recordInfos = recordInfoMapper.selectHangupNodeDetail(endNodeNamePage);
        return forMatRuleName((Page<RecordInfoPage>) recordInfos);
    }

    public Page selectPageByHangupNodeCondition(EndNodeNamePage endNodeNamePage, int current, int pageSize) throws ParseException {
        Page p = selectPageByHangupNodeConditionTmp(endNodeNamePage, current, pageSize);
        if(DateUtils.withinAMonth(endNodeNamePage.getStartTime())){
            return p;
        }
        Long lSum = Long.valueOf(p.getTotal());
        if(lSum < current * pageSize){
            return selectPageByHangupNodeCondition2(endNodeNamePage, current, pageSize);
        }
        p.setTotal(recordInfoMapper.selectHangupNodeDetailCount(endNodeNamePage));
        return p;
    }

    public Page<RecordInfoPage> selectPageByHangupNodeConditionTmp(EndNodeNamePage endNodeNamePage, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        return forMatRuleName((Page)recordInfoMapper.selectHangupNodeDetailTmp(endNodeNamePage));
    }

    public Page<RecordInfoPage> selectPageByHangupNodeCondition2(EndNodeNamePage endNodeNamePage, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        return forMatRuleName((Page)recordInfoMapper.selectHangupNodeDetail(endNodeNamePage));
    }

    public Map<String, Object> getSceneThroughDetail(SceneThroughPage sceneThroughPage, int current, int pageSize) throws ParseException {
        PageHelper.startPage(current, pageSize);
        Map<String, Object> m = getSceneThroughDetailTmp(sceneThroughPage,true);
        if(DateUtils.withinAMonth(sceneThroughPage.getStartTime())){
            return m;
        }
        Long lSum = (Long) m.get("total");
        if(lSum < current * pageSize){
            PageHelper.startPage(current, pageSize);
            return getSceneThroughDetail(sceneThroughPage, true);
        }
        m.replace("total", recordInfoMapper.selectSceneThroughDeatilCount(sceneThroughPage));
        return m;
    }

    public Map<String, Object> getSceneThroughDetailTmp(SceneThroughPage sceneThroughPage, boolean flag) {
        Page<Map<String, Object>> hangupNodeResults = (Page<Map<String, Object>>) recordInfoMapper.selectSceneThroughDeatilTmp(sceneThroughPage);
        long total = 0;
        if (flag) {
            total = hangupNodeResults.getTotal();
        }
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("customerId", "客户编号"));
        tableHeads.add(new TableHead("ruleId", "规则名称"));
        tableHeads.add(new TableHead("flowId", "流程名称"));
        tableHeads.add(new TableHead("onState", "接通状态"));
        tableHeads.add(new TableHead("startTime", "开始时间"));
        tableHeads.add(new TableHead("endTime", "结束时间"));
        tableHeads.add(new TableHead("duration", "时长"));
        tableHeads.add(new TableHead("contents", "对白文本"));
        List<ObjectNode> array = handleSceneThroughDetail(hangupNodeResults, tableHeads);
        return getStringObjectMap(total, tableHeads, array);
    }

    private List<ObjectNode> handleSceneThroughDetail(Page<Map<String, Object>> hangupNodeResults, List<TableHead> tableHeads) {
        Set<String> nodeNameSet = new HashSet<>();
        Map<String, String> headMap = new HashMap<>();
        for (Map<String, Object> result : hangupNodeResults.getResult()) {
            String nodeNamesStr = result.get("NODENAMES").toString();
            String[] nodeNames = nodeNamesStr.split(",");
            nodeNameSet.addAll(Arrays.asList(nodeNames));
        }
        formatAllNodeName(tableHeads, nodeNameSet, headMap);
        ObjectNode row;
        ObjectMapper mapper = new ObjectMapper();
        Collection<String> values = headMap.values();
        List<ObjectNode> array = new ArrayList<>();
        for (Map<String, Object> result : hangupNodeResults.getResult()) {
            row = mapper.createObjectNode();
            String projectId = String.valueOf(result.get("PROJECTID"));
            String uuid = String.valueOf(result.get("UUID"));
            row.put("uuid", uuid);
            String ruleid = String.valueOf(result.get("RULEID"));
            row.put("ruleId", icmApi.getRuleName(ruleid, projectId));
            String flowId = String.valueOf(result.get("FLOWID"));
            row.put("flowId", icmApi.getFlowName(flowId, projectId));
            String customerId = String.valueOf(result.get("CUSTOMERID"));
            row.put("customerId", customerId);
            String onstate = String.valueOf(result.get("ONSTATE"));
            row.put("onState", onstate.equals("connect") ? "接通" : "未接通");
            String startTime = String.valueOf(result.get("STARTTIME"));
            row.put("startTime", startTime);
            String endTime = String.valueOf(result.get("ENDTIME"));
            row.put("endTime", endTime);
            String duration = String.valueOf(result.get("DURATION"));
            row.put("duration", duration);
            row.put("contents", result.get("CONTENTS").toString());
            for (String name : values) {
                if (null == name) {
                    name = "";
                }
                row.put(name, "未进入");
            }
            String nodeNamesStr = result.get("NODENAMES").toString();
            String[] nodeNames = nodeNamesStr.split(",");
            String nodeCountsStr = result.get("SCENENAMES").toString();
            String[] nodeCounts = nodeCountsStr.split(",");
            for (int i = 0; i < nodeNames.length; i++) {
                String name = headMap.get(nodeNames[i]);
                switch (nodeCounts[i]) {
                    case "notThrough":
                        row.put(name, "未通过");
                        break;
                    case "through":
                        row.put(name, "通过");
                        break;
                    case "noSpeak":
                        row.put(name, "客户静音");
                        break;
                    case "hangup":
                        row.put(name, "客户挂机");
                        break;
                }
            }
            array.add(row);
        }
        return array;
    }

    public Map<String, Object> getSceneThroughDetail(SceneThroughPage sceneThroughPage, boolean flag) {
        Page<Map<String, Object>> hangupNodeResults = (Page<Map<String, Object>>) recordInfoMapper.selectSceneThroughDeatil(sceneThroughPage);
        long total = 0;
        if (flag) {
            total = hangupNodeResults.getTotal();
        }
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("customerId", "客户编号"));
        tableHeads.add(new TableHead("ruleId", "规则名称"));
        tableHeads.add(new TableHead("flowId", "流程名称"));
        tableHeads.add(new TableHead("onState", "接通状态"));
        tableHeads.add(new TableHead("startTime", "开始时间"));
        tableHeads.add(new TableHead("endTime", "结束时间"));
        tableHeads.add(new TableHead("duration", "时长"));
        tableHeads.add(new TableHead("contents", "对白文本"));
        List<ObjectNode> array = handleSceneThroughDetail(hangupNodeResults, tableHeads);
        return getStringObjectMap(total, tableHeads, array);
    }

    public Map<String, Object> getConnectionRate(ConnectRate connectRate, int current, int pageSize) throws ParseException {
        PageHelper.startPage(current, pageSize);
        Map<String, Object> m = getConnectionRateTmp(connectRate,true);
        if(DateUtils.withinAMonth(connectRate.getStartTime())){
            return m;
        }
        Long lSum = (Long) m.get("total");
        if(lSum < current * pageSize){
            PageHelper.startPage(current, pageSize);
            return getConnectionRate(connectRate, true);
        }
        m.replace("total", getConnectionRateCount(connectRate));
        return m;
    }

    public int getConnectionRateCount(ConnectRate connectRate){
        int count = 0;
        switch (connectRate.getStatisticalDimension()) {
            case "year":
                count = recordInfoMapper.selectConnectionRateByYearCount(connectRate);
                break;
            case "month":
                count = recordInfoMapper.selectConnectionRateByMonthCount(connectRate);
                break;
            case "week":
                count = recordInfoMapper.selectConnectionRateByWeekCount(connectRate);
                break;
            default:
                count = recordInfoMapper.selectConnectionRateByDayCount(connectRate);
        }
        return count;
    }

    public Map<String, Object> getConnectionRateTmp(ConnectRate connectRate, boolean flag) {
        Page<Map<String, Object>> connectionRateResults;
        switch (connectRate.getStatisticalDimension()) {
            case "year":
                connectionRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectConnectionRateByYearTmp(connectRate);
                break;
            case "month":
                connectionRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectConnectionRateByMonthTmp(connectRate);
                break;
            case "week":
                connectionRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectConnectionRateByWeekTmp(connectRate);
                break;
            default:
                connectionRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectConnectionRateByDayTmp(connectRate);
        }
        long total = 0;
        if (flag) {
            total = connectionRateResults.getTotal();
        }
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("projectName", "项目名称"));
        tableHeads.add(new TableHead("ruleName", "规则名称"));
        tableHeads.add(new TableHead("date", "时间"));
        List<ObjectNode> array = handleConnectionRate(connectRate, connectionRateResults, tableHeads);
        return getStringObjectMap(total, tableHeads, array);
    }

    private List<ObjectNode> handleConnectionRate(ConnectRate connectRate, Page<Map<String, Object>> connectionRateResults, List<TableHead> tableHeads) {
        Set<String> timeSet = new HashSet<String>();
        List<String> timeList = new ArrayList<String>();
        Map<String, String> headMap = new HashMap<>();
        for (Map<String, Object> result : connectionRateResults.getResult()) {
            String timeStr = (String) result.get("TIMES");
            String[] times = timeStr.split(",");
            timeSet.addAll(Arrays.asList(times));
        }
        timeList.addAll(timeSet);
        //对times进行排序
        Collections.sort(timeList);
        int n = 1;
        for (String time : timeList) {
            String timeStr = time + ":00~" + time + ":59";
            headMap.put(timeStr, "cnt" + n);
            tableHeads.add(new TableHead("cnt" + n, timeStr));
            n++;
        }
        log.info(headMap + "");
        ObjectNode row;
        ObjectMapper mapper = new ObjectMapper();
        Collection<String> values = headMap.values();
        List<ObjectNode> array = new ArrayList<>();
        for (Map<String, Object> result : connectionRateResults.getResult()) {
            row = mapper.createObjectNode();
            String projectId = String.valueOf(result.get("PROJECTID"));
            String ruleId = String.valueOf(result.get("RULEID"));
            row.put("projectId", projectId);
            row.put("ruleId", ruleId);
            row.put("projectName", ipmApi.getProjectName(projectId));
            row.put("ruleName", icmApi.getRuleName(ruleId, projectId));
            String date = String.valueOf(result.get("DAY"));
            row.put("date", date);
            long sumCount = Long.valueOf(String.valueOf(result.get("COUNTS")));
            row.put("sumCount", sumCount);
            for (String name : values) {
                if (null == name) {
                    name = "";
                }
                String title = "";
                for (TableHead tableHead : tableHeads) {
                    if (tableHead.getDataIndex().equals(name)) {
                        title = tableHead.getTitle();
                        break;
                    }
                }
                ObjectNode objectNode = new ObjectMapper().createObjectNode();
                objectNode.put("projectId", projectId);
                objectNode.put("ruleId", ruleId);
                objectNode.put("projectName", ipmApi.getProjectName(projectId));
                objectNode.put("ruleName", icmApi.getRuleName(ruleId, projectId));
                objectNode.put("date", date);
                objectNode.put("time", title);
                objectNode.put("value", "0.00%");
                objectNode.put("statisticalDimension", connectRate.getStatisticalDimension());
                row.put(name, objectNode);
            }
            String timeNamesStr = (String) result.get("TIMES");
            String[] timeNames = timeNamesStr.split(",");
            String timeCountStr = (String) result.get("TIMECOUNTS");
            String[] timeCount = timeCountStr.split(",");
            for (int i = 0; i < timeNames.length; i++) {
                String name = headMap.get(timeNames[i] + ":00~" + timeNames[i] + ":59");
                ObjectNode nodeObj = mapper.createObjectNode();
                nodeObj.put("projectId", projectId);
                nodeObj.put("ruleId", ruleId);
                nodeObj.put("projectName", ipmApi.getProjectName(projectId));
                nodeObj.put("ruleName", icmApi.getRuleName(ruleId, projectId));
                nodeObj.put("date", date);
                nodeObj.put("time", timeNames[i] + ":00~" + timeNames[i] + ":59");
                nodeObj.put("value", new DecimalFormat("0.00%").format(Double.valueOf(timeCount[i]) / Double.valueOf(sumCount)));
                nodeObj.put("statisticalDimension", connectRate.getStatisticalDimension());
                row.put(name, nodeObj);
            }
            array.add(row);
        }
        return array;
    }

    @SuppressWarnings("deprecation")
    public Map<String, Object> getConnectionRate(ConnectRate connectRate, boolean flag) {
        Page<Map<String, Object>> connectionRateResults;
        switch (connectRate.getStatisticalDimension()) {
            case "year":
                connectionRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectConnectionRateByYear(connectRate);
                break;
            case "month":
                connectionRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectConnectionRateByMonth(connectRate);
                break;
            case "week":
                connectionRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectConnectionRateByWeek(connectRate);
                break;
            default:
                connectionRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectConnectionRateByDay(connectRate);
        }
        long total = 0;
        if (flag) {
            total = connectionRateResults.getTotal();
        }
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("projectName", "项目名称"));
        tableHeads.add(new TableHead("ruleName", "规则名称"));
        tableHeads.add(new TableHead("date", "时间"));
        List<ObjectNode> array = handleConnectionRate(connectRate, connectionRateResults, tableHeads);
        return getStringObjectMap(total, tableHeads, array);
    }

    public List<RecordInfoPage> selectPageConnectionRateCondition(ConnectRate connectRate) {
        List<RecordInfoPage> recordInfos = recordInfoMapper.selectConnectionRateDetail(connectRate);
        return forMatRuleName((Page<RecordInfoPage>) recordInfos);
    }

    public Page<RecordInfoPage> selectPageConnectionRateCondition(ConnectRate connectRate, int current, int pageSize) throws ParseException {
        Page<RecordInfoPage> p = selectPageConnectionRateConditionTmp(connectRate, current, pageSize);
        if(DateUtils.withinAMonth(connectRate.getStartTime())){
            return p;
        }
        Long lSum = Long.valueOf(p.getTotal());
        if(lSum < current * pageSize){
            return selectPageConnectionRateCondition2(connectRate, current, pageSize);
        }
        p.setTotal(recordInfoMapper.selectConnectionRateDetailCount(connectRate));
        return p;
    }

    public Page<RecordInfoPage> selectPageConnectionRateConditionTmp(ConnectRate connectRate, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        Page<RecordInfoPage> recordInfos = (Page<RecordInfoPage>) recordInfoMapper.selectConnectionRateDetailTmp(connectRate);
        return forMatRuleName(recordInfos);
    }

    public Page<RecordInfoPage> selectPageConnectionRateCondition2(ConnectRate connectRate, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        Page<RecordInfoPage> recordInfos = (Page<RecordInfoPage>) recordInfoMapper.selectConnectionRateDetail(connectRate);
        return forMatRuleName(recordInfos);
    }

    public Map<String, Object> getHangupRate(EndNodeNamePage endNodeNamePage, int current, int pageSize) throws ParseException {
        PageHelper.startPage(current, pageSize);
        Map<String, Object> m = getHangupRateTmp(endNodeNamePage,true);
        if(DateUtils.withinAMonth(endNodeNamePage.getStartTime())){
            return m;
        }
        Long lSum = (Long) m.get("total");
        if(lSum < current * pageSize){
            PageHelper.startPage(current, pageSize);
            return getHangupRate(endNodeNamePage, true);
        }
        m.replace("total", getHungupRateTmpCount(endNodeNamePage));
        return m;
    }

    public int getHungupRateTmpCount(EndNodeNamePage endNodeNamePage){
        int count = 0;
        switch (endNodeNamePage.getStatisticalDimension()) {
            case "year":
                count = recordInfoMapper.selectHangupRateByYearCount(endNodeNamePage);
                break;
            case "month":
                count = recordInfoMapper.selectHangupRateByMonthCount(endNodeNamePage);
                break;
            case "week":
                count = recordInfoMapper.selectHangupRateByWeekCount(endNodeNamePage);
                break;
            default:
                count = recordInfoMapper.selectHangupRateByDayCount(endNodeNamePage);
        }
        return count;
    }

    public Map<String, Object> getHangupRateTmp(EndNodeNamePage endNodeNamePage, boolean flag) {
        Page<Map<String, Object>> hangupRateResults;
        log.info(endNodeNamePage + "");
        switch (endNodeNamePage.getStatisticalDimension()) {
            case "year":
                hangupRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectHangupRateByYearTmp(endNodeNamePage);
                break;
            case "month":
                hangupRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectHangupRateByMonthTmp(endNodeNamePage);
                break;
            case "week":
                hangupRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectHangupRateByWeekTmp(endNodeNamePage);
                break;
            default:
                hangupRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectHangupRateByDayTmp(endNodeNamePage);
        }
        log.info("查询结果数为：" + hangupRateResults.size() + "条");
        long total = 0;
        if (flag) {
            total = hangupRateResults.getTotal();
        }
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("projectName", "项目名称"));
        tableHeads.add(new TableHead("ruleName", "规则名称"));
        tableHeads.add(new TableHead("date", "时间"));
        List<ObjectNode> array = handleHangupRate(endNodeNamePage, hangupRateResults, tableHeads);
        return getStringObjectMap(total, tableHeads, array);
    }

    private List<ObjectNode> handleHangupRate(EndNodeNamePage endNodeNamePage, Page<Map<String, Object>> hangupRateResults, List<TableHead> tableHeads) {
        Set<String> nodeNameSet = new HashSet<String>();
        List<String> nodeNameList = new ArrayList<String>();
        Map<String, String> headMap = new HashMap<>();
        for (Map<String, Object> result : hangupRateResults.getResult()) {
            String nodeNamesStr = (String) result.get("NODENAMES");
            String[] nodeNames = nodeNamesStr.split(",");
            nodeNameSet.addAll(Arrays.asList(nodeNames));
        }
        nodeNameList.addAll(nodeNameSet);
        //对nodename进行排序
        Collections.sort(nodeNameList);
        int n = 1;
        for (String nodeName : nodeNameList) {
            headMap.put(nodeName, "cnt" + n);
            tableHeads.add(new TableHead("cnt" + n, nodeName));
            n++;
        }
        ObjectNode row;
        ObjectMapper mapper = new ObjectMapper();
        Collection<String> values = headMap.values();
        List<ObjectNode> array = new ArrayList<>();
        for (Map<String, Object> result : hangupRateResults.getResult()) {
            row = mapper.createObjectNode();
            String projectId = String.valueOf(result.get("PROJECTID"));
            String ruleId = String.valueOf(result.get("RULEID"));
            row.put("projectId", projectId);
            row.put("ruleId", ruleId);
            row.put("projectName", ipmApi.getProjectName(projectId));
            row.put("ruleName", icmApi.getRuleName(ruleId, projectId));
            String date = String.valueOf(result.get("DAY"));
            long sumCount = Long.valueOf(String.valueOf(result.get("COUNTS")));
            row.put("sumCount", String.valueOf(sumCount));
            row.put("date", date);
            for (String name : values) {
                if (null == name) {
                    name = "";
                }
                String title = "";
                for (TableHead tableHead : tableHeads) {
                    if (tableHead.getDataIndex().equals(name)) {
                        title = tableHead.getTitle();
                        break;
                    }
                }
                ObjectNode objectNode = new ObjectMapper().createObjectNode();
                objectNode.put("projectId", projectId);
                objectNode.put("ruleId", ruleId);
                objectNode.put("projectName", ipmApi.getProjectName(projectId));
                objectNode.put("ruleName", icmApi.getRuleName(ruleId, projectId));
                objectNode.put("date", date);
                objectNode.put("endNodeName", title);
                objectNode.put("value", "0.00%");
                objectNode.put("statisticalDimension", endNodeNamePage.getStatisticalDimension());
                row.put(name, objectNode);
            }
            String nodeNamesStr = (String) result.get("NODENAMES");
            String[] nodeNames = nodeNamesStr.split(",");
            String nodeCountsStr = (String) result.get("NODECOUNTS");
            String[] nodeCounts = nodeCountsStr.split(",");
            for (int i = 0; i < nodeNames.length; i++) {
                String name = headMap.get(nodeNames[i]);
                ObjectNode nodeObj = mapper.createObjectNode();
                nodeObj.put("projectId", projectId);
                nodeObj.put("ruleId", ruleId);
                nodeObj.put("projectName", ipmApi.getProjectName(projectId));
                nodeObj.put("ruleName", icmApi.getRuleName(ruleId, projectId));
                nodeObj.put("date", date);
                nodeObj.put("endNodeName", nodeNames[i]);
                nodeObj.put("value", new DecimalFormat("0.00%").format(Double.valueOf(nodeCounts[i]) / sumCount));
                nodeObj.put("statisticalDimension", endNodeNamePage.getStatisticalDimension());
                row.put(name, nodeObj);
            }
            array.add(row);
        }
        return array;
    }

    @SuppressWarnings("deprecation")
    public Map<String, Object> getHangupRate(EndNodeNamePage endNodeNamePage, boolean flag) {
        Page<Map<String, Object>> hangupRateResults;
        log.info(endNodeNamePage + "");
        switch (endNodeNamePage.getStatisticalDimension()) {
            case "year":
                hangupRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectHangupRateByYear(endNodeNamePage);
                break;
            case "month":
                hangupRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectHangupRateByMonth(endNodeNamePage);
                break;
            case "week":
                hangupRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectHangupRateByWeek(endNodeNamePage);
                break;
            default:
                hangupRateResults = (Page<Map<String, Object>>) recordInfoMapper.selectHangupRateByDay(endNodeNamePage);
        }
        log.info("查询结果数为：" + hangupRateResults.size() + "条");
        long total = 0;
        if (flag) {
            total = hangupRateResults.getTotal();
        }
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("projectName", "项目名称"));
        tableHeads.add(new TableHead("ruleName", "规则名称"));
        tableHeads.add(new TableHead("date", "时间"));
        List<ObjectNode> array = handleHangupRate(endNodeNamePage, hangupRateResults, tableHeads);
        return getStringObjectMap(total, tableHeads, array);
    }

    public Page<?> selectPageHangupRateCondition(EndNodeNamePage endNodeNamePage, int current, int pageSize) throws ParseException {
        Page<?> p = selectPageHangupRateConditionTmp(endNodeNamePage, current, pageSize);
        if(DateUtils.withinAMonth(endNodeNamePage.getStartTime())){
            return p;
        }
        Long lSum = Long.valueOf(p.getTotal());
        if(lSum < current * pageSize){
            return selectPageHangupRateCondition2(endNodeNamePage, current, pageSize);
        }
        p.setTotal(recordInfoMapper.selectHangupRateDetailCount(endNodeNamePage));
        return p;
    }

    public Page<RecordInfoPage> selectPageHangupRateCondition2(EndNodeNamePage endNodeNamePage, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        Page<RecordInfoPage> recordInfos = (Page<RecordInfoPage>) recordInfoMapper.selectHangupRateDetail(endNodeNamePage);
        return forMatRuleName(recordInfos);
    }

    public Page<RecordInfoPage> selectPageHangupRateConditionTmp(EndNodeNamePage endNodeNamePage, int current, int pageSize) {
        PageHelper.startPage(current, pageSize);
        Page<RecordInfoPage> recordInfos = (Page<RecordInfoPage>) recordInfoMapper.selectHangupRateDetailTmp(endNodeNamePage);
        return forMatRuleName(recordInfos);
    }

    public List<RecordInfoPage> selectPageHangupRateCondition(EndNodeNamePage endNodeNamePage) {
        List<RecordInfoPage> recordInfos = recordInfoMapper.selectHangupRateDetail(endNodeNamePage);
        return forMatRuleName((Page<RecordInfoPage>) recordInfos);
    }
}


