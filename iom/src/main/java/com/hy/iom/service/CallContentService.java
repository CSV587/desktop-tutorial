package com.hy.iom.service;

import com.hy.iom.base.excel.ReportExcel;
import com.hy.iom.base.zip.ZipUnit;
import com.hy.iom.entities.*;
import com.hy.iom.mapper.oracle.CallContentMapper;
import com.hy.iom.reporting.excel.CallContents;
import com.hy.iom.reporting.page.RecordInfoPage;
import com.hy.iom.reporting.utils.ReportingUtils;
import com.hy.iom.utils.CollectionsUtil;
import com.hy.iom.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class CallContentService {

    private static Logger log = LoggerFactory.getLogger(CallContentService.class);

    @Autowired
    private CallContentMapper callContentMapper;

    @Autowired
    private CustomerInfoService customerInfoService;

    @Autowired
    private RecordInfoService recordInfoService;

    public List<CallContentMatchResult> selectMatchResultByUuid(String uuid) {
        List<CallContent> callContentList = callContentMapper.selectMatchResultByUuid(uuid);
        return transState(processState(callContentList, uuid));
    }

    public static List<CallContentMatchResult> transState(List<CallContentMatchResult> callContentMatchList) {
        for (CallContentMatchResult result : callContentMatchList) {
            if (StringUtils.isBlank(result.getMatchNode())) {
                result.setMatchNode("");
            }
            if (result.getMatchNode().equals("matchError")) {
                result.setMatchNode("匹配失败");
            } else {
                if (result.getMatchNode().equals("speechOverTime")) {
                    result.setMatchNode("识别超时");
                } else if (result.getMatchNode().equals("error")) {
                    result.setMatchNode("异常中断");
                }
            }
        }
        return callContentMatchList;
    }


    public static List<CallContentMatchResult> processState(List<CallContent> callContentList, String uuid) {
        List<CallContentMatchResult> list = new ArrayList<>();
        int seq = 1;
        if (callContentList != null) {
            CallContentMatchResult callContentMatchResult = null;
            for (CallContent cc : callContentList) {
                switch (cc.getType()) {
                    case "tts":
                        callContentMatchResult = new CallContentMatchResult();
                        callContentMatchResult.setSeq(seq++);
                        callContentMatchResult.setCurrentNode(cc.getNodeName());
                        callContentMatchResult.setAiContent(cc.getContent());
                        break;
                    case "speechSuccess":
                        if (callContentMatchResult == null) {
                            log.error("该speechSuccess节点前没有TTS uuid={}", uuid);
                        } else {
                            String customerContent = cc.getContent();
                            callContentMatchResult.setCustomerContent(StringUtils.isNotBlank(customerContent) ? customerContent : "");
                        }
                        break;
                    case "dtmfSuccess":
                        if (callContentMatchResult == null) {
                            log.error("该dtmfSuccess节点前没有TTS uuid={}", uuid);
                        } else if (callContentMatchResult.getCustomerContent() != null) {
                            log.error("单TTS节点 重复dtmfSuccess uuid={}", uuid);
                        } else {
                            String customerContent = cc.getContent();
                            callContentMatchResult.setCustomerContent(StringUtils.isNotBlank(customerContent) ? customerContent.substring("接受按键输入完成:[".length(), customerContent.length() - 1) : "");
                        }
                        break;
                    case "dtmfError":
                        if (callContentMatchResult == null) {
                            log.error("该dtmfError节点前没有TTS uuid={}", uuid);
                        } else if (callContentMatchResult.getCustomerContent() != null) {
                            log.error("单TTS节点 重复dtmfError uuid={}", uuid);
                        } else {
                            String customerContent = cc.getContent();
                            if (StringUtils.isNotBlank(customerContent)) {
                                int startIndex = customerContent.indexOf("[") + 1;
                                int endIndex = customerContent.indexOf("]");
                                customerContent = customerContent.substring(startIndex, endIndex);
                            }
                            callContentMatchResult.setCustomerContent(customerContent.substring("(按键长度不符)".length()));
                        }
                        break;
                    case "dtmfOverTime":
                        if (callContentMatchResult == null) {
                            log.error("该dtmfOverTime节点前没有TTS uuid={}", uuid);
                        } else if (callContentMatchResult.getCustomerContent() != null) {
                            log.error("单TTS节点 重复dtmfOverTime uuid={}", uuid);
                        } else {
                            String customerContent = cc.getContent();
                            if (StringUtils.isNotBlank(customerContent)) {
                                int startIndex = customerContent.indexOf("[") + 1;
                                int endIndex = customerContent.indexOf("]");
                                customerContent = customerContent.substring(startIndex, endIndex);
                            }
                            callContentMatchResult.setCustomerContent(customerContent.substring("(超时)".length()));
                        }
                        break;
                    case "matchSucess":
                        if (callContentMatchResult == null) {
                            log.error("该matchSucess节点前没有TTS  uuid={}", uuid);
                        } else {
                            callContentMatchResult.setMatchNode(cc.getNodeName());
                        }
                        break;
                    case "speechOverTime":
                    case "matchError":
                        if (callContentMatchResult == null) {
                            log.error("该{}节点前没有TTS  uuid={}", cc.getType(), uuid);
                        } else if (callContentMatchResult.getMatchNode() == null) {
                            callContentMatchResult.setMatchNode(cc.getType());
                        }
                        break;
                    case "error":
                        callContentMatchResult = new CallContentMatchResult();
                        callContentMatchResult.setSeq(seq++);
                        if (cc.getNodeName().equals("FS连接断开")) {
                            callContentMatchResult.setCurrentNode("客户挂断");
                            callContentMatchResult.setAiContent("客户挂断");
                        } else {
                            callContentMatchResult.setCurrentNode(cc.getNodeName());
                            callContentMatchResult.setAiContent("异常挂断");
                        }
                        break;
                    case "hangup":
                        callContentMatchResult = new CallContentMatchResult();
                        callContentMatchResult.setSeq(seq++);
                        callContentMatchResult.setCurrentNode(cc.getNodeName());
                        callContentMatchResult.setAiContent(cc.getContent());
                        break;
                    case "end":
                        callContentMatchResult = new CallContentMatchResult();
                        callContentMatchResult.setSeq(seq++);
                        callContentMatchResult.setCurrentNode("流程结束");
                        callContentMatchResult.setAiContent("流程结束");
                        break;
                }
                if (callContentMatchResult != null && (!list.contains(callContentMatchResult))) {
                    list.add(callContentMatchResult);
                }
            }
        }
        return list;
    }

    public List<CallContents> getCallContentInfo(List<String> uuids) {
        List<CallContents> callContents = new LinkedList<>();
        if (CollectionsUtil.isNotEmpty(uuids)) {
            for (String uuid : uuids) {
                CallContents cc = new CallContents(uuid);
                List<RecordInfo> recordInfos = recordInfoService.selectByUuid(new RecordInfo(uuid));
                if (recordInfos != null && recordInfos.size() > 0) {
                    RecordInfo recordInfo = recordInfos.get(0);
                    if (StringUtils.isNotBlank(recordInfo.getRecordPath())) {
                        cc.setVoiceFile(new File(recordInfo.getRecordPath()));
                    }
                }
                List<CallContentMatchResult> page = this.selectMatchResultByUuid(uuid);
                ReportExcel reportExcel = new ReportExcel();
                Workbook workbook = new XSSFWorkbook();
                reportExcel.createNewSheet(workbook, page, "呼叫对白", CallContentMatchResult.class, 1);
                List<CustomerInfo> customerInfos = customerInfoService.selectByUuid(new CustomerInfo(uuid));
                reportExcel.createNewSheet(workbook, customerInfos, "客户信息", CustomerInfo.class, 1);
                try {
                    cc.writeExcel(workbook);
                } catch (IOException e) {
                    log.error("excel生成失败:{}", e);
                }
                callContents.add(cc);
            }
        }
        return callContents;
    }

    /**
     * .
     * 分流写文件
     *
     * @param zipos          压缩流
     * @param recordInfoPage 分页查询条件
     * @param current        当前页
     * @param pageSize       每页大小
     */
    public void writeZipFile(ZipOutputStream zipos, RecordInfoPage recordInfoPage, int current, int pageSize, DataOutputStream os) {
        List<CallContents> callContents = new LinkedList<>();
        try {
            callContents = recordInfoService.selectCallContentDetailByCondition(recordInfoPage, current, pageSize);
            ReportingUtils.createExcelFiles(callContents);
            List<ZipUnit> files = ReportingUtils.getCallContentFiles(callContents);
            if (files != null && files.size() > 0) {
                for (ZipUnit unit : files) {
                    if (unit == null) {
                        continue;
                    }
                    File file = unit.getFile();
                    if (!file.exists()) {
                        log.error("文件不存在 跳过,{}", file.getAbsolutePath());
                    }
                    //添加ZipEntry，并ZipEntry中写入文件流
                    //这里，加上i是防止要下载的文件有重名的导致下载失败
                    if (zipos != null) {
                        zipos.putNextEntry(new ZipEntry(unit.getFileName()));
                        os = new DataOutputStream(zipos);
                        try (InputStream is = new FileInputStream(file)) {
                            byte[] b = new byte[64 * 1024];
                            int length;
                            while ((length = is.read(b)) != -1) {
                                os.write(b, 0, length);
                            }
                        } catch (Exception e) {
                            log.error("单个文件压缩出错,{}", file.getAbsolutePath());
                        }
                        zipos.closeEntry();
                    }
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.flush();
                } catch (Exception e) {
                    log.error("刷新流出错！{}", e.getMessage());
                }
            }
            FileUtils.deleteTempFile(callContents);
        }
    }
}
