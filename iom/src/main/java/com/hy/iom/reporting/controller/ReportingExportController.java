package com.hy.iom.reporting.controller;

import com.github.pagehelper.PageHelper;
import com.hy.iom.base.excel.ExcelUtil;
import com.hy.iom.base.excel.ReportExcel;
import com.hy.iom.base.zip.ZipUnit;
import com.hy.iom.entities.*;
import com.hy.iom.mapper.oracle.RecordInfoMapper;
import com.hy.iom.reporting.excel.CallContents;
import com.hy.iom.reporting.page.*;
import com.hy.iom.reporting.utils.ReportingUtils;
import com.hy.iom.service.CallContentService;
import com.hy.iom.service.CallStatisticsService;
import com.hy.iom.service.RecordInfoService;
import com.hy.iom.utils.FileUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author yuzhiping
 * Description：
 * 运营管理平台-文件导出模块控制器
 */
@RestController
@RequestMapping("/reportingExport")
public class ReportingExportController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private final CallContentService callContentService;
    private final RecordInfoService recordInfoService;
    private final CallStatisticsService callStatisticsService;
    private final RecordInfoMapper recordInfoMapper;

    @Autowired
    public ReportingExportController(CallContentService callContentService, RecordInfoService recordInfoService, CallStatisticsService callStatisticsService, RecordInfoMapper recordInfoMapper) {
        this.callContentService = callContentService;
        this.recordInfoService = recordInfoService;
        this.callStatisticsService = callStatisticsService;
        this.recordInfoMapper = recordInfoMapper;
    }

    /**
     * .
     * 呼叫列表-导出列表
     *
     * @param request  request
     * @param response response
     * @return 结果
     */
    @RequestMapping("/recordInfoDownload")
    public void recordInfoDownload(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> objectMap = request.getParameterMap();
        RecordInfoPage recordInfoPage = new RecordInfoPage();
        try {
            BeanUtils.populate(recordInfoPage, objectMap);//将Map中对应的key和value赋值到对象中
            String[] callNumbers = objectMap.get("callnumber");
            if (callNumbers != null) {
                recordInfoPage.setCallNumber(callNumbers[0]);
            }
            ReportExcel reportExcel = new ReportExcel();
            ExcelUtil excelUtil = new ExcelUtil();
            Long lSum = Long.valueOf(recordInfoMapper.selectByConditionCount(new RecordInfoPage2(recordInfoPage)));
            String sheetName = "呼叫列表";
            if(lSum <= 100){
                XSSFWorkbook workbook = new XSSFWorkbook();
                for(int i = 1; i <= Math.ceil(lSum/10.00); i++){
                    if(i == 1) {
                        PageHelper.startPage(i, 10);
                        List<RecordInfoPage> tmp = recordInfoService.selectByCondition(recordInfoPage);
                        reportExcel.createNewSheet(workbook, tmp, sheetName, RecordInfoPage.class, 1);
                    } else {
                        PageHelper.startPage(i, 10);
                        List<RecordInfoPage> tmp = recordInfoService.selectByCondition(recordInfoPage);
                        List<Field> fields = excelUtil.getFields(RecordInfoPage.class);
                        excelUtil.dealCreateRow(tmp, fields, fields.size(), (i-1)*10+2,
                            workbook.getSheet("呼叫列表"), excelUtil.getDatacellStyle(workbook));
                    }
                }
                reportExcel.export(workbook, sheetName, response, request);
            }
            else {
                for(int i = 1; i <= Math.ceil(lSum/100.00); i++){
                    XSSFWorkbook workbook = new XSSFWorkbook();
                    if(lSum > i * 100){
                        for(int j = 1; j <= 10; j++){
                            if(1 == j){
                                PageHelper.startPage((i-1)*10+j,10);
                                List<RecordInfoPage> tmp = recordInfoService.selectByCondition(recordInfoPage);
                                reportExcel.createNewSheet(workbook, tmp, sheetName, RecordInfoPage.class, 1);
                            } else {
                                PageHelper.startPage((i-1)*10+j,10);
                                List<RecordInfoPage> tmp = recordInfoService.selectByCondition(recordInfoPage);
                                List<Field> fields = excelUtil.getFields(RecordInfoPage.class);
                                excelUtil.dealCreateRow(tmp, fields, fields.size(), (j-1)*10+2,
                                    workbook.getSheet("呼叫列表"), excelUtil.getDatacellStyle(workbook));
                            }
                        }
                    } else {
                        for(int k = 1; k <= Math.ceil((lSum-(i-1)*100)/10.00); k++){
                            if(1 == k){
                                PageHelper.startPage((i-1)*10+k,10);
                                List<RecordInfoPage> tmp = recordInfoService.selectByCondition(recordInfoPage);
                                reportExcel.createNewSheet(workbook, tmp, sheetName, RecordInfoPage.class, 1);
                            } else {
                                PageHelper.startPage((i-1)*10+k,10);
                                List<RecordInfoPage> tmp = recordInfoService.selectByCondition(recordInfoPage);
                                List<Field> fields = excelUtil.getFields(RecordInfoPage.class);
                                excelUtil.dealCreateRow(tmp, fields, fields.size(), (k-1)*10+2,
                                    workbook.getSheet("呼叫列表"), excelUtil.getDatacellStyle(workbook));
                            }
                        }
                    }
                    workbook.write(new FileOutputStream(FileUtils.getTempDirPath() + File.separator + "呼叫列表" + i + ".xlsx"));
                }
                ZipOutputStream zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
                zipos.setMethod(ZipOutputStream.DEFLATED); //设置压缩方法
                DataOutputStream os = null;
                String fname = "呼叫列表1.xlsx";
                File f = new File(FileUtils.getTempDirPath() + File.separator + "呼叫列表1.xlsx");
                int l = 1;
                while(f.exists()){
                    ZipUnit unit = new ZipUnit(f,fname);
                    if (zipos != null) {
                        zipos.putNextEntry(new ZipEntry(unit.getFileName()));
                        os = new DataOutputStream(zipos);
                        try (InputStream is = new FileInputStream(f)) {
                            byte[] b = new byte[64 * 1024];
                            int length;
                            while ((length = is.read(b)) != -1) {
                                os.write(b, 0, length);
                            }
                        } catch (Exception e) {
                            log.error("单个文件压缩出错,{}", f.getAbsolutePath());
                        }
                        zipos.closeEntry();
                    }
                    l++;
                    fname = "呼叫列表" + l + ".xlsx";
                    f = new File(FileUtils.getTempDirPath() + File.separator + "呼叫列表" + l + ".xlsx");
                }
                FileUtils.deleteTempFile2("呼叫列表");
                try {
                    if (os != null) {
                        os.close();
                    }
                    if(zipos != null){
                        zipos.close();
                    }
                } catch (Exception e) {
                    log.error("关闭流出错！{}", e.getMessage());
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 呼叫列表-导出对白详情
     *
     * @param request  request
     * @param response response
     * @return 结果
     */
    @RequestMapping("/callContentDown")
    public void callContentDown(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> objectMap = request.getParameterMap();
        RecordInfoPage recordInfoPage = new RecordInfoPage();
        try {
            BeanUtils.populate(recordInfoPage, objectMap);
            String[] callNumbers = objectMap.get("callnumber");
            if (callNumbers != null) {
                recordInfoPage.setCallNumber(callNumbers[0]);
            }
            long sum = recordInfoMapper.selectCallContentCountByCondition(new RecordInfoPage2(recordInfoPage));
            //响应头的设置
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");

            //设置压缩包的名字
            //解决不同浏览器压缩包名字含有中文时乱码的问题
            String downloadName = System.currentTimeMillis() + ".zip";
            String agent = request.getHeader("USER-AGENT");

            ZipOutputStream zipos;
            if (agent.contains("MSIE") || agent.contains("Trident")) {
                downloadName = java.net.URLEncoder.encode(downloadName, "UTF-8");
            } else {
                downloadName = new String(downloadName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            }
            response.setHeader("Content-Disposition", "attachment;fileName=\"" + downloadName + "\"");
            //设置压缩流：直接写入response，实现边压缩边下载
            zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));

            zipos.setMethod(ZipOutputStream.DEFLATED); //设置压缩方法
            DataOutputStream os = null;
            int pageSize = 100;
            for (int i = 1; (i - 1) * 100 < sum; i++) {
                callContentService.writeZipFile(zipos, recordInfoPage, i, pageSize, os);
            }
            try {
                if (os != null) {
                    os.close();
                }
                if(zipos != null){
                    zipos.close();
                }
            } catch (Exception e) {
                log.error("关闭流出错！{}", e.getMessage());
            }
        } catch (IllegalAccessException | InvocationTargetException | IOException e) {
            log.error("导出文件异常：{}", e.getMessage());
        }
    }

    /**
     * 呼叫结果统计-导出
     *
     * @param request  request
     * @param response response
     * @return 结果
     */
    @RequestMapping("/endNodeCntByDateDownload")
    public void endNodeCntByDateDownload(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resMap = recordInfoService.getEndNodeCntResult(request.getParameter("projectId"), request.getParameter("ruleId"), request.getParameter("recordStartTime"), request.getParameter("recordEndTime"));
        String sheetName = "呼叫结果统计";
        ReportingUtils.downloadDynamicTitleExecl(request, response, resMap, sheetName);
    }

    /**
     * 呼叫结果统计明细下载
     *
     * @param callResult      呼叫结果
     * @param projectId       项目id
     * @param recordEndTime   开始呼叫时间
     * @param recordStartTime 结束呼叫时间
     * @param ruleId          规则id
     * @param request         request
     * @param response        response
     */
    @RequestMapping("/callResultDetailDownload")
    public void callResultDetailDownload(@RequestParam(required = false) String callResult,
                                         @RequestParam(required = false) String projectId,
                                         @RequestParam(required = false) String recordEndTime,
                                         @RequestParam(required = false) String recordStartTime,
                                         @RequestParam(required = false) String ruleId,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        RecordInfoPage recordInfoPage = new RecordInfoPage();
        recordInfoPage.setCallResult(callResult);
        recordInfoPage.setProjectId(projectId);
        recordInfoPage.setRecordStartTime(recordStartTime);
        recordInfoPage.setRecordEndTime(recordEndTime);
        recordInfoPage.setRuleId(ruleId);
        List<RecordInfoPage> page = recordInfoService.selectByCondition(recordInfoPage);
        ReportExcel reportExcel = new ReportExcel();
        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetName = "呼叫结果统计明细";
        reportExcel.createNewSheet(workbook, page, sheetName, RecordInfoPage.class, 1);
        reportExcel.export(workbook, sheetName, response, request);
    }

    /**
     * 呼叫差错统计条形图明细下载
     *
     * @param type            类型
     * @param projectId       项目id
     * @param recordStartTime 开始呼叫时间
     * @param recordEndTime   结束呼叫时间
     * @param ruleId          规则id
     */
    @RequestMapping("/callErrorStatisticsDetailDownload")
    public void callErrorStatisticsDetailDownload(@RequestParam(required = false) String type,
                                                  @RequestParam(required = false) String projectId,
                                                  @RequestParam(required = false) String recordEndTime,
                                                  @RequestParam(required = false) String recordStartTime,
                                                  @RequestParam(required = false) String ruleId,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) throws ParseException {
        CallErrorStatistics callErrorStatistics = new CallErrorStatistics();
        callErrorStatistics.setType(type);
        callErrorStatistics.setProjectId(projectId);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        callErrorStatistics.setRecordStartTime(format.parse(recordStartTime));
        callErrorStatistics.setRecordEndTime(format.parse(recordEndTime));
        callErrorStatistics.setRuleId(ruleId);
        List<RecordInfoPage> page = recordInfoService.getCallErrorStatisticsDetail(callErrorStatistics);
        ReportExcel reportExcel = new ReportExcel();
        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetName = "外呼差错统计明细下载";
        reportExcel.createNewSheet(workbook, page, sheetName, RecordInfoPage.class, 1);
        reportExcel.export(workbook, sheetName, response, request);
    }

    /**
     * 呼叫量统计-导出
     *
     * @param request  request
     * @param response response
     * @return 结果
     */
    @RequestMapping("/callCntDownload")
    public void callCntDownload(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> reqMap = request.getParameterMap();
        CallStatistics callStatistics = new CallStatistics();
        try {
            BeanUtils.populate(callStatistics, reqMap);
            callStatistics.setMonth(reqMap.get("recordStartTime") != null ? reqMap.get("recordStartTime")[0] : null);
            callStatistics.setDay(reqMap.get("recordEndTime") != null ? reqMap.get("recordEndTime")[0] : null);
            List<CallCntResult> page = callStatisticsService.getCallCnt(callStatistics);

            ReportExcel reportExcel = new ReportExcel();
            XSSFWorkbook workbook = new XSSFWorkbook();
            String sheetName = "呼叫量统计";
            reportExcel.createNewSheet(workbook, page, sheetName, CallCntResult.class, 1);
            reportExcel.export(workbook, sheetName, response, request);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 交易成功率统计-导出
     *
     * @param request  request
     * @param response response
     * @return 结果
     */
    @RequestMapping("/tradeStatisticsDownload")
    public void tradeStatisticsDownload(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> reqMap = request.getParameterMap();

        TradeStatistics tradeStatistics = new TradeStatistics();
        Date recordStartTime = ReportingUtils.parseDate(reqMap.get("recordStartTime"));
        Date recordEndTime = ReportingUtils.parseDate(reqMap.get("recordEndTime"));
        ReportingUtils.getTradeCondition(reqMap, tradeStatistics, recordStartTime, recordEndTime);

        List<TradeInfoPage> page = recordInfoService.getTradeStatistics(tradeStatistics);

        ReportExcel reportExcel = new ReportExcel();
        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetName = "交易成功率统计";
        reportExcel.createNewSheet(workbook, page, sheetName, TradeInfoPage.class, 1);
        reportExcel.export(workbook, sheetName, response, request);
    }

    /**
     * 交易成功率详情下载
     *
     * @param tradeType       交易类型
     * @param projectId       项目id
     * @param recordEndTime   记录结束时间
     * @param recordStartTime 记录开始时间
     * @param ruleId          角色id
     * @return 结果
     */
    @RequestMapping("/tradeStatisticsDetailDownload")
    public void tradeStatisticsDetailDownload(@RequestParam(required = false) String tradeType,
                                              @RequestParam(required = false) String projectId,
                                              @RequestParam(required = false) String recordEndTime,
                                              @RequestParam(required = false) String recordStartTime,
                                              @RequestParam(required = false) String ruleId,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws ParseException {
        RecordInfoPage recordInfoPage = new RecordInfoPage();
        recordInfoPage.setTradeType(tradeType);
        recordInfoPage.setProjectId(projectId);
        recordInfoPage.setRecordStartTime(recordStartTime);
        recordInfoPage.setRecordEndTime(recordEndTime);
        recordInfoPage.setRuleId(ruleId);
        List<RecordInfoPage> page = recordInfoService.selectByCondition(recordInfoPage);
        ReportExcel reportExcel = new ReportExcel();
        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetName = "交易成功率详情";
        reportExcel.createNewSheet(workbook, page, sheetName, RecordInfoPage.class, 1);
        reportExcel.export(workbook, sheetName, response, request);
    }

    /**
     * 时间段交易成功率统计-导出
     *
     * @param request  request
     * @param response response
     */
    @RequestMapping("/timeSectionTradeStatisticsDownload")
    public void timeSectionTradeStatisticsDownload(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> reqMap = request.getParameterMap();

        TradeStatistics tradeStatistics = new TradeStatistics();
        Date recordTime = ReportingUtils.parseDate(reqMap.get("recordTime"));
        ReportingUtils.getTradeCondition(reqMap, tradeStatistics, recordTime, recordTime);

        List<TradeInfoPage> page = recordInfoService.getTimeSectionTradeStatistics(tradeStatistics);

        ReportExcel reportExcel = new ReportExcel();
        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetName = "时间段交易成功率统计";
        reportExcel.createNewSheet(workbook, page, sheetName, TradeInfoPage.class, 1);
        reportExcel.export(workbook, sheetName, response, request);
    }

    /**
     * 节点通过率统计下载
     *
     * @param projectId            项目id
     * @param ruleId               角色id
     * @param flowId               流id
     * @param nodeName             节点名称
     * @param startTime            开始时间
     * @param endTime              结束时间
     * @param statisticalDimension 统计维度
     * @param statisticalDirection 统计方向
     * @param request              request
     * @param response             response
     * @throws ParseException 异常
     */
    @RequestMapping("/nodePassRateDownload")
    public void nodePassRateDownload(@RequestParam(required = false) String projectId,
                                     @RequestParam(required = false) String ruleId,
                                     @RequestParam(required = false) String flowId,
                                     @RequestParam(required = false) String nodeName,
                                     @RequestParam String startTime,
                                     @RequestParam String endTime,
                                     @RequestParam(required = false) String statisticalDimension,
                                     @RequestParam(required = false) String statisticalDirection,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws ParseException {
        NodePassRatePage nodePassRatePage = new NodePassRatePage();
        nodePassRatePage.setProjectId(projectId);
        nodePassRatePage.setRuleId(ruleId);
        nodePassRatePage.setFlowId(flowId);
        nodePassRatePage.setNodeName(nodeName);
        nodePassRatePage.setStatisticalDimension(statisticalDimension);
        nodePassRatePage.setStatisticalDirection(statisticalDirection);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate;
        Date endDate;
        startDate = format.parse(startTime);
        endDate = format.parse(endTime);
        nodePassRatePage.setStartTime(startDate);
        nodePassRatePage.setEndTime(endDate);
        Map<String, Object> resMap = recordInfoService.getNodePassRateResult(nodePassRatePage, false);
        ReportingUtils.downloadDynamicTitleExecl(request, response, resMap, "节点通过率统计");
    }

    /**
     * 节点通过率详情下载
     *
     * @param flowId               流id
     * @param ruleId               角色id
     * @param nodeName             节点名称
     * @param date                 日期
     * @param statisticalDimension 统计维度
     * @param statisticalDirection 统计方向
     * @param request              request
     * @param response             response
     */
    @RequestMapping("/nodePassRateDetailDownload")
    public void nodePassRateDetailDownload(@RequestParam(required = false) String flowId,
                                           @RequestParam(required = false) String ruleId,
                                           @RequestParam(required = false) String nodeName,
                                           @RequestParam(required = false) String date,
                                           @RequestParam(required = false) String statisticalDirection,
                                           @RequestParam(required = false) String statisticalDimension,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        NodePassRatePage nodePassRatePage = new NodePassRatePage();
        nodePassRatePage.setDate(date);
        nodePassRatePage.setProjectId(flowId);
        nodePassRatePage.setRuleId(ruleId);
        nodePassRatePage.setNodeName(nodeName);
        nodePassRatePage.setStatisticalDirection(statisticalDirection);
        nodePassRatePage.setStatisticalDimension(statisticalDimension);
        List<RecordInfoPage> page = recordInfoService.selectPageByNodePassCondition(nodePassRatePage);
        ReportExcel reportExcel = new ReportExcel();
        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetName = "节点通过详情";
        reportExcel.createNewSheet(workbook, page, sheetName, RecordInfoPage.class, 1);
        reportExcel.export(workbook, sheetName, response, request);
    }

    /**
     * 挂机节点统计下载
     *
     * @param projectId            项目id
     * @param ruleId               角色id
     * @param flowId               流程id
     * @param taskId               任务id
     * @param endNodeName          结束节点名称
     * @param startTime            开始时间
     * @param endTime              结束时间
     * @param statisticalDimension 统计维度
     * @param request              request
     * @param response             response
     * @throws ParseException 格式化异常
     */
    @RequestMapping("/hangupNodeNameDownload")
    public void hangupNodeNameDownload(@RequestParam(required = false) String projectId,
                                       @RequestParam(required = false) String ruleId,
                                       @RequestParam(required = false) String flowId,
                                       @RequestParam(required = false) String taskId,
                                       @RequestParam(required = false) String endNodeName,
                                       @RequestParam String startTime,
                                       @RequestParam String endTime,
                                       @RequestParam(required = false) String statisticalDimension,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws ParseException {
        EndNodeNamePage endNodeNamePage = new EndNodeNamePage();
        endNodeNamePage.setProjectId(projectId);
        endNodeNamePage.setRuleId(ruleId);
        endNodeNamePage.setFlowId(flowId);
        endNodeNamePage.setTaskId(taskId);
        endNodeNamePage.setEndNodeName(endNodeName);
        endNodeNamePage.setStatisticalDimension(statisticalDimension);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate;
        Date endDate;
        startDate = format.parse(startTime);
        endDate = format.parse(endTime);
        endNodeNamePage.setStartTime(startDate);
        endNodeNamePage.setEndTime(endDate);
        Map<String, Object> resMap = recordInfoService.getHangupNodeNameStatistical(endNodeNamePage, false);
        String sheetName = "挂机节点统计";
        ReportingUtils.downloadDynamicTitleExecl(request, response, resMap, sheetName);
    }

    /**
     * 挂机节点详情下载
     *
     * @param flowId               流程id
     * @param endNodeName          挂机节点名称
     * @param date                 时间
     * @param statisticalDimension 统计维度
     * @param request              request
     * @param response             response
     */
    @RequestMapping("/hangupNodeDetailDownload")
    public void hangupNodeDetailDownload(@RequestParam(required = false) String flowId,
                                         @RequestParam(required = false) String ruleId,
                                         @RequestParam(required = false) String taskId,
                                         @RequestParam(required = false) String endNodeName,
                                         @RequestParam(required = false) String date,
                                         @RequestParam(required = false) String statisticalDimension,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        EndNodeNamePage endNodeNamePage = new EndNodeNamePage();
        endNodeNamePage.setDate(date);
        endNodeNamePage.setFlowId(flowId);
        endNodeNamePage.setRuleId(ruleId);
        endNodeNamePage.setTaskId(taskId);
        endNodeNamePage.setEndNodeName(endNodeName);
        endNodeNamePage.setStatisticalDimension(statisticalDimension);
        List<RecordInfoPage> page = recordInfoService.selectPageByHangupNodeCondition(endNodeNamePage);
        ReportExcel reportExcel = new ReportExcel();
        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetName = "挂机节点详情";
        reportExcel.createNewSheet(workbook, page, sheetName, RecordInfoPage.class, 1);
        reportExcel.export(workbook, sheetName, response, request);
    }

    /**
     * 场景通过明细下载
     *
     * @param projectId  项目id
     * @param ruleId     规则Id
     * @param flowId     流程id
     * @param taskId     任务id
     * @param sceneName  场景名称
     * @param customerId 客户编号
     * @param onState    接通状态
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param request    request
     * @param response   response
     * @throws ParseException 格式化异常
     */
    @RequestMapping("/sceneThroughDeatilDownload")
    public void sceneThroughDeatilDownload(@RequestParam(required = false) String projectId,
                                           @RequestParam(required = false) String ruleId,
                                           @RequestParam(required = false) String flowId,
                                           @RequestParam(required = false) String taskId,
                                           @RequestParam(required = false) String sceneName,
                                           @RequestParam(required = false) String customerId,
                                           @RequestParam(required = false) String onState,
                                           @RequestParam String startTime,
                                           @RequestParam String endTime,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws ParseException {
        SceneThroughPage page = new SceneThroughPage();
        page.setProjectId(projectId);
        page.setRuleId(ruleId);
        page.setFlowId(flowId);
        page.setTaskId(taskId);
        page.setSceneName(sceneName);
        page.setCustomerId(customerId);
        page.setOnState(onState);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate;
        Date endDate;
        startDate = format.parse(startTime);
        endDate = format.parse(endTime);
        page.setStartTime(startDate);
        page.setEndTime(endDate);
        Map<String, Object> resMap = recordInfoService.getSceneThroughDetail(page, false);
        String sheetName = "场景通过明细";
        ReportingUtils.downloadDynamicTitleExecl(request, response, resMap, sheetName);
    }

    /**
     * 新接通率查询导出接口
     *
     * @param projectId            项目id
     * @param ruleId               规则id
     * @param taskId               任务id
     * @param statisticalDimension 统计维度
     * @param startTime            开始时间
     * @param endTime              结束时间
     * @param validStartTime       有效开始时间
     * @param validEndTime         有效结束时间
     * @param request              request
     * @param response             response
     */
    @RequestMapping("/newConnectionRateDownload")
    public void newConnectionRateDownload(@RequestParam(required = false) String projectId,
                                          @RequestParam(required = false) String ruleId,
                                          @RequestParam(required = false) String taskId,
                                          @RequestParam(required = false) String statisticalDimension,
                                          @RequestParam String startTime,
                                          @RequestParam String endTime,
                                          @RequestParam(required = false) String validStartTime,
                                          @RequestParam(required = false) String validEndTime,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        ConnectRate connectRate = new ConnectRate();
        connectRate.setProjectId(projectId);
        connectRate.setRuleId(ruleId);
        connectRate.setTaskId(taskId);
        connectRate.setStatisticalDimension(statisticalDimension);
        connectRate.setStartTime(startTime);
        connectRate.setEndTime(endTime);
        connectRate.setValidStartTime(validStartTime);
        connectRate.setValidEndTime(validEndTime);
        Map<String, Object> resMap = recordInfoService.getConnectionRate(connectRate, false);
        String sheetName = "接通率查询统计";
        ReportingUtils.downloadDynamicTitleExecl(request, response, resMap, sheetName);
    }

    /**
     * 新接通率查询明细导出接口
     *
     * @param projectId            项目id
     * @param ruleId               规则id
     * @param taskId               任务id
     * @param statisticalDimension 统计维度
     * @param date                 日期
     * @param time                 时间
     * @param request              request
     * @param response             response
     */
    @RequestMapping("/ConnectionRateDetailDownload")
    public void ConnectionRateDetailDownload(@RequestParam(required = false) String projectId,
                                             @RequestParam(required = false) String ruleId,
                                             @RequestParam(required = false) String taskId,
                                             @RequestParam(required = false) String statisticalDimension,
                                             @RequestParam String date,
                                             @RequestParam String time,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        ConnectRate connectRate = new ConnectRate();
        connectRate.setProjectId(projectId);
        connectRate.setRuleId(ruleId);
        connectRate.setTaskId(taskId);
        connectRate.setDate(date);
        connectRate.setTime(time);
        connectRate.setStatisticalDimension(statisticalDimension);
        List<RecordInfoPage> page = recordInfoService.selectPageConnectionRateCondition(connectRate);
        ReportExcel reportExcel = new ReportExcel();
        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetName = "接通率时间点详情";
        reportExcel.createNewSheet(workbook, page, sheetName, RecordInfoPage.class, 1);
        reportExcel.export(workbook, sheetName, response, request);
    }

    /**
     * 挂机率查询导出接口
     *
     * @param projectId            项目id
     * @param ruleId               规则id
     * @param taskId               任务id
     * @param statisticalDimension 统计维度
     * @param startTime            开始时间
     * @param endTime              结束时间
     * @param request              request
     * @param response             response
     */
    @RequestMapping("/hangupRateDownload")
    public void HangupRateDownload(@RequestParam(required = false) String projectId,
                                   @RequestParam(required = false) String ruleId,
                                   @RequestParam(required = false) String taskId,
                                   @RequestParam(required = false) String statisticalDimension,
                                   @RequestParam String startTime,
                                   @RequestParam String endTime,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        EndNodeNamePage endNodeNamePage = new EndNodeNamePage();
        endNodeNamePage.setProjectId(projectId);
        endNodeNamePage.setRuleId(ruleId);
        endNodeNamePage.setTaskId(taskId);
        endNodeNamePage.setStatisticalDimension(statisticalDimension);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = format.parse(startTime);
            endDate = format.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        endNodeNamePage.setStartTime(startDate);
        endNodeNamePage.setEndTime(endDate);
        Map<String, Object> resMap = recordInfoService.getHangupRate(endNodeNamePage, false);
        String sheetName = "挂机率查询统计";
        ReportingUtils.downloadDynamicTitleExecl(request, response, resMap, sheetName);
    }

    /**
     * 挂机率明细导出接口
     *
     * @param projectId            项目id
     * @param ruleId               规则id
     * @param taskId               任务id
     * @param statisticalDimension 统计维度
     * @param date                 日期
     * @param endNodeName          挂机节点
     * @param request              request
     * @param response             response
     */
    @RequestMapping("/hangupRateDetailDownload")
    public void HangupRateDetailDownloadDownload(@RequestParam(required = false) String projectId,
                                                 @RequestParam(required = false) String ruleId,
                                                 @RequestParam(required = false) String taskId,
                                                 @RequestParam(required = false) String statisticalDimension,
                                                 @RequestParam String date,
                                                 @RequestParam String endNodeName,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {
        EndNodeNamePage endNodeNamePage = new EndNodeNamePage();
        endNodeNamePage.setProjectId(projectId);
        endNodeNamePage.setRuleId(ruleId);
        endNodeNamePage.setTaskId(taskId);
        endNodeNamePage.setDate(date);
        endNodeNamePage.setEndNodeName(endNodeName);
        endNodeNamePage.setStatisticalDimension(statisticalDimension);
        List<RecordInfoPage> page = recordInfoService.selectPageHangupRateCondition(endNodeNamePage);
        ReportExcel reportExcel = new ReportExcel();
        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetName = "挂机率详情";
        reportExcel.createNewSheet(workbook, page, sheetName, RecordInfoPage.class, 1);
        reportExcel.export(workbook, sheetName, response, request);
    }
}
