package com.hy.iom.reporting.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.hy.iom.base.excel.ReportExcel;
import com.hy.iom.base.zip.ZipUnit;
import com.hy.iom.common.page.PageResult;
import com.hy.iom.common.page.TableHead;
import com.hy.iom.entities.CallContentMatchResult;
import com.hy.iom.entities.CallStatistics;
import com.hy.iom.entities.CustomerInfo;
import com.hy.iom.entities.TradeStatistics;
import com.hy.iom.reporting.excel.CallContents;
import com.hy.iom.utils.BeanTools;
import com.hy.iom.utils.CollectionsUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * @author yuzhiping
 * @date 2018年12月26日
 * Description：
 * Reporting模块中用到的工具类
 */
public class ReportingUtils {

    /**
     * Description：
     * 将所有的list对象转换成LinkedList对象
     */
    public static LinkedList<Map<String, String>> toListMap(List<?> data) {
        LinkedList<Map<String, String>> list = new LinkedList<>();
        for (Object object : data) {
            try {
                list.add(BeanUtils.describe(object));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * Description：
     * 给TradeStatistics对象赋值
     */
    public static void getTradeCondition(Map<String, String[]> reqMap, TradeStatistics tradeStatistics, Date recordStartTime, Date recordEndTime) {
        String[] projectId = reqMap.get("projectId");
        String[] ruleId = reqMap.get("ruleId");
        tradeStatistics.setRecordStartTime(recordStartTime);
        tradeStatistics.setRecordEndTime(recordEndTime);
        tradeStatistics.setProjectId(projectId[0]);
        if (ruleId != null && ruleId.length > 0) {
            tradeStatistics.setRuleId(ruleId[0]);
        }
    }

    /**
     * Description：
     * 日期格式化处理
     */
    public static Date parseDate(String[] recordStartTimes) {
        Date date = null;
        if (recordStartTimes != null && recordStartTimes.length > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String time = recordStartTimes[0];
            try {
                date = simpleDateFormat.parse(time);
            } catch (Exception e) {
                //ignore
            }
        }
        return date;
    }

    /**
     * Description：
     * 带参数格式化日期处理
     */
    public static void formatData(Map<String, LinkedList<Integer>> onceHashMap, LinkedList<Integer> ll, CallStatistics cs) {
        if (onceHashMap.containsKey(cs.getDay())) {
            LinkedList<Integer> linkedList = onceHashMap.get(cs.getDay());
            updateData(cs.getOnState(), cs.getCnt(), linkedList);
        } else {
            LinkedList<Integer> clone = (LinkedList<Integer>) ll.clone();
            updateData(cs.getOnState(), cs.getCnt(), clone);
            onceHashMap.put(cs.getDay(), clone);
        }
    }

    /**
     * Description：
     * 日期修改
     */
    private static void updateData(String state, Integer cnt, LinkedList<Integer> linkedList) {
        if ("connect".equals(state)) {
            linkedList.set(0, cnt);
        } else if ("unconnect".equals(state)) {
            linkedList.set(1, cnt);
        }
    }

    /**
     * Description：
     * 给linkedList填充值，计算速率
     */
    public static void calculateRate(LinkedList<Integer> list, LinkedList<Double> linkedList, int index, int dividendIndex) {
        BigDecimal rate = BigDecimal.valueOf(list.get(dividendIndex))
            .divide(BigDecimal.valueOf(list.get(0)).add(BigDecimal.valueOf(list.get(1))),
                2, ROUND_HALF_UP);
        linkedList.set(index, rate.doubleValue());
    }

    /**
     * Description：
     * 下载动态Excel
     */
    public static void downloadDynamicTitleExecl(HttpServletRequest request, HttpServletResponse response, Map<String, Object> resMap, String sheetName) {
        List<ObjectNode> list = (List) resMap.get("list");
        List<TableHead> tableHeads = (List) resMap.get("tableHeads");
        ReportExcel reportExcel = new ReportExcel();
        String[] title = new String[tableHeads.size()];
        String[][] content = new String[list.size()][tableHeads.size()];
        for (int i = 0; i < tableHeads.size(); i++) {
            TableHead tableHead = tableHeads.get(i);
            title[i] = tableHead.getTitle();
        }
        for (int i = 0; i < list.size(); i++) {
            ObjectNode item = list.get(i);
            for (int j = 0; j < tableHeads.size(); j++) {
                TableHead tableHead = tableHeads.get(j);
                JsonNode node = item.get(tableHead.getDataIndex());
                if (node != null) {
                    if (node instanceof ObjectNode) {
                        content[i][j] = node.get("value") + "";
                    } else if (node instanceof TextNode) {
                        content[i][j] = node.textValue();
                    } else if (node instanceof LongNode) {
                        content[i][j] = String.valueOf(node.longValue());
                    }
                } else {
                    content[i][j] = "0.00%";
                }
            }
        }
        reportExcel.excelExport(sheetName, title, content, response, request);
    }

    /**
     * Description：
     * 获取分页结果
     */
    public static PageResult getPageResult(Map<String, Object> resMap) {
        List list = (List) resMap.get("list");
        List tableHeads = (List) resMap.get("tableHeads");
        long total = (Long) resMap.get("total");
        return PageResult.success("操作成功", list, tableHeads, total);
    }

    /**
     * Description：
     * 创建Excel文件
     */
    public static void createExcelFiles(List<CallContents> callContents) {
        ReportExcel reportExcel = new ReportExcel();
        if (CollectionsUtil.isNotEmpty(callContents)) {
            for (CallContents cc : callContents) {
                List<CallContentMatchResult> page = BeanTools.jsonArray2BeanList(cc.getCallContent(), CallContentMatchResult.class);
                Workbook workbook = new XSSFWorkbook();
                reportExcel.createNewSheet(workbook, page, "呼叫对白", CallContentMatchResult.class, 1);
                List<CustomerInfo> customerInfos = BeanTools.jsonArray2BeanList(cc.getCustomInfo(), CustomerInfo.class);
                reportExcel.createNewSheet(workbook, customerInfos, "客户信息", CustomerInfo.class, 1);
                try {
                    cc.writeExcel(workbook);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Description：
     * 获得呼叫内容文件
     */
    public static List<ZipUnit> getCallContentFiles(List<CallContents> callContents) {
        List<ZipUnit> files = new LinkedList<>();
        if (CollectionsUtil.isNotEmpty(callContents)) {
            for (CallContents cc : callContents) {
                if (cc.getExcelFile() != null && cc.getExcelFile().exists()) {
                    String fileName = cc.getExcelFile().getName();
                    String formaul = "";
                    if (fileName.lastIndexOf(".") >= 0) {
                        formaul = fileName.substring(fileName.lastIndexOf("."));
                    }
                    files.add(new ZipUnit(cc.getExcelFile(), cc.formatterName() + formaul));
                }
                if (cc.getVoiceFile() != null && cc.getVoiceFile().exists()) {
                    String fileName = cc.getVoiceFile().getName();
                    String formaul = "";
                    if (fileName.lastIndexOf(".") >= 0) {
                        formaul = fileName.substring(fileName.lastIndexOf("."));
                    }
                    files.add(new ZipUnit(cc.getVoiceFile(), cc.formatterName() + formaul));
                }
            }
        }
        return files;
    }
}
