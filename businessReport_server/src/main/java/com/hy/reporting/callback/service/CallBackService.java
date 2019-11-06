package com.hy.reporting.callback.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.base.excel.ExcelAnnotation;
import com.hy.base.excel.ExcelEnumAnnotation;
import com.hy.base.utils.FieldUtil;
import com.hy.config.NoticeEvent;
import com.hy.constant.CaseConstant;
import com.hy.entities.CallInfoStatistics;
import com.hy.entity.filenotice.FileNotice;
import com.hy.entity.filenotice.FileNoticeResp;
import com.hy.entity.filenotice.FileNoticeResps;
import com.hy.entity.filenotice.FileNotices;
import com.hy.entity.paper.Answers;
import com.hy.entity.paper.Paper;
import com.hy.entity.paper.Papers;
import com.hy.entity.paper.Questions;
import com.hy.entity.paper.Save;
import com.hy.entity.paper.Saves;
import com.hy.entity.record.RecordEntity;
import com.hy.entity.visithistory.CallOutInfo;
import com.hy.entity.visithistory.CallOutResult;
import com.hy.entity.visithistory.VisitHistory;
import com.hy.entity.visithistory.VisitHistoryRslt;
import com.hy.entity.visithistory.VisitHistoryRslts;
import com.hy.entity.visithistory.VisitHistorys;
import com.hy.error.ErrorUtil;
import com.hy.mapper.oracle.CallBackEntitiesMapper;
import com.hy.mapper.oracle.CallInfoStatisticsMapper;
import com.hy.mapper.oracle.PaperMapper;
import com.hy.reporting.callback.dao.CallBackDao;
import com.hy.reporting.callback.entities.CallBackEntities;
import com.hy.reporting.callback.page.CallBackPage;
import com.hy.reporting.callcyclemanage.page.CallCyclePage;
import com.hy.reporting.callcyclemanage.service.CallCycleService;
import com.hy.util.BusinessType;
import com.hy.util.CustomXmlUtil;
import com.hy.util.MessageType;
import com.hy.util.MyFileUtil;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-16
 * user: lxg
 * package_name: com.hy.reporting.callback
 */
@Slf4j
@Service
@PropertySource(value = "classpath:scheduler.properties", encoding = "UTF-8")
public class CallBackService {

    /**
     * .
     * oracle sql语句 in 后的最大数据量
     */
    private static final int SQL_IN_DATA_MAX_SIZE = 999;

    /**
     * .
     * 批量写入大小
     */
    private static final int WRITE_SIZE = 1000;

    /**
     * .
     * CallInfoStatisticsMapper
     */
    private final CallInfoStatisticsMapper callInfoStatisticsMapper;

    /**
     * .
     * callBackEntitiesMapper
     */
    private final CallBackEntitiesMapper callBackEntitiesMapper;

    /**
     * .
     * CallCycleService
     */
    private final CallCycleService callCycleService;

    /**
     * .
     * PaperMapper
     */
    private final PaperMapper paperMapper;

    private final CallBackDao callBackDao;

    /**
     * .
     * ApplicationContext
     */
    private final ApplicationContext publisher;

    /**
     * .
     * ftp服务Ip
     */
    @Value("${ftpId}")
    private String ftpId;

    /**
     * .
     * ftp服务用户名
     */
    @Value("${ftpUser}")
    private String ftpUserName;

    /**
     * .
     * ftp服务密码
     */
    @Value("${ftpPwd}")
    private String ftpPwd;

    /**
     * .
     * ftp使用方式
     */
    @Value("${ftpFlag}")
    private String ftpFlag;

    /**
     * .
     * 秘钥文件路径
     */
    @Value("${pukPath:ccrobot_rsa}")
    private String pukPath;

    /**
     * .
     * 临时路径
     */
    @Value("${tmpPath:/tmp}")
    private String tmpPath;

    /**
     * .
     * 构造函数
     *
     * @param mapper1     CallInfoStatisticsMapper
     * @param mapper2     CallBackEntitiesMapper
     * @param service     callCycleService
     * @param mapper3     PaperMapper
     * @param callBackDao
     * @param context     上下文
     */
    public CallBackService(final CallInfoStatisticsMapper mapper1,
                           final CallBackEntitiesMapper mapper2,
                           final CallCycleService service,
                           final PaperMapper mapper3, CallBackDao callBackDao, final ApplicationContext context) {
        this.callInfoStatisticsMapper = mapper1;
        this.callBackEntitiesMapper = mapper2;
        this.callCycleService = service;
        this.paperMapper = mapper3;
        this.callBackDao = callBackDao;
        this.publisher = context;
    }

    /**
     * .
     * 分页查询
     *
     * @param callBackPage CallBackPage
     * @return 结果
     */
    public Page query(final CallBackPage callBackPage) {
        PageHelper.startPage(callBackPage.getCurrent(),
            callBackPage.getPageSize());
        if (callBackPage.getQueAnswer() != null) {
            callBackPage.setQueAnswer(callBackPage
                .getQueAnswer()
                .replace("，", ","));
        }
        List<CallBackPage> pages
            = callInfoStatisticsMapper
            .queryCallBack(callBackPage);
        return (Page) pages;
    }

    /**
     * .
     * 解析文件下载请求
     *
     * @param xmlStr xml字符串
     * @return 结果
     */
    public String parseFile(final String xmlStr) {
        FileNoticeResps fileNoticeResps = new FileNoticeResps();
        List<FileNoticeResp> fileNoticeRespList = new ArrayList<>();
        Map<String, FileNotices> fileNoticesMap;
        try {
            fileNoticesMap = CustomXmlUtil.unpack(xmlStr, FileNotices.class);
        } catch (Exception e) {
            log.error("解析异常:{},ErrorMsg:{}",
                xmlStr, ErrorUtil.getStackTrace(e));
            return "解析异常";
        }
        FileNotices fileNotices = null;
        String uuid = null;
        for (String key : fileNoticesMap.keySet()) {
            uuid = key;
            fileNotices = fileNoticesMap.get(key);
        }
        NoticeEvent noticeEvent = new NoticeEvent(this, xmlStr);
        publisher.publishEvent(noticeEvent);
        log.debug("消息发布");
        if (fileNotices != null) {
            List<FileNotice> fileNoticeList
                = fileNotices.getNoticeReq();
            for (FileNotice fileNotice : fileNoticeList) {
                FileNoticeResp resp = new FileNoticeResp();
                String fileName = fileNotice.getFileName();
                resp.setFileName(fileName);
                resp.setFileReceiveState("0");
                fileNoticeRespList.add(resp);
            }
            fileNoticeResps.setFileNoticeRespList(fileNoticeRespList);
            try {
                return CustomXmlUtil.pack(fileNoticeResps,
                    BusinessType.Notice,
                    MessageType.response,
                    uuid);
            } catch (Exception e) {
                log.error("封装异常:{},ErrorMsg:{}",
                    fileNoticeResps, ErrorUtil.getStackTrace(e));
                return "封装异常";
            }
        } else {
            return "";
        }
    }


//    /**
//     * .
//     * 解析文件下载请求
//     *
//     * @param event 通知事件
//     */
//    @EventListener
//    public void saveFile(final NoticeEvent event) {
//        Map<String, FileNotices> fileNoticesMap;
//        String xmlStr = event.getMessage();
//        try {
//            fileNoticesMap = CustomXmlUtil.unpack(xmlStr, FileNotices.class);
//        } catch (Exception e) {
//            log.error("解析异常:{},ErrorMsg:{}",
//                xmlStr, ErrorUtil.getStackTrace(e));
//            return;
//        }
//        FileNotices fileNotices = null;
//        for (String key : fileNoticesMap.keySet()) {
//            fileNotices = fileNoticesMap.get(key);
//        }
//        if (fileNotices != null) {
//            try (SSHClient ssh = new SSHClient()) {
//                ssh.addHostKeyVerifier(new PromiscuousVerifier());
//                ssh.loadKnownHosts();
//                ssh.connect(ftpId);
//                if (ftpFlag.equals("psw")) {
//                    ssh.authPassword(ftpUserName, ftpPwd);
//                } else {
//                    ssh.authPublickey(ftpUserName, pukPath);
//                }
//                try (SFTPClient sftp = ssh.newSFTPClient()) {
//                    List<FileNotice> fileNoticeList
//                        = fileNotices.getNoticeReq();
//                    for (FileNotice fileNotice : fileNoticeList) {
//                        dealSingleFile(fileNotice, sftp);
//                    }
//                }
//            } catch (IOException e) {
//                log.error("ssh异常:{}", ErrorUtil.getStackTrace(e));
//            }
//        }
//    }

    /**
     * .
     * 测试录音文件上传接口解析
     *
     * @param xmlStr xml字符串
     * @return 结果
     * @throws Exception Exception
     */
    public String testParseFile(final String xmlStr) throws Exception {
        FileNoticeResps fileNoticeResps = new FileNoticeResps();
        List<FileNoticeResp> fileNoticeRespList = new ArrayList<>();
        Map<String, FileNotices> fileNoticesMap
            = CustomXmlUtil.unpack(xmlStr, FileNotices.class);
        FileNotices fileNotices = null;
        String uuid = null;
        for (String key : fileNoticesMap.keySet()) {
            uuid = key;
            fileNotices = fileNoticesMap.get(key);
        }
        if (fileNotices != null) {
            List<FileNotice> fileNoticeList = fileNotices.getNoticeReq();
            for (FileNotice fileNotice : fileNoticeList) {
                FileNoticeResp resp = testRecordSingleFile(fileNotice);
                fileNoticeRespList.add(resp);
            }
            fileNoticeResps.setFileNoticeRespList(fileNoticeRespList);
            return CustomXmlUtil.pack(fileNoticeResps,
                BusinessType.FileNotices,
                MessageType.response,
                uuid);
        } else {
            return "";
        }
    }

    /**
     * .
     * 处理单个文件
     *
     * @param fileNotice FileNotice对象
     * @return FileNoticeResp FileNoticeResp
     */
    private FileNoticeResp testRecordSingleFile(final FileNotice fileNotice) {
        FileNoticeResp resp = new FileNoticeResp();
        String fileName = fileNotice.getFileName();
        resp.setFileName(fileName);
        resp.setFileReceiveState("0");
        return resp;
    }

    /**
     * .
     * 处理单个文件
     *
     * @param fileNotice FileNotice对象
     * @param sftp       SFTPClient
     */
    @Transactional
    public void dealSingleFile(final FileNotice fileNotice,
                               final SFTPClient sftp) {
        String filePath = fileNotice.getFilePath();
        String fileName = fileNotice.getFileName();
        String md5Code = fileNotice.getFileMd5Code();
        String downPath = String.format("%s%s%s",
            filePath, File.separatorChar, fileName);
        File file = downLoadFtpFile(downPath, sftp);
        try {
            if (file != null && md5Code.equals(MyFileUtil.getFileMd5(file))) {
                List<String> lines = FileUtils.readLines(
                    file,
                    StandardCharsets.UTF_8
                );
                List<CallBackEntities> callBackEntitiesList = new ArrayList<>();
                for (String line : lines) {
                    CallBackEntities entities = JSONObject
                        .parseObject(
                            String.valueOf(line),
                            CallBackEntities.class
                        );
                    callBackEntitiesList.add(entities);
                    if (callBackEntitiesList.size() == WRITE_SIZE) {
                        this.save(callBackEntitiesList);
                        callCycleService.insertCallCycle(callBackEntitiesList);
                        callBackEntitiesList = new ArrayList<>();
                    }
                }
                this.save(callBackEntitiesList);
                callCycleService.insertCallCycle(callBackEntitiesList);
            } else {
                throw new IOException("文件Md5值异常");
            }
        } catch (Exception e) {
            log.error("{}", ErrorUtil.getStackTrace(e));
        }
    }

    /**
     * .
     * 满足条件全部查询
     *
     * @param callBackPage CallBackPage
     * @return 结果
     */
    public List<CallBackPage> queryAll(
        final CallBackPage callBackPage) {
        if (callBackPage.getQueAnswer() != null) {
            callBackPage.setQueAnswer(callBackPage
                .getQueAnswer()
                .replace("，", ","));
        }
        return callInfoStatisticsMapper.queryCallBack(callBackPage);
    }

    /**
     * .
     * 查询所有满足条件的数据
     *
     * @return 满足条件集合
     */
    @Transactional
    public List<CallBackEntities> queryAllPendingData() {
        int count = callBackEntitiesMapper.dealInvalidData();
        log.debug("处理无效数据：{}条", count);
        List<CallBackEntities> callBackEntitiesList
            = callBackEntitiesMapper.queryAllPendingData();
        List<String> ids = new ArrayList<>();
        for (CallBackEntities entities : callBackEntitiesList) {
            ids.add(entities.getId());
        }
        int end;
        for (int start = 0; start < ids.size(); start = end) {
            end = start + SQL_IN_DATA_MAX_SIZE;
            if (end > ids.size()) {
                end = ids.size();
            }
            List<String> itemIds = ids.subList(start, end);
            count = callBackEntitiesMapper.distributeData(itemIds);
            log.debug("{}条数据设置为分发状态", count);
        }
        return callBackEntitiesList;
    }

    /**
     * .
     * 保存业务数据
     *
     * @param entitiesList 数据集合
     */
    public void save(final List<CallBackEntities> entitiesList) {
        if (!entitiesList.isEmpty()) {
            callBackDao.insertRecordInfoList(entitiesList);
        }
        log.debug("插入成功{}条", entitiesList.size());
    }

    /**
     * .
     * 查询所有未回写录音数据
     *
     * @return 满足条件集合
     */
    public List<RecordEntity> queryAllCallBackRecordData() {
        return callInfoStatisticsMapper.queryAllCallBackRecord();
    }

    /**
     * .
     * 查询所有未回传数据
     *
     * @return 满足条件集合
     */
    public List<CallInfoStatistics> queryAllCallBackData() {
        return callInfoStatisticsMapper.queryAllCallBack();
    }

    /**
     * .
     * 解析数据得到VisitHistory结果
     *
     * @param callInfoStatistics 呼叫数据
     * @param paper              问卷编号
     * @return VisitHistory
     */
    public VisitHistory parseToVisitHistory(
        final CallInfoStatistics callInfoStatistics,
        final Paper paper) {
        if (callInfoStatistics.getColumn25() == null) {
            callInfoStatisticsMapper.invalidRecordList(callInfoStatistics);
            return null;
        }
        VisitHistory visitHistory
            = callBackEntitiesMapper
            .findByVisitValue(callInfoStatistics.getColumn25());
        if (visitHistory == null) {
            callInfoStatisticsMapper.invalidRecordList(callInfoStatistics);
            return null;
        }
        SimpleDateFormat format =
            new SimpleDateFormat("yyyyMMddHHmmss");
        if (paper != null && paper.isAllAccess()) {
            visitHistory.setQstnaireAnswerSum("1");
        }
        if (paper != null && paper.isAllAnswer()) {
            visitHistory.setQstnaireFinishFlg("1");
        }
        visitHistory.setHandleTm(
            format.format(callInfoStatistics.getRecordStartTime()));

        String callResult = callInfoStatistics.getColumn15();

        if (callInfoStatistics.getIsConnect().equals("unconnect")) {
            //未联系--投保人转人工;未接通--其他结案、多次回访不成功
            String callCountStr = visitHistory.getCallCount();
            int callCount = Integer.parseInt(callCountStr);
            String currentCountStr = callInfoStatistics.getColumn13();
            int currentCount = Integer.parseInt(currentCountStr);
            if (currentCount == callCount) {
                callResult = "未联系到客户";
            } else {
                callResult = "智能外呼-继续回访";
            }
        } else {
            String callCountStr = visitHistory.getCallCount();
            int callCount = Integer.parseInt(callCountStr);
            String currentCountStr = callInfoStatistics.getColumn13();
            int currentCount = Integer.parseInt(currentCountStr);
            int answerCount = callInfoStatisticsMapper.maxCountByRecordId(callInfoStatistics.getColumn25());
            if (currentCount < callCount
                && answerCount == 0
                && (callResult == null
                || (callResult.equals("未联系到客户")
                || callResult.equals("约定下次回访")))) {
                callResult = "智能外呼-继续回访";
            } else if (callResult == null) {
                callResult = "未联系到客户";
            }
            if (answerCount == 1) {
                log.debug("[{}]最大呼叫次数已打满", callInfoStatistics.getColumn25());
            }
        }
        if (callResult.equals("预警-安抚")) {
            callResult = "预警-转人工";
        }
        //人工跟进、预警转人工、预警安抚
        if (callResult.equals("智能外呼-转人工")
            || callResult.equals("预警-转人工")) {
            visitHistory.setIsTransToSeater("1");
        }
        CallOutResult callOutResult;
        callOutResult = new CallOutResult();
        Map<String, String> caseMaps = CaseConstant.getCaseMaps();
        String result = caseMaps.get(callResult);
        String closeFlag;
        if (result == null) {
            log.error("呼叫结果[{}]无对应数据", callResult);
            return null;
        }
        callOutResult.setCalloutRslt(callResult);
        String[] items = result.split(";");
        callOutResult.setCallResultId(items[0]);
        closeFlag = items[1];

        if (closeFlag.equals("1")) {
            closeList(visitHistory.getId(), "2");
        } else {
            closeList(visitHistory.getId(), "1");
        }

        List<CallOutResult> callOutResults = new ArrayList<>();
        callOutResults.add(callOutResult);
        visitHistory.setCallOutResultList(callOutResults);

        visitHistory.setCloseStatus(closeFlag);

        CallOutInfo callOutInfo = new CallOutInfo();
        callOutInfo.setCallOutNo(callInfoStatistics.getCallNumber());
        if (callInfoStatistics.getIsConnect().equals("connect")) {
            callOutInfo.setRecordId(callInfoStatistics.getUuid());
        }
        List<CallOutInfo> callOutInfos = new ArrayList<>();
        callOutInfos.add(callOutInfo);
        visitHistory.setCallOutInfoList(callOutInfos);
        return visitHistory;
    }

    /**
     * .
     * 解析数据得到VisitHistory结果
     *
     * @param callBackEntities 业务实体
     * @return VisitHistory
     */
    public VisitHistory getInvalidVisitHistory(
        final CallBackEntities callBackEntities) {
        VisitHistory visitHistory = new VisitHistory();
        visitHistory.setId(callBackEntities.getId());
        visitHistory.setContNo(callBackEntities.getPolicyNo());
        visitHistory.setCalloutType1(callBackEntities.getCallOutType1());
        visitHistory.setCalloutType2(callBackEntities.getCallOutType2());
        visitHistory.setCallTaskId(callBackEntities.getCallTask_fisc_id());
        visitHistory.setAppCustName(callBackEntities.getApplicantName());
        SimpleDateFormat format =
            new SimpleDateFormat("yyyyMMddHHmmss");
        visitHistory.setIsTransToSeater("1");
        visitHistory.setHandleTm(
            format.format(new Date()));

        String closeFlag = "1";

        CallOutResult callOutResult;
        callOutResult = new CallOutResult();
        callOutResult.setCalloutRslt("智能外呼-转人工");
        callOutResult.setCallResultId("1071");
        closeList(visitHistory.getId(), "2");
        List<CallOutResult> callOutResults = new ArrayList<>();
        callOutResults.add(callOutResult);
        visitHistory.setCallOutResultList(callOutResults);
        visitHistory.setCloseStatus(closeFlag);
        return visitHistory;
    }


    /**
     * .
     * 关闭名单
     *
     * @param id   唯一标识
     * @param flag 标记
     */
    private void closeList(
        final String id, final String flag) {
        callBackEntitiesMapper.closePolicyNo(id, flag);
    }


    /**
     * .
     * 替换动态信息
     *
     * @param content  文本
     * @param entities 实体类
     * @return 替换后信息
     */
    private String replaceInfo(final String content,
                               final CallBackEntities entities) {
        List<Field> fields = FieldUtil.getAllFieldByClass(CallBackEntities.class);
        String tmp = content;
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                field.setAccessible(true);
                // 获取该字段的注解对象
                ExcelAnnotation annotation
                    = field.getAnnotation(ExcelAnnotation.class);
                String colName = annotation.name()[0];
                try {
                    Object result = field.get(entities);
                    String value = "";
                    if (result != null) {
                        if (result instanceof Enum) {
                            Enum eResult = (Enum) result;
                            Field vField = eResult
                                .getClass()
                                .getField(eResult.name());
                            if (vField != null) {
                                ExcelEnumAnnotation excelEnumAnnotation
                                    = vField.getAnnotation(
                                    ExcelEnumAnnotation.class);
                                if (excelEnumAnnotation != null) {
                                    value = excelEnumAnnotation.name();
                                }
                            }
                        } else {
                            value = String.valueOf(field.get(entities));
                        }
                    }
                    tmp = tmp.replace("{" + colName + "}", value);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    log.error("{}", ErrorUtil.getStackTrace(e));
                }
            }
        }
        return tmp;
    }

    /**
     * .
     * 解析数据得到paper结果
     *
     * @param callInfoStatistics 呼叫数据
     * @return Paper
     */
    public Paper parseToPaper(
        final CallInfoStatistics callInfoStatistics) {
        if (callInfoStatistics.getColumn25() == null) {
            callInfoStatisticsMapper.invalidRecordList(callInfoStatistics);
            return null;
        }
        VisitHistory visitHistory
            = callBackEntitiesMapper
            .findByVisitValue(callInfoStatistics.getColumn25());
        if (visitHistory == null) {
            callInfoStatisticsMapper.invalidRecordList(callInfoStatistics);
            return null;
        }
        Paper paper = new Paper();
        paper.setCallTaskId(callInfoStatistics.getColumn11());
        paper.setContNo(callInfoStatistics.getColumn5());
        paper.setPaperId(callInfoStatistics.getColumn19());

        int size = Integer.parseInt(callInfoStatistics.getColumn26());
        List<String> questionList = new ArrayList<>();
        for (int index = 27; index < 27 + size; index++) {
            String qus = "";
            try {
                Field f = callInfoStatistics.getClass()
                    .getDeclaredField("column" + index);
                f.setAccessible(true);
                qus = String.valueOf(f.get(callInfoStatistics));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("{}", ErrorUtil.getStackTrace(e));
            }
            questionList.add(qus);
        }
        String answerStr = callInfoStatistics.getColumn14();
        List<String> answerList = new ArrayList<>();
        answerList.add("是");
        answerList.add("否");
        answerList.add("空");
        String[] answer = null;
        if (answerStr != null) {
            answer = answerStr.split(",");
        }
        List<Questions> questionsList = new ArrayList<>();
        boolean allQuesAccess = true;
        CallBackEntities callBackEntities
            = callBackEntitiesMapper.findById(callInfoStatistics.getColumn25());
        for (int index = 0; index < questionList.size(); index++) {
            Questions questions = new Questions();
            questions.setSeqno(String.valueOf(index + 1));
            String content = paperMapper.selectQuestionContentById(questionList.get(index));
            questions.setQuestionContent(replaceInfo(content, callBackEntities));
            List<Answers> answersList = new ArrayList<>();
            for (int answerIndex = 0;
                 answerIndex < answerList.size();
                 answerIndex++) {
                Answers answers = new Answers();
                answers.setAnswerSeqno(String.valueOf(answerIndex + 1));
                answers.setAnswerContent(answerList.get(answerIndex));
                if (answer != null
                    && index < answer.length
                    && answer[index].equals("是")
                    && answerIndex == 0) {
                    answers.setIsSelected("1");
                } else if (answer != null
                    && index < answer.length
                    && answer[index].equals("否")
                    && answerIndex == 1) {
                    answers.setIsSelected("1");
                    allQuesAccess = false;
                } else if ((answer == null
                    || index >= answer.length)
                    && answerIndex == 2) {
                    answers.setIsSelected("1");
                    allQuesAccess = false;
                } else {
                    answers.setIsSelected("0");
                }
                answersList.add(answers);
            }
            questions.setAnswersList(answersList);
            questionsList.add(questions);
        }
        paper.setQuestionsList(questionsList);
        if (answer != null && answer.length == questionList.size()) {
            paper.setAllAnswer(true);
        } else {
            paper.setAllAnswer(false);
            allQuesAccess = false;
        }
        paper.setAllAccess(allQuesAccess);
        return paper;
    }

    /**
     * .
     * 批量更新已回访数据
     *
     * @param items 保单号集合
     * @return 受影响数
     */
    public int updateBatchCallBackPaper(final List<String> items) {
        int end;
        int sum = 0;
        for (int start = 0; start < items.size(); start = end) {
            end = start + SQL_IN_DATA_MAX_SIZE;
            if (end > items.size()) {
                end = items.size();
            }
            int count = callInfoStatisticsMapper
                .updateBatchCallBackPaper(
                    items.subList(start, end));
            sum = count + sum;
        }
        return sum;
    }

    /**
     * .
     * 批量更新已回写录音数据
     *
     * @param items id集合
     * @return 受影响数
     */
    public int updateBatchCallBackRecord(final List<String> items) {
        int end;
        int sum = 0;
        for (int start = 0; start < items.size(); start = end) {
            end = start + SQL_IN_DATA_MAX_SIZE;
            if (end > items.size()) {
                end = items.size();
            }
            int count = callInfoStatisticsMapper
                .updateBatchCallBackRecord(
                    items.subList(start, end));
            sum = count + sum;
        }
        return sum;
    }

    /**
     * .
     * 获取异常数据
     *
     * @param page 查询条件
     * @return CallBackEntities 集合
     */
    public List<CallBackEntities> queryInvalidData(
        final CallCyclePage page) {
        return callBackEntitiesMapper.queryInvalidData(page);
    }

    /**
     * .
     * 测试解析问卷
     *
     * @param xmlStr 请求报文
     * @return 回传报文
     * @throws Exception Exception
     */
    public String testParsePaper(final String xmlStr)
        throws Exception {
        Map<String, Papers> papersMap
            = CustomXmlUtil.unpack(xmlStr, Papers.class);
        Papers papers = null;
        String uuid = null;
        for (String key : papersMap.keySet()) {
            uuid = key;
            papers = papersMap.get(key);
        }
        if (papers != null) {
            List<Paper> paperList = papers.getPaperList();
            List<Save> saveList = new ArrayList<>();
            for (Paper paper : paperList) {
                Save save = testSinglePaper(paper);
                saveList.add(save);
            }
            Saves saves = new Saves();
            saves.setSaveList(saveList);
            return CustomXmlUtil.pack(saves,
                BusinessType.Papers,
                MessageType.response,
                uuid);
        } else {
            return "";
        }
    }

    /**
     * .
     * 处理单个Paper
     *
     * @param paper Paper
     * @return Save
     */
    private Save testSinglePaper(final Paper paper) {
        Save save = new Save();
        save.setContNo(paper.getContNo());
        save.setStatus("1");
        save.setQuestionnaireID(UUID.randomUUID().toString());
        return save;
    }

    /**
     * .
     * 测试解析回访历史
     *
     * @param xmlStr 请求报文
     * @return 回传报文
     * @throws Exception Exception
     */
    public String testParseVisitHistory(final String xmlStr)
        throws Exception {
        Map<String, VisitHistorys> visitHistorysMap
            = CustomXmlUtil.unpack(xmlStr, VisitHistorys.class);
        VisitHistorys visitHistorys = null;
        String uuid = null;
        for (String key : visitHistorysMap.keySet()) {
            uuid = key;
            visitHistorys = visitHistorysMap.get(key);
        }
        if (visitHistorys != null) {
            List<VisitHistory> visitHistoryList
                = visitHistorys.getVisitHistoryList();
            List<VisitHistoryRslt> visitHistoryRsltList = new ArrayList<>();
            if (visitHistoryList != null) {
                for (VisitHistory visitHistory : visitHistoryList) {
                    VisitHistoryRslt visitHistoryRslt
                        = testSingleVisitHistory(visitHistory);
                    visitHistoryRsltList.add(visitHistoryRslt);
                }
            }
            VisitHistoryRslts visitHistoryRslts
                = new VisitHistoryRslts();
            visitHistoryRslts.setVisitHistoryRsltList(visitHistoryRsltList);
            return CustomXmlUtil.pack(visitHistoryRslts,
                BusinessType.VisitHistory,
                MessageType.response,
                uuid);
        } else {
            return "";
        }
    }

    /**
     * .
     * 处理单个History
     *
     * @param visitHistory VisitHistory
     * @return VisitHistoryRslt
     */
    private VisitHistoryRslt testSingleVisitHistory(
        final VisitHistory visitHistory) {
        VisitHistoryRslt visitHistoryRslt = new VisitHistoryRslt();
        visitHistoryRslt.setContNo(visitHistory.getContNo());
        visitHistoryRslt.setStatus("1");
        visitHistoryRslt.setVisitHistorySeqno(UUID.randomUUID().toString());
        return visitHistoryRslt;
    }

    /**
     * .
     * 下载单个文件
     *
     * @param remotePath 远端地址
     * @param sftp       SFTPClient
     * @return 文件对象
     */
    private File downLoadFtpFile(final String remotePath,
                                 final SFTPClient sftp) {
        File file = new File(remotePath);
        File destFile = new File(tmpPath
            + File.separator
            + file.getName());
        try {
            sftp.get(remotePath,
                new FileSystemFile(destFile));
        } catch (IOException e) {
            log.error("下载异常:{}", ErrorUtil.getStackTrace(e));
            return null;
        }
        return destFile;
    }

    /**
     * .
     * 根据主键终止单个数据
     *
     * @param id 主键
     */
    public void terminateById(String id) {
        callBackEntitiesMapper.terminateById(id);
    }


    /**
     * .
     * 回传无效录音异常处理
     *
     * @param id id值
     */
    public void updateInValidCallBackRecord(final String id) {
        callInfoStatisticsMapper.updateInValidCallBackRecord(id);
    }
}
