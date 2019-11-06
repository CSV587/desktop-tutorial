package com.hy.iom.reporting.controller;

import com.github.pagehelper.Page;
import com.hy.iom.common.page.ChartResult;
import com.hy.iom.common.page.PageParam;
import com.hy.iom.common.page.PageResult;
import com.hy.iom.common.page.TableHeadsFactory;
import com.hy.iom.entities.CallErrorStatistics;
import com.hy.iom.entities.CallStatistics;
import com.hy.iom.entities.TradeStatistics;
import com.hy.iom.reporting.page.*;
import com.hy.iom.reporting.utils.ReportingUtils;
import com.hy.iom.service.CallStatisticsService;
import com.hy.iom.service.RecordInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author yuzhiping
 * @date 2018年12月27日 
 * Description：
 * 运营管理平台-查询模块控制器
 */
@RestController
@RequestMapping("/reportingQuery")
public class ReportingQueryController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

    private final RecordInfoService recordInfoService;
    private final CallStatisticsService callStatisticsService;

    public ReportingQueryController(RecordInfoService recordInfoService, CallStatisticsService callStatisticsService) {
        this.recordInfoService = recordInfoService;
        this.callStatisticsService = callStatisticsService;
    }
	
	/**
     * 呼叫列表-查询
     *
     * @param recordInfoPage 
     * @param current
     * @param pageSize
     * @return 结果
     */
    @PostMapping("/recordInfo")
    public PageResult recordInfo(@RequestBody RecordInfoPage recordInfoPage, @RequestParam int current, @RequestParam int pageSize) throws ParseException {
        Page page = recordInfoService.selectPageByCondition(recordInfoPage, current, pageSize);
        return PageResult.success("success", page, TableHeadsFactory.createRecordInfosHead());
    }
    
    /**
     * 呼叫列表-符合条件的数量
     *
     * @param recordInfoPage
     * @return 结果
     */
    @PostMapping("/recordInfoCount")
    public ChartResult recordInfoCount(@RequestBody RecordInfoPage recordInfoPage) {
        int count = recordInfoService.countByCondition(recordInfoPage);
        Map map = new HashMap();
        map.put("count", count);
        return ChartResult.success(map);
    }
    
    /**
     * 呼叫结果统计-查询
     *
     * @param projectId       项目Id
     * @param ruleId          规则Id
     * @param recordStartTime 开始呼叫时间
     * @param recordEndTime   结束呼叫时间
     * @return 结果
     */
    @GetMapping("/endNodeCntByDate")
    public PageResult endNodeCntByDate(@RequestParam String projectId, @RequestParam(required = false) String ruleId, @RequestParam String recordStartTime, @RequestParam String recordEndTime) {
        Map<String, Object> resMap = recordInfoService.getEndNodeCntResult(projectId, ruleId, recordStartTime, recordEndTime);
        List list = (List) resMap.get("list");
        List tableHeads = (List) resMap.get("tableHeads");
        return PageResult.success("操作成功", list, tableHeads);
    }
    
    /**
     * 外呼次数接通率
     * 
     * @param projectId       项目id
     * @param ruleId          规则id
     * @param queryMode       查询方式
     * @param date            日期
     * @return
     * 
     */
    @GetMapping("/connectedRate")
    public ChartResult connectedRate(@RequestParam(required = false) String projectId,
                                     @RequestParam(required = false) String ruleId,
                                     @RequestParam(required = false) String queryMode,
                                     @RequestParam(required = false) String date) {
        List<CallStatistics> onceConnectList = new ArrayList<>();
        List<CallStatistics> multiConnectList = new ArrayList<>();
        //一次外呼接通与未接通数
        Map<String, LinkedList<Integer>> onceHashMap = new LinkedHashMap<>();
        //多次外呼接通与未接通数
        Map<String, LinkedList<Integer>> multiHashMap = new LinkedHashMap<>();
        //外呼接通率
        Map<String, LinkedList<Double>> hashMap = new LinkedHashMap<>();

        CallStatistics callStatistics = new CallStatistics();
        callStatistics.setProjectId(projectId);
        callStatistics.setRuleId(ruleId);
        callStatistics.setDay(date);
        LinkedList<Integer> ll = new LinkedList<>();
        ll.add(0);
        ll.add(0);

        LinkedList<Double> lld = new LinkedList<>();
        lld.add(0.0);
        lld.add(0.0);

        String unit = "";

        if ("1".equals(queryMode)) {//按年份统计查询
            onceConnectList = callStatisticsService.getOnceConnectedAndNotByYear(callStatistics);
            multiConnectList = callStatisticsService.getMultiConnectedAndNotByYear(callStatistics);
            unit = "月";
        }
        if ("2".equals(queryMode)) {//按月份统计查询
            onceConnectList = callStatisticsService.getOnceConnectedAndNotByMonth(callStatistics);
            multiConnectList = callStatisticsService.getMultiConnectedAndNotByMonth(callStatistics);
            unit = "日";
        }
        for (CallStatistics cs : onceConnectList) {
        	ReportingUtils.formatData(onceHashMap, ll, cs);
        }

        for (CallStatistics cs : multiConnectList) {
        	ReportingUtils.formatData(multiHashMap, ll, cs);
        }

        //遍历一次接通数和多次接通数，计算接通率
        Set<String> allKey = new HashSet<>();
        allKey.addAll(onceHashMap.keySet());
        allKey.addAll(multiHashMap.keySet());
        for (String key : allKey) {
            LinkedList<Double> clone = (LinkedList<Double>) lld.clone();
            if (onceHashMap.containsKey(key)) {
            	ReportingUtils.calculateRate(onceHashMap.get(key), clone, 0, 0);
            }
            if (multiHashMap.containsKey(key)) {
            	ReportingUtils.calculateRate(multiHashMap.get(key), clone, 1, 0);
            }
            hashMap.put(key, clone);
        }

        //修改key的格式，并按照key排序
        Map<String, LinkedList<Double>> map = new LinkedHashMap<>();
        Set<String> keySet = hashMap.keySet();
        String[] keys = keySet.toArray(new String[keySet.size()]);
        Arrays.parallelSort(keys);
        for (String key : keys) {
            LinkedList<Double> rates = hashMap.get(key);
            map.put(key.substring(key.lastIndexOf("-") + 1) + unit, rates);
        }

        return ChartResult.success(map);

    }
    

    /**
     * 时间段接通率
     * 
     * @param projectId       项目id
     * @param ruleId          规则id
     * @param queryMode       查询方式
     * @param date            日期
     * @return
     */
    @GetMapping("/connectedAndNotRate")
    public ChartResult connectedAndNotRate(@RequestParam(required = false) String projectId,
                                           @RequestParam(required = false) String ruleId,
                                           @RequestParam(required = false) String queryMode,
                                           @RequestParam(required = false) String date) {

        List<CallStatistics> list = new ArrayList<>();
        Map<String, LinkedList<Integer>> hashMap = new LinkedHashMap<>();
        Map<String, LinkedList<Double>> rateMap = new LinkedHashMap<>();
        CallStatistics callStatistics = new CallStatistics();
        callStatistics.setProjectId(projectId);
        callStatistics.setRuleId(ruleId);
        callStatistics.setDay(date);
        LinkedList<Integer> ll = new LinkedList<>();
        ll.add(0);
        ll.add(0);
        LinkedList<Double> lld = new LinkedList<>();
        lld.add(0.0);
        lld.add(0.0);

        String unit = "";
        if ("1".equals(queryMode)) {//按年份统计查询
            list = callStatisticsService.getConnectedAndNotCntByYear(callStatistics);
            unit = "月";
        } else if ("2".equals(queryMode)) {//按月份统计查询
            list = callStatisticsService.getConnectedAndNotCntByMonth(callStatistics);
            unit = "日";
        } else if ("3".equals(queryMode)) {//按日统计查询
            list = callStatisticsService.getConnectedAndNotCntByDay(callStatistics);
            unit = ":00";
        }
        for (CallStatistics cs : list) {
        	ReportingUtils.formatData(hashMap, ll, cs);
        }

        //遍历一次接通数和多次接通数，计算接通率
        Set<String> keySet = hashMap.keySet();
        for (String key : keySet) {
            LinkedList<Double> clone = (LinkedList<Double>) lld.clone();
            LinkedList<Integer> cntList = hashMap.get(key);
            ReportingUtils.calculateRate(cntList, clone, 0, 0);
            ReportingUtils.calculateRate(cntList, clone, 1, 1);
            rateMap.put(key, clone);
        }

        //修改key的格式，并按照key排序
        Map<String, LinkedList<Double>> map = new LinkedHashMap<>();
        Set<String> rateKeys = rateMap.keySet();
        String[] keys = rateKeys.toArray(new String[rateKeys.size()]);
        Arrays.parallelSort(keys);
        for (String key : keys) {
            LinkedList<Double> rates = rateMap.get(key);
            map.put(key.substring(key.lastIndexOf("-") + 1) + unit, rates);
        }
        return ChartResult.success(map);

    }
    
    /**
     * 接通与未接通数(接通率)
     * 
     * @return
     *  
     */
    @PostMapping("/connectedAndNotCnt")
    public ChartResult connectedAndNotCnt(@RequestBody CallStatisticsPage page
    ) {
        List<CallStatistics> list = new ArrayList<>();
        Map<String, LinkedList<Integer>> hashMap = new LinkedHashMap<>();

        if (page.getStatisticalDimension().equals("year")) {//按年份统计查询
            list = callStatisticsService.getConnectedAndNotCntByYear(page);
        }
        if (page.getStatisticalDimension().equals("month")) {//按月份统计查询
            list = callStatisticsService.getConnectedAndNotCntByMonth(page);
        }
        if (page.getStatisticalDimension().equals("week")) {//按周统计查询
            list = callStatisticsService.getConnectedAndNotCntByWeek(page);
        }
        if (page.getStatisticalDimension().equals("day")) {//按日统计查询
            list = callStatisticsService.getConnectedAndNotCntByDay(page);
        }
        LinkedList<Integer> ll = new LinkedList<>();
        ll.add(0);
        ll.add(0);
        for (CallStatistics cs : list) {
        	ReportingUtils.formatData(hashMap, ll, cs);
        }
        return ChartResult.success(hashMap);
    }  
    
    /**
     * 呼叫差错统计条形图
     * 
     * @param callErrorStatistics
     * @return
     *
     */
    @PostMapping("/callErrorLine")
    public ChartResult callCntLine(@RequestBody CallErrorStatistics callErrorStatistics) {
        Map<String, LinkedList<Map<String, Object>>> map = new LinkedHashMap<>();
        callStatisticsService.getCallErrorLineStatistics(map, callErrorStatistics);
        return ChartResult.success(map);
    }
    
    /**
     * 呼叫差错统计条形图明细，通过点击图片了解详情
     *
     * @param callErrorStatistics
     * @param current
     * @param pageSize
     * @return
     *
     */
    @PostMapping("/callErrorStatisticsDetail")
    public PageResult callErrorStatisticsDetail(@RequestBody CallErrorStatistics callErrorStatistics, @RequestParam int current, @RequestParam int pageSize) throws ParseException {
        log.info("current=" + current + ";pageSize=" + pageSize);
        Page page = recordInfoService.getCallErrorStatisticsDetail(callErrorStatistics, current, pageSize);
        log.info("Page Total == " + page.getTotal());
        return PageResult.success(page, TableHeadsFactory.createErrorDetailHead());
    }
    
    /**
     * 呼叫量统计
     *
     * @param projectId       项目Id
     * @param ruleId          角色Id
     * @param recordStartTime 开始时间
     * @param recordEndTime   结束时间
     * @param pageParam       页面参数
     * @return 结果
     */
    @GetMapping("/callCnt")
    public PageResult callCnt(@RequestParam(required = false) String projectId,
                              @RequestParam(required = false) String ruleId,
                              @RequestParam(required = false) String recordStartTime,
                              @RequestParam(required = false) String recordEndTime,
                              PageParam pageParam) {
        CallStatistics callStatistics = new CallStatistics();
        callStatistics.setProjectId(projectId);
        callStatistics.setRuleId(ruleId);
        callStatistics.setMonth(recordStartTime);
        callStatistics.setDay(recordEndTime);

        log.info("current=" + pageParam.getCurrent() + ";pageSize=" + pageParam.getPageSize());
        Page page = callStatisticsService.getCallCntByPage(callStatistics, pageParam);
        log.info("Page Total == " + page.getTotal());
        return PageResult.success(page, TableHeadsFactory.createCallCntHead());
    }

    /**
     * 交易成功率统计
     * 
     * @param tradeStatistics
     * @param current
     * @param pageSize
     * @return 结果
     * 
     */
    @PostMapping("/tradeStatistics")
    public PageResult tradeStatistics(@RequestBody TradeStatistics tradeStatistics, @RequestParam int current, @RequestParam int pageSize) throws ParseException {
//        log.info("current=" + current + ";pageSize=" + pageSize);
        Page page = recordInfoService.getTradeStatistics(tradeStatistics, current, pageSize);
//        log.info("Page Total == " + page.getTotal());
        return PageResult.success(page, TableHeadsFactory.createTradeStatistics());
    }
    
    /**
     * 交易成功率详情
     * 
     * @param recordInfoPage
     * @param current
     * @param pageSize
     * @return 结果
     * 
     */
    @PostMapping("/tradeStatisticsDetail")
    public PageResult tradeStatisticsDetail(@RequestBody RecordInfoPage recordInfoPage, @RequestParam int current, @RequestParam int pageSize) {
        log.info("current=" + current + ";pageSize=" + pageSize);
        Page page = new Page();
        try{
            page = recordInfoService.selectPageByCondition(recordInfoPage, current, pageSize);
        } catch(Exception e){
            e.printStackTrace();
        }
        return PageResult.success("success", page, TableHeadsFactory.createRecordInfoHead());
    }
      
    /**
     * 时间段交易成功率统计
     *
     * @param tradeStatistics  
     * @param current
     * @param pageSize
     * @return
     *
     */
    @PostMapping("/timeSectionTradeStatistics")
    public PageResult timeSectionTradeStatistics(@RequestBody TradeStatistics tradeStatistics, @RequestParam int current, @RequestParam int pageSize) {
        log.info("current=" + current + ";pageSize=" + pageSize);
        Page page = new Page();
        try {
            page = recordInfoService.getTimeSectionTradeStatistics(tradeStatistics, current, pageSize);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.info("Page Total == " + page.getTotal());
        return PageResult.success(page, TableHeadsFactory.createTradeStatistics());
    }
     
    /**
     * 节点通过率统计
     *
     * @param nodePassRatePage 
     * @param current
     * @param pageSize
     * @return
     *
     */
    @PostMapping("/nodePassRate")
    public PageResult nodePassRate(@RequestBody NodePassRatePage nodePassRatePage, @RequestParam int current, @RequestParam int pageSize) throws ParseException {
        Map<String, Object> resMap = recordInfoService.getNodePassRateResult(nodePassRatePage, current, pageSize);
        return ReportingUtils.getPageResult(resMap);
    }

    
    /**
     * 节点通过率详情
     *
     * @param nodePassRatePage
     * @param current
     * @param pageSize
     * @return
     *
     */
    @PostMapping("/nodePassRateDetail")
    public PageResult nodePassRateDetail(@RequestBody NodePassRatePage nodePassRatePage, @RequestParam int current, @RequestParam int pageSize) throws ParseException {
        log.info("current=" + current + ";pageSize=" + pageSize);
        Page page = recordInfoService.selectPageByNodePassCondition(nodePassRatePage, current, pageSize);
        return PageResult.success("success", page, TableHeadsFactory.createRecordInfoHead());
    }

    /**
     *  挂机节点统计
     *
     * @param endNodeNamePage
     * @param current
     * @param pageSize
     * @return
     *
     */
    @PostMapping("/hangupNodeNameStatistical")
    public PageResult hangupNodeNameStatistical(@RequestBody EndNodeNamePage endNodeNamePage, @RequestParam int current, @RequestParam int pageSize) throws ParseException {
        Map<String, Object> resMap = recordInfoService.getHangupNodeNameStatistical(endNodeNamePage, current, pageSize);
        return ReportingUtils.getPageResult(resMap);
    }
    
    /**
     * 挂机节点详情
     *
     * @param endNodeNamePage
     * @param current
     * @param pageSize
     * @return
     *
     */
    @PostMapping("/hangupNodeDetail")
    public PageResult hangupNodeDetail(@RequestBody EndNodeNamePage endNodeNamePage, @RequestParam int current, @RequestParam int pageSize) throws ParseException {
        log.info("current=" + current + ";pageSize=" + pageSize);
        Page page = recordInfoService.selectPageByHangupNodeCondition(endNodeNamePage, current, pageSize);
        return PageResult.success("success", page, TableHeadsFactory.createRecordInfoHead());
    }
    
    /**
     * 场景通过明细
     *
     * @param sceneThroughPage
     * @param current
     * @param pageSize
     * @return
     *
     */
    @PostMapping("/sceneThroughDeatil")
    public PageResult sceneThroughDeatil(@RequestBody SceneThroughPage sceneThroughPage, @RequestParam int current, @RequestParam int pageSize) throws ParseException {
        Map<String, Object> resMap = recordInfoService.getSceneThroughDetail(sceneThroughPage, current, pageSize);
        return ReportingUtils.getPageResult(resMap);
    }
    
    /**
     * 新需求接通率查询
     *
     * @param connectRate   接通率对象
     * @param current       
     * @param pageSize
     * @return
     *
     */
    @PostMapping("/newConnectionRate")
    public PageResult<?> newConnectionRate(@RequestBody ConnectRate connectRate, @RequestParam int current, @RequestParam int pageSize ) throws ParseException {
    	Map<String, Object> resMap = recordInfoService.getConnectionRate(connectRate, current, pageSize);
        return ReportingUtils.getPageResult(resMap);
    }
    
    /**
     * 新需求接通率明细查询
     *
     * @param connectRate
     * @param current
     * @param pageSize
     * @return
     *
     */
    @PostMapping("/newConnectionRateDetail")
    public PageResult<?> newConnectionRateDetail(@RequestBody ConnectRate connectRate, @RequestParam int current, @RequestParam int pageSize ) throws ParseException {
    	log.info("current=" + current + ";pageSize=" + pageSize);
        Page<?> page = recordInfoService.selectPageConnectionRateCondition(connectRate, current, pageSize);
        return PageResult.success("success", page, TableHeadsFactory.createRecordInfoHead());
    }
    
    /**
     * 新增挂机率查询
     *
     * @param endNodeNamePage   挂机节点对象
     * @param current       
     * @param pageSize
     * @return
     *
     */
    @PostMapping("/hangupRate")
    public PageResult<?> HangupRate(@RequestBody EndNodeNamePage endNodeNamePage, @RequestParam int current, @RequestParam int pageSize ) throws ParseException {
    	Map<String, Object> resMap = recordInfoService.getHangupRate(endNodeNamePage, current, pageSize);
        return ReportingUtils.getPageResult(resMap);
    }
    
    /**
     * 新增挂机率明细查询
     *
     * @param endNodeNamePage
     * @param current
     * @param pageSize
     * @return
     *
     */
    @PostMapping("/hangupRateDetail")
    public PageResult<?> hangupRateDetail(@RequestBody EndNodeNamePage endNodeNamePage, @RequestParam int current, @RequestParam int pageSize ) throws ParseException {
    	log.info("current=" + current + ";pageSize=" + pageSize);
        Page<?> page = recordInfoService.selectPageHangupRateCondition(endNodeNamePage, current, pageSize);
        return PageResult.success("success", page, TableHeadsFactory.createRecordInfoHead());
    }
    
}
