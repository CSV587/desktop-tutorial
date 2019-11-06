package com.hy.task;

import com.hy.connector.constant.CallBackTaskConstant;
import com.hy.entities.CallInfoStatistics;
import com.hy.entity.paper.Paper;
import com.hy.entity.paper.Papers;
import com.hy.entity.paper.Save;
import com.hy.entity.paper.Saves;
import com.hy.entity.visithistory.VisitHistory;
import com.hy.entity.visithistory.VisitHistoryRslt;
import com.hy.entity.visithistory.VisitHistoryRslts;
import com.hy.entity.visithistory.VisitHistorys;
import com.hy.error.ErrorUtil;
import com.hy.reporting.callback.entities.CallBackEntities;
import com.hy.reporting.callback.service.CallBackService;
import com.hy.reporting.callcyclemanage.page.CallCyclePage;
import com.hy.reporting.callcyclemanage.service.CallCycleService;
import com.hy.util.BusinessType;
import com.hy.util.CustomXmlUtil;
import com.hy.util.HttpRequestUtils;
import com.hy.util.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-25
 * user: lxg
 * package_name: com.hy.task
 */
@Slf4j
@Service
@PropertySource(value = "classpath:scheduler.properties", encoding = "UTF-8")
public class CallBackVisitTask {
    /**
     * .
     * 回访数据service 服务
     */
    private final CallBackService callBackService;

    /**
     * .
     * CallCycleService
     */
    private final CallCycleService callCycleService;

    /**
     * .
     *
     * @param service  CallBackService
     * @param service1 callCycleService
     */
    public CallBackVisitTask(final CallBackService service, CallCycleService service1) {
        this.callBackService = service;
        this.callCycleService = service1;
    }

    /**
     * .
     * 定时回写回访问卷数据
     */
    @Scheduled(cron = "${writeBack.cron:0/5 * * * * ?}")
    public void writeBackTask() {
        List<CallInfoStatistics> callInfoStatisticsList
            = callBackService.queryAllCallBackData();
        try {
            dealPaper(callInfoStatisticsList);
        } catch (Exception e) {
            log.error("处理问卷报错:{}", ErrorUtil.getStackTrace(e));
        }

    }

    /**
     * .
     * 处理批量数据回传
     *
     * @param callInfoStatisticsList 呼叫实体
     * @throws Exception exception
     */
    private void dealPaper(
        final List<CallInfoStatistics> callInfoStatisticsList)
        throws Exception {
        Map<String, Map<CallInfoStatistics, Paper>> dataMap = new HashMap<>();
        List<Paper> paperList = new ArrayList<>();
        List<CallInfoStatistics> tmpList = new ArrayList<>();
        for (CallInfoStatistics callInfoStatistics : callInfoStatisticsList) {
            if (callInfoStatistics.getIsConnect().equals("connect")) {
                Paper paper = callBackService.parseToPaper(callInfoStatistics);
                if (paper != null) {
                    paperList.add(paper);
                    Map<CallInfoStatistics, Paper> keyMap = new HashMap<>();
                    String contNo = callInfoStatistics.getColumn5();
                    keyMap.put(callInfoStatistics, paper);
                    dataMap.put(contNo, keyMap);
                }
            } else {
                tmpList.add(callInfoStatistics);
            }
        }
        List<Save> saveList = sendPaper(paperList);
        List<VisitHistory> visitHistoryList = new ArrayList<>();
        if (saveList != null) {
            for (Save save : saveList) {
                if (save.getStatus().equals("1")) {
                    String contNo = save.getContNo();
                    String qid = save.getQuestionnaireID();
                    Map<CallInfoStatistics, Paper> dataItem
                        = dataMap.get(contNo);
                    for (CallInfoStatistics callInfoStatistics
                        : dataItem.keySet()) {
                        Paper paper = dataItem.get(callInfoStatistics);
                        VisitHistory visitHistory =
                            callBackService.parseToVisitHistory(
                                callInfoStatistics, paper);
                        if (visitHistory != null) {
                            visitHistory.setQuestionnaireID(qid);
                            visitHistoryList.add(visitHistory);
                        }
                    }
                } else {
                    log.warn("保单号:{}问卷回传失败，显示note:{}",
                        save.getContNo(), save.getNotes());
                }
            }
        }
        for (CallInfoStatistics callInfoStatistics : tmpList) {
            VisitHistory visitHistory =
                callBackService.parseToVisitHistory(
                    callInfoStatistics, null);
            if (visitHistory != null) {
                visitHistoryList.add(visitHistory);
            }
        }
        sendVisitHistory(visitHistoryList);
    }


    /**
     * .
     * 发送数据
     *
     * @param paperList paperList
     * @return Save集合
     * @throws Exception Exception
     */
    private List<Save> sendPaper(final List<Paper> paperList) throws Exception {
        if (paperList == null
            || paperList.isEmpty()) {
            log.info("回访问卷请求数据为空");
            return new ArrayList<>();
        }
        Papers papers = new Papers();
        papers.setPaperList(paperList);
        String uuid = UUID.randomUUID().toString();
        String xmlStr = CustomXmlUtil.pack(papers,
            BusinessType.Papers,
            MessageType.request,
            uuid);
        String result = HttpRequestUtils.httpPost(
            CallBackTaskConstant.getCallBackUrl(), xmlStr);
        log.debug("回访问卷回执报文:\n{}", result);
        Map<String, Saves> savesMap
            = CustomXmlUtil.unpack(result, Saves.class);
        Saves saves = null;
        String traceId = null;
        for (String key : savesMap.keySet()) {
            traceId = key;
            saves = savesMap.get(key);
        }
        if (uuid.equals(traceId) && saves != null) {
            return saves.getSaveList();
        } else {
            return new ArrayList<>();
        }
    }


    /**
     * .
     * 处理异常数据
     */
    @Scheduled(cron = "${invalidTask.cron:0 0 1 * * ?}")
    public void invalidTask() {
        CallCyclePage callCyclePage = new CallCyclePage();
        callCyclePage.setTaskState(0);
        List<CallCyclePage> pageList = callCycleService.query(callCyclePage);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (CallCyclePage page : pageList) {
            try {
                Date date = sdf.parse(page.getNewEndDate());
                Date curDate = new Date();
                if (0 > date.compareTo(curDate)) {
                    List<VisitHistory> visitHistoryList = new ArrayList<>();
                    List<CallBackEntities> callBackEntitiesList
                        = callBackService.queryInvalidData(page);
                    for (CallBackEntities callBackEntities : callBackEntitiesList) {
                        VisitHistory visitHistory
                            = callBackService.getInvalidVisitHistory(callBackEntities);
                        visitHistoryList.add(visitHistory);
                    }
                    sendVisitHistory(visitHistoryList);
                    callCycleService.invalidCallCycle(page.getId());
                    log.debug("结束任务 任务编号{},推送编号{},结束时间为{}",
                        page.getPushTaskId(), page.getReturnTaskId(), page.getEndDate());
                } else {
                    log.debug("未结束任务 任务编号{},推送编号{},结束时间为{}",
                        page.getPushTaskId(), page.getReturnTaskId(), page.getEndDate());
                }
            } catch (Exception e) {
                log.error("异常数据回写异常:{}", ErrorUtil.getStackTrace(e));
            }
        }
    }

    /**
     * .
     * 发送历史回传文件
     *
     * @param visitHistoryList 回访历史集合
     * @throws Exception Exception
     */
    private void sendVisitHistory(
        final List<VisitHistory> visitHistoryList)
        throws Exception {
        if (visitHistoryList == null
            || visitHistoryList.isEmpty()) {
            log.info("回访历史请求数为空");
            return;
        }
        VisitHistorys visitHistorys = new VisitHistorys();
        visitHistorys.setVisitHistoryList(visitHistoryList);
        String uuid = UUID.randomUUID().toString();
        String xmlStr = CustomXmlUtil.pack(visitHistorys,
            BusinessType.VisitHistory,
            MessageType.request,
            uuid);
        String result = HttpRequestUtils.httpPost(
            CallBackTaskConstant.getCallBackHistoryUrl(), xmlStr);
        log.debug("回访历史回执报文:\n{}", result);
        Map<String, VisitHistoryRslts> visitHistoryRsltsMap
            = CustomXmlUtil.unpack(result, VisitHistoryRslts.class);
        VisitHistoryRslts visitHistoryRslts = null;
        String traceId = null;
        for (Iterator<String> iterator = visitHistoryRsltsMap.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            traceId = key;
            visitHistoryRslts = visitHistoryRsltsMap.get(key);
        }
        if (uuid.equals(traceId) && visitHistoryRslts != null) {
            List<VisitHistoryRslt> visitHistoryRsltList
                = visitHistoryRslts.getVisitHistoryRsltList();
            List<String> items = new ArrayList<>();
            for (VisitHistoryRslt visitHistoryRslt : visitHistoryRsltList) {
                if (visitHistoryRslt.getStatus().equals("1")) {
                    items.add(visitHistoryRslt.getContNo());
                }
            }
            int count = callBackService.updateBatchCallBackPaper(items);
            log.debug("回写{}条成功", count);
        } else {
            log.debug("返回traceId与发送id不一致:原始id:{}\n返回id:{}",
                uuid,
                traceId);
        }
    }
}
