package com.hy.cpic.base.page;

import java.util.ArrayList;
import java.util.List;

public class TableHeadsFactory {

    public static List<TableHead> createCallDetailHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("dateStr", "日期"));
        tableHeads.add(new TableHead("company", "分公司"));
        tableHeads.add(new TableHead("center", "职场"));
        tableHeads.add(new TableHead("area", "片区"));
        tableHeads.add(new TableHead("team", "团队"));
        tableHeads.add(new TableHead("dialTaskCount", "发起外呼任务总量"));
        tableHeads.add(new TableHead("dialSuccessCount", "外呼成功量"));
        tableHeads.add(new TableHead("dialFailureCount", "外呼失败量"));
        tableHeads.add(new TableHead("dialExceptionCount", "外呼异常量"));
        return tableHeads;
    }

    public static List<TableHead> createTaskListHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("dateStr", "日期"));
        tableHeads.add(new TableHead("company", "分公司"));
        tableHeads.add(new TableHead("center", "职场"));
        tableHeads.add(new TableHead("area", "片区"));
        tableHeads.add(new TableHead("team", "团队"));
        tableHeads.add(new TableHead("agentId", "座席工号"));
        tableHeads.add(new TableHead("carNumber", "车牌号"));
        tableHeads.add(new TableHead("policyNo", "保单号"));
        tableHeads.add(new TableHead("businessType", "业务类型"));
        tableHeads.add(new TableHead("productType", "产品类型"));
        tableHeads.add(new TableHead("recordId", "录音ID"));
        tableHeads.add(new TableHead("recordStartTime", "外呼开始时间"));
        tableHeads.add(new TableHead("recordEndTime", "外呼结束时间"));
        tableHeads.add(new TableHead("duration", "呼叫总时长"));
        tableHeads.add(new TableHead("callCount", "外呼次数"));
        tableHeads.add(new TableHead("callResult1", "一级外呼结果"));
        tableHeads.add(new TableHead("callResult2", "二级外呼结果"));
        return tableHeads;
    }

    public static List<TableHead> createCCMonitorHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("dateStr", "日期"));
        tableHeads.add(new TableHead("company", "分公司"));
        tableHeads.add(new TableHead("center", "职场"));
        tableHeads.add(new TableHead("area", "片区"));
        tableHeads.add(new TableHead("team", "团队"));
        tableHeads.add(new TableHead("timeSlot", "时段"));
        tableHeads.add(new TableHead("requestCount", "请求量"));
        tableHeads.add(new TableHead("realDialCount", "实际外呼量"));
        tableHeads.add(new TableHead("exceedingCount", "超出并发量"));
        return tableHeads;
    }

    public static List<TableHead> createCallBackRateHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("callBackTaskNum", "回访任务总量"));
        tableHeads.add(new TableHead("callBackTaskSuccessNum", "语音回访成功量"));
        tableHeads.add(new TableHead("successRate", "成功占比"));
        tableHeads.add(new TableHead("callBackTaskFailNum", "语音回访失败量"));
        tableHeads.add(new TableHead("failRate", "失败占比"));
        tableHeads.add(new TableHead("transFerNum", "转人工量"));
        tableHeads.add(new TableHead("transFerRate", "转人工占比"));
        return tableHeads;
    }

    public static List<TableHead> createCallBackListHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("dateStr", "任务生成日期"));
        tableHeads.add(new TableHead("company", "分公司"));
        tableHeads.add(new TableHead("center", "职场"));
        tableHeads.add(new TableHead("area", "片区"));
        tableHeads.add(new TableHead("team", "团队"));
        tableHeads.add(new TableHead("agentId", "坐席工号"));
        tableHeads.add(new TableHead("agentName", "坐席姓名"));
        tableHeads.add(new TableHead("carNumber", "车牌"));
        tableHeads.add(new TableHead("validDateStr", "生效日期"));
        tableHeads.add(new TableHead("businessType", "业务类型"));
        tableHeads.add(new TableHead("callBackType", "回访类型"));
        tableHeads.add(new TableHead("recordStartTime", "录音开始时间"));
        tableHeads.add(new TableHead("callResult1", "回访一级结果"));
        tableHeads.add(new TableHead("callResult2", "回访二级结果"));
        tableHeads.add(new TableHead("duration", "通话时长"));
        tableHeads.add(new TableHead("channeltype", "渠道标识"));
        return tableHeads;
    }

    public static List<TableHead> createIntentionRateHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("company", "分公司"));
        tableHeads.add(new TableHead("activityName", "营销活动名称"));
        tableHeads.add(new TableHead("callTotal", "呼叫总量"));
        tableHeads.add(new TableHead("successNum", "呼叫成功量"));
        tableHeads.add(new TableHead("successRate", "成功量占比"));
        tableHeads.add(new TableHead("failNum", "呼叫失败量"));
        tableHeads.add(new TableHead("failRate", "失败量占比"));
        return tableHeads;
    }

    public static List<TableHead> createIntentionStatisticsHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("company", "分公司"));
        tableHeads.add(new TableHead("sumCount", "名单输入量"));
        tableHeads.add(new TableHead("sumCon", "接通名单量"));
        tableHeads.add(new TableHead("conRate", "接通率"));
        tableHeads.add(new TableHead("validCount", "名单输出"));
        tableHeads.add(new TableHead("validRate", "输出率"));
        tableHeads.add(new TableHead("sumUnCon", "未接通"));
        tableHeads.add(new TableHead("hangupCount", "接通秒挂"));
        tableHeads.add(new TableHead("highLevelCount", "高级"));
        tableHeads.add(new TableHead("middleLevelCount", "中级"));
        tableHeads.add(new TableHead("lowLevelCount", "低级"));
        tableHeads.add(new TableHead("blackCount", "黑名单"));
        return tableHeads;
    }

    public static List<TableHead> createIntentionListHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("company", "分公司"));
        tableHeads.add(new TableHead("activityName", "营销活动名称"));
        tableHeads.add(new TableHead("recordStartTime", "录音开始时间"));
        tableHeads.add(new TableHead("recordEndTime", "录音结束时间"));
        tableHeads.add(new TableHead("callDuration", "通话时长"));
        tableHeads.add(new TableHead("cusNumber", "客户手机号码"));
        tableHeads.add(new TableHead("cusName", "客户姓名"));
        tableHeads.add(new TableHead("carNumber", "车牌号"));
        tableHeads.add(new TableHead("intentionClass", "意向分类"));
        tableHeads.add(new TableHead("allFlow", "是否全流程"));
        tableHeads.add(new TableHead("callResult", "呼叫结果"));
        tableHeads.add(new TableHead("interactions", "交互次数"));
        return tableHeads;
    }

    public static List<TableHead> createMarketRateHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("callNumber", "呼叫号码"));
        tableHeads.add(new TableHead("ruleName", "规则名称"));
        tableHeads.add(new TableHead("lastCallTime", "最后呼叫时间"));
        tableHeads.add(new TableHead("callCount", "呼叫次数"));
        tableHeads.add(new TableHead("connectState", "接通情况"));
        tableHeads.add(new TableHead("contactState", "接触情况"));
        tableHeads.add(new TableHead("activityDirection", "活动意向"));
        tableHeads.add(new TableHead("phoneIntention", "电话意向"));
//        tableHeads.add(new TableHead("hangupNode", "最后挂机节点"));
        tableHeads.add(new TableHead("callResult", "客户反馈"));
        tableHeads.add(new TableHead("masterHangupNode", "主流程挂机节点"));
        tableHeads.add(new TableHead("site", "地址"));
        tableHeads.add(new TableHead("belongTo", "是否是归属地"));
        tableHeads.add(new TableHead("finishMatchError", "匹配失败挂机"));
        return tableHeads;
    }

    public static List<TableHead> createCallNumConfigHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("name", "名称"));
        tableHeads.add(new TableHead("callMaxNum", "最大量"));
        tableHeads.add(new TableHead("path", "路径"));
        tableHeads.add(new TableHead("regexp", "正则"));
        tableHeads.add(new TableHead("ruleName", "所属规则"));
        tableHeads.add(new TableHead("projectName", "所属项目"));
        tableHeads.add(new TableHead("businessType", "业务类型"));
        tableHeads.add(new TableHead("totalNum", "剩余总量"));
        tableHeads.add(new TableHead("repeatNum", "重复次数"));
        tableHeads.add(new TableHead("execIp", "执行IP"));
        return tableHeads;
    }

    public static List<TableHead> createBranchCallNumConfigHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("branchName", "名称"));
        tableHeads.add(new TableHead("callNum", "呼叫量"));
        tableHeads.add(new TableHead("callMaxNum", "最大量"));
        tableHeads.add(new TableHead("callNumConfigName", "所属业务"));
        tableHeads.add(new TableHead("surplusNum", "剩余量"));
        return tableHeads;
    }

    public static List<TableHead> createReturnVisitBranchCallNumConfigHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("branchName", "名称"));
        tableHeads.add(new TableHead("callNumConfigName", "所属业务"));
        tableHeads.add(new TableHead("callNum", "呼叫量"));
        return tableHeads;
    }

    public static List<TableHead> createReturnVisitDataHead(String dimension) {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("callDate", "呼叫日期"));
        tableHeads.add(new TableHead("center", "职场"));
        if ("company".equals(dimension))
            tableHeads.add(new TableHead("company", "分公司"));
        tableHeads.add(new TableHead("taskNum", "任务总量"));
        tableHeads.add(new TableHead("returnNum", "已回访量"));
        tableHeads.add(new TableHead("coverRate", "覆盖率"));
        tableHeads.add(new TableHead("unReturnNum", "未回访量"));
        tableHeads.add(new TableHead("blackListNum", "黑名单量"));
        tableHeads.add(new TableHead("whiteListNum", "白名单量"));
        tableHeads.add(new TableHead("repeatNum", "除重名单量"));
        tableHeads.add(new TableHead("conversionNum", "转人工量"));
        tableHeads.add(new TableHead("connectedNum", "接通量"));
        tableHeads.add(new TableHead("connectedRate", "接通率"));
        tableHeads.add(new TableHead("recordingDuration", "通话时长"));
        tableHeads.add(new TableHead("unconnectedNum", "未接通量"));
        tableHeads.add(new TableHead("answerOneNum", "问卷1回访量"));
        tableHeads.add(new TableHead("answerOneRate", "问卷1占比"));
        tableHeads.add(new TableHead("answerTwoNum", "问卷2回访量"));
        tableHeads.add(new TableHead("answerTwoRate", "问卷2占比"));
        tableHeads.add(new TableHead("answerThreeNum", "问卷3回访量"));
        tableHeads.add(new TableHead("answerThreeRate", "问卷3占比"));
        tableHeads.add(new TableHead("answerFourNum", "问卷4回访量"));
        tableHeads.add(new TableHead("answerFourRate", "问卷4占比"));
        tableHeads.add(new TableHead("answerFiveNum", "问卷5回访量"));
        tableHeads.add(new TableHead("answerFiveRate", "问卷5占比"));
        tableHeads.add(new TableHead("overNum", "结束量"));
        tableHeads.add(new TableHead("overRate", "全流程率"));
        tableHeads.add(new TableHead("virtualNumberNum", "非真实号码量"));
        tableHeads.add(new TableHead("virtualNumberRate", "非真实号码占比"));
        tableHeads.add(new TableHead("onlineNumber", "线上引流量"));
        tableHeads.add(new TableHead("onlineRate", "线上引流率"));

        return tableHeads;
    }

    public static List<TableHead> createCaseReturnVisitHead(){
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("numberLocation","号码归属地"));
        tableHeads.add(new TableHead("insuredName","投保人姓名"));
        tableHeads.add(new TableHead("caseDate","事故日期"));
        tableHeads.add(new TableHead("caseTime","事故具体时间"));
        tableHeads.add(new TableHead("account","账户"));
        tableHeads.add(new TableHead("accountEndNumber","账户尾号"));
        tableHeads.add(new TableHead("caseEndNumber","案件尾号"));
        tableHeads.add(new TableHead("compensation","赔付金额"));
        tableHeads.add(new TableHead("commercialInsurance","商业险"));
        tableHeads.add(new TableHead("compulsoryInsurance","交强险"));
        tableHeads.add(new TableHead("callNumber","手机号码"));
        tableHeads.add(new TableHead("caseLocation","出险地址"));
        return tableHeads;
    }

    public static List<TableHead> createInsuredIntentionDataHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("company", "分公司"));
        tableHeads.add(new TableHead("activityName", "营销活动"));
//        tableHeads.add(new TableHead("acceptDate", "接收名单时间"));
//        tableHeads.add(new TableHead("acceptCount", "接收名单量"));
        tableHeads.add(new TableHead("calledCount", "已拨打名单量"));
        tableHeads.add(new TableHead("unCalledCount", "剩余未拨名单量"));
        tableHeads.add(new TableHead("highLevelCount", "意向反馈名单量-高意向"));
        tableHeads.add(new TableHead("middleLevelCount", "意向反馈名单量-中意向"));
        tableHeads.add(new TableHead("lowLevelCount", "意向反馈名单量-低意向"));
        tableHeads.add(new TableHead("blackCount", "意向反馈名单量-黑名单"));
        tableHeads.add(new TableHead("noMatchCount", "意向反馈名单量-接通且无匹配等级"));
        tableHeads.add(new TableHead("hangupCount", "意向反馈名单量-接通且秒挂"));
        tableHeads.add(new TableHead("noRecognitionCount", "意向反馈名单量-说话且无识别结果"));
        tableHeads.add(new TableHead("unconnectedCount", "意向反馈名单量-未接通"));
        tableHeads.add(new TableHead("intentionCount", "高级、中级、低级名单总量"));

        return tableHeads;
    }

    public static List<TableHead> createMarketListHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("ruleName", "所属规则"));
        tableHeads.add(new TableHead("taskName", "所属任务"));
        tableHeads.add(new TableHead("callNumber", "客户编号"));
        tableHeads.add(new TableHead("startTime", "开始时间"));
        tableHeads.add(new TableHead("endTime", "结束时间"));
        tableHeads.add(new TableHead("callCount", "呼叫次数"));
        tableHeads.add(new TableHead("callResult", "呼叫结果"));
        tableHeads.add(new TableHead("nodName", "挂机节点"));
        tableHeads.add(new TableHead("hangupSec", "挂机秒数"));
        return tableHeads;
    }

    public static List<TableHead> createInsuredIntentionRecordHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("dateRange", "时间范围"));

        tableHeads.add(new TableHead("calledCount", "总拨打名单量"));
        tableHeads.add(new TableHead("connectOnceCount", "一次拨打接通名单量"));//calledCount-(connectTwiceCount+unconnectCount)
        tableHeads.add(new TableHead("connectTwiceCount", "二次拨打接通名单量"));
        tableHeads.add(new TableHead("unconnectCount", "一直未接通名单量"));

        tableHeads.add(new TableHead("calledTimes", "总通次"));
        tableHeads.add(new TableHead("connectTimes", "接通通次"));
        tableHeads.add(new TableHead("unconnectTimes", "未接通通次"));
        tableHeads.add(new TableHead("connectAvgTimes", "通次接通率"));//connectTimes/calledTimes

        tableHeads.add(new TableHead("calledLength", "总时长（秒）"));
        tableHeads.add(new TableHead("connectLength", "接通总时长（振铃+通话）"));
        tableHeads.add(new TableHead("connectRingLength", "接通振铃时长"));
        tableHeads.add(new TableHead("connectAvgLength", "接通通话均时（秒）"));//connectLength/calledTimes
        tableHeads.add(new TableHead("connectChatLength", "有效通话时长"));
        tableHeads.add(new TableHead("connectChatAvgLength", "有效接通通均总时长（秒）"));//connectChatLength/calledTimes
        tableHeads.add(new TableHead("unconnectRingLength", "未接通振铃时长"));
        tableHeads.add(new TableHead("unconnectRingAvgLength", "未接通通均振铃时长（秒）"));//unconnectRingLength/calledTimes
        return tableHeads;
    }

    public static List<TableHead> createCodeTypeHead() {
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("type", "类型"));
        tableHeads.add(new TableHead("code", "代码"));
        tableHeads.add(new TableHead("content", "内容"));
        tableHeads.add(new TableHead("remark", "备注"));
        tableHeads.add(new TableHead("fst", "创建时间"));
        tableHeads.add(new TableHead("lmt", "修改时间"));
        tableHeads.add(new TableHead("foidName", "创建者"));
        tableHeads.add(new TableHead("loidName", "修改者"));
        return tableHeads;
    }
}
