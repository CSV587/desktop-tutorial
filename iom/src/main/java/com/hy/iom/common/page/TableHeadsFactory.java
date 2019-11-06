package com.hy.iom.common.page;

import java.util.ArrayList;
import java.util.List;

public class TableHeadsFactory {

    public static List<TableHead> createRecordInfoHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("callnumber", "客户编号"));
        tableHeads.add(new TableHead("ruleName", "规则名称"));
        tableHeads.add(new TableHead("recordStartTime", "开始呼叫时间"));
        tableHeads.add(new TableHead("recordEndTime", "结束呼叫时间"));
        tableHeads.add(new TableHead("duration", "时长"));
        tableHeads.add(new TableHead("callCount", "呼叫次数"));
        tableHeads.add(new TableHead("onState", "接通情况"));
        tableHeads.add(new TableHead("turnCount", "流转次数"));
        tableHeads.add(new TableHead("endNodeName", "挂机节点"));
        tableHeads.add(new TableHead("finalNodeName", "呼叫结果"));
        tableHeads.add(new TableHead("tagName", "标签结果"));
        return tableHeads;
    }

    public static List<TableHead> createErrorDetailHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("callnumber", "客户编号"));
        tableHeads.add(new TableHead("ruleName", "规则名称"));
        tableHeads.add(new TableHead("recordStartTime", "开始呼叫时间"));
        tableHeads.add(new TableHead("recordEndTime", "结束呼叫时间"));
        tableHeads.add(new TableHead("duration", "时长"));
        tableHeads.add(new TableHead("callCount", "呼叫次数"));
        tableHeads.add(new TableHead("onState", "接通情况"));
        tableHeads.add(new TableHead("turnCount", "流转次数"));
        tableHeads.add(new TableHead("type", "差错类型"));
        return tableHeads;
    }

    public static List<TableHead> createCallCntHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("projectName", "项目名称"));
        tableHeads.add(new TableHead("callDate", "呼叫日期"));
        tableHeads.add(new TableHead("dialCount", "已呼叫量"));
        tableHeads.add(new TableHead("connectCount", "接通量"));
        tableHeads.add(new TableHead("unconnectCount", "未接通量"));
        tableHeads.add(new TableHead("successRate", "成功率"));
        tableHeads.add(new TableHead("failRate", "失败率"));
        return tableHeads;
    }

    public static List<TableHead> createTradeStatistics() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("projectName", "项目名称"));
        tableHeads.add(new TableHead("ruleName", "规则名称"));
        tableHeads.add(new TableHead("callDate", "时间"));
        tableHeads.add(new TableHead("connectCount", "接通量"));
        tableHeads.add(new TableHead("unconnectCount", "未接通量"));
        tableHeads.add(new TableHead("successCount", "成功量"));
        tableHeads.add(new TableHead("failCount", "失败量"));
        tableHeads.add(new TableHead("successRate", "成功率"));
        tableHeads.add(new TableHead("failRate", "失败率"));
        return tableHeads;
    }

    //制造呼叫列表中添加结果标签
    public static List<TableHead> createRecordInfosHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("callnumber", "客户编号"));
        tableHeads.add(new TableHead("ruleName", "规则名称"));
        tableHeads.add(new TableHead("recordStartTime", "开始呼叫时间"));
        tableHeads.add(new TableHead("recordEndTime", "结束呼叫时间"));
        tableHeads.add(new TableHead("duration", "时长"));
        tableHeads.add(new TableHead("callCount", "呼叫次数"));
        tableHeads.add(new TableHead("onState", "接通情况"));
        tableHeads.add(new TableHead("turnCount", "流转次数"));
        tableHeads.add(new TableHead("endNodeName", "挂机节点"));
        tableHeads.add(new TableHead("finalNodeName", "呼叫结果"));
        tableHeads.add(new TableHead("tagStatus", "标签状态"));
        tableHeads.add(new TableHead("tagName", "标签结果"));
        return tableHeads;
    }
}
