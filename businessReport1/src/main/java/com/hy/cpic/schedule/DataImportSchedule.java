package com.hy.cpic.schedule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.cpic.base.api.AddressApi;
import com.hy.cpic.entities.CallInfoStatistics;
import com.hy.cpic.entities.InsuredIntentionOneMonth;
import com.hy.cpic.reporting.codetype.service.CodeTypeService;
import com.hy.cpic.reporting.intentionlist.service.IntentionListService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class DataImportSchedule {

    private final DataImportService dataImportService;

    private final AddressApi addressApi;

    private final CodeTypeService codeTypeService;

    @Value(value = "${import.oracle.projectMaps}")
    private String projectMaps;

    @Value(value = "${import.oracle.endNodeNames}")
    private String endNodeNames;

    @Transactional(rollbackFor = Exception.class)
    @KafkaListener(topics = "${import.oracle.topic}", groupId = "${import.oracle.groupId}")
    public void processRecordInfoMessage(ConsumerRecord record) {
        List<CallInfoStatistics> callinfoStatistics = new LinkedList<>();
        JSONArray jsonTextArray = new JSONArray();
        jsonTextArray.add(record.value());
        List<CallInfoStatistics> callInfoStatistics = processData(jsonTextArray);
        if (callInfoStatistics != null && callInfoStatistics.size() > 0) {
            callinfoStatistics.addAll(callInfoStatistics);
        }
        try {
            dataImportService.insertCallInfoStatistics(callinfoStatistics);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @KafkaListener(topics = "${import.oracle.overTopic}", groupId = "${import.oracle.groupId}")
    public void processMessage(ConsumerRecord record) {
        List<CallInfoStatistics> callinfoStatistics = new LinkedList<>();
        JSONArray jsonTextArray = new JSONArray();
        jsonTextArray.add(record.value());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int j = 0; j < jsonTextArray.size(); j++) {
            String next = jsonTextArray.getString(j);
            JSONObject item = JSONObject.parseObject(next);
            JSONObject customer = item.getJSONObject("customerInfo");
            CallInfoStatistics data = new CallInfoStatistics();
            data.setIsConnect("fail");
            data.setProType("车险订单");
            getCarInsuranceInfo(customer, data);
            data.setColumn2(customer.getString("职场"));
            data.setColumn3(customer.getString("片区"));
            data.setColumn4(customer.getString("团队"));
            String column5 = customer.getString("坐席工号");
            data.setColumn5(column5);
            String column6 = customer.getString("车牌号");
            data.setColumn6(column6);

            boolean jiaoqiang = customer.getBooleanValue("交强险");
            boolean lejia = customer.getBooleanValue("乐驾");
            boolean shanye = customer.getBooleanValue("商业险");


            String column7 = "";
            String column8 = customer.getString("业务类型");
            data.setColumn8(column8);

            //保单号
            if (jiaoqiang) {
                column7 += customer.getString("交强险投保单号");
            }
            if (lejia) {
                if (!column7.equals(""))
                    column7 = column7 + ",";
                column7 += customer.getString("乐驾投保单号");
            }
            if (shanye) {
                if (!column7.equals(""))
                    column7 = column7 + ",";
                column7 += customer.getString("商业险投保单号");
            }

            //产品类型
            String column9 = "";
            if (jiaoqiang && lejia && shanye) {
                column9 = "交商同出+乐驾";
            } else if (jiaoqiang && shanye) {
                column9 = "交商同出";
            } else if (shanye && lejia) {
                column9 = "单商业+乐驾";
            } else if (jiaoqiang && lejia) {
                column9 = "单交强+乐驾";
            } else if (lejia) {
                column9 = "乐驾";
            } else if (shanye) {
                column9 = "单商业";
            } else if (jiaoqiang) {
                column9 = "单交强";
            }
            data.setColumn7(column7);
            data.setColumn9(column9);

            try {
                data.setRecordStartTime(formatter.parse(item.getString("date")));
                data.setRecordEndTime(formatter.parse(item.getString("date")));
                data.setChannelStartTime(formatter.parse(item.getString("date")));
                data.setChannelEndTime(formatter.parse(item.getString("date")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            callinfoStatistics.add(data);
        }
        try {
            dataImportService.insertCallInfoStatistics(callinfoStatistics);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    private void getCarInsuranceInfo(JSONObject customer, CallInfoStatistics data) {
        String column1 = customer.getString("商业险分公司");
        if (column1 == null || column1.equals("")) {
            column1 = customer.getString("交强险分公司");
        }
        if (column1 == null || column1.equals("")) {
            column1 = customer.getString("乐驾分公司");
        }

        data.setColumn1(column1);
    }

    @Autowired
    public DataImportSchedule(DataImportService dataImportService, AddressApi api, CodeTypeService codeTypeService) {
        this.dataImportService = dataImportService;
        this.addressApi = api;
        this.codeTypeService = codeTypeService;
    }

    private List<CallInfoStatistics> processData(JSONArray jsonTextArray) {
        List<CallInfoStatistics> callinfoStatistics = new LinkedList<>();
        for (int j = 0; j < jsonTextArray.size(); j++) {
            String next = jsonTextArray.getString(j);
            if (StringUtils.isBlank(next)) {
                continue;
            }
            CallInfoStatistics recordInfo = parseJSON(next);
            if (recordInfo != null) {
                callinfoStatistics.add(recordInfo);
            }
        }
        return callinfoStatistics;
    }

    private CallInfoStatistics parseJSON(String item) {
        JSONObject obj = JSONObject.parseObject(item);
        JSONObject callInfo = obj.getJSONObject("callInfo");
        if (callInfo == null) {
            return null;
        }
        String project = callInfo.getString("projectId");
        JSONObject proMaps = JSONObject.parseObject(projectMaps);
        log.info("项目id=====" + obj.toString());
        if (proMaps.getString(project) == null) {
            return null;
        }
        switch (proMaps.getString(project)) {
            case "投保意向":
                CallInfoStatistics data = processIntention(obj);
                if (data == null) {
                    return null;
                }
                data.setProType("投保意向");
                return data;
            case "车险回访":
                data = processCallBack(obj);
                if (data == null) {
                    return null;
                }
                data.setProType("车险回访");
                return data;
            case "车险订单":
                data = processIndent(obj);
                if (data == null) {
                    return null;
                }
                data.setProType("车险订单");
                return data;
            case "寿险营销":
                data = processLifeIns(obj);
                data.setProType("寿险营销");
                return data;
            default:
                return null;
        }
    }

    private void initBaseData(CallInfoStatistics data, JSONObject obj) {
        JSONObject recordInfo = obj.getJSONObject("recordInfo");
        data.setUuid(recordInfo.getString("uuid"));
        data.setCallNumber(recordInfo.getString("callnumber"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            data.setChannelStartTime(formatter.parse(recordInfo.getString("channelStartTime")));
            data.setChannelEndTime(formatter.parse(recordInfo.getString("channelEndTime")));
            if (recordInfo.getString("recordStartTime") == null) {
                data.setRecordStartTime(formatter.parse(recordInfo.getString("channelStartTime")));
            } else {
                data.setRecordStartTime(formatter.parse(recordInfo.getString("recordStartTime")));
            }
            if (recordInfo.getString("recordEndTime") == null) {
                data.setRecordEndTime(formatter.parse(recordInfo.getString("channelEndTime")));
            } else {
                data.setRecordEndTime(formatter.parse(recordInfo.getString("recordEndTime")));
            }
            if (!recordInfo.getString("onState").equals("connect")) {
                data.setRecordStartTime(data.getRecordEndTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        data.setIsConnect(recordInfo.getString("onState"));
    }

    private CallInfoStatistics processIndent(JSONObject obj) {
        CallInfoStatistics callInfoStatistics = new CallInfoStatistics();
        initBaseData(callInfoStatistics, obj);
        JSONObject customerInfo = obj.getJSONObject("customerInfo");
        if (customerInfo == null) {
            return null;
        }
        getCarInsuranceInfo(customerInfo, callInfoStatistics);
        String column2 = customerInfo.getString("职场");
        callInfoStatistics.setColumn2(column2);
        String column3 = customerInfo.getString("片区");
        callInfoStatistics.setColumn3(column3);
        String column4 = customerInfo.getString("团队");
        callInfoStatistics.setColumn4(column4);
        String column5 = customerInfo.getString("坐席工号");
        callInfoStatistics.setColumn5(column5);
        String column6 = customerInfo.getString("车牌号");
        callInfoStatistics.setColumn6(column6);

        boolean jiaoqiang = customerInfo.getBooleanValue("交强险");
        boolean lejia = customerInfo.getBooleanValue("乐驾");
        boolean shanye = customerInfo.getBooleanValue("商业险");


        String column7 = "";
        String column8 = customerInfo.getString("业务类型");
        callInfoStatistics.setColumn8(column8);

        //保单号
        if (jiaoqiang) {
            column7 += customerInfo.getString("交强险投保单号");
        }
        if (lejia) {
            if (!column7.equals(""))
                column7 = column7 + ",";
            column7 += customerInfo.getString("乐驾投保单号");
        }
        if (shanye) {
            if (!column7.equals(""))
                column7 = column7 + ",";
            column7 += customerInfo.getString("商业险投保单号");
        }

        //产品类型
        String column9 = "";
        if (jiaoqiang && lejia && shanye) {
            column9 = "交商同出+乐驾";
        } else if (jiaoqiang && shanye) {
            column9 = "交商同出";
        } else if (shanye && lejia) {
            column9 = "单商业+乐驾";
        } else if (jiaoqiang && lejia) {
            column9 = "单交强+乐驾";
        } else if (lejia) {
            column9 = "乐驾";
        } else if (shanye) {
            column9 = "单商业";
        } else if (jiaoqiang) {
            column9 = "单交强";
        }
        callInfoStatistics.setColumn7(column7);
        callInfoStatistics.setColumn9(column9);

        JSONObject callInfo = obj.getJSONObject("callInfo");
        callInfoStatistics.setColumn10(callInfo.getString("callCount"));

        //一级呼叫结果
        String column11;
        //二级呼叫结果
        String column12;

        JSONObject resInfo = obj.getJSONObject("resInfo");
        boolean errFlag = false;
        boolean newServiceFlag = false;
        boolean cruxFlag = true;
        boolean unCruxFlag = true;
        String endNodeName = "";
        if (resInfo != null) {
            for (String key : resInfo.keySet()) {
                if (key.startsWith("异常_")) {
                    if (resInfo.getString(key).equals("fail")) {
                        errFlag = true;
                    }
                } else if (key.startsWith("新服务_")) {
                    if (resInfo.getString(key).equals("fail")) {
                        newServiceFlag = true;
                    }
                } else if (key.startsWith("关键_")) {
                    if (resInfo.getString(key).equals("fail")) {
                        cruxFlag = false;
                    }
                } else if (key.startsWith("非关键_")) {
                    if (resInfo.getString(key).equals("fail")) {
                        unCruxFlag = false;
                    }
                }
            }
            endNodeName = resInfo.getString("endNodeName");
        }
        if (!callInfoStatistics.getIsConnect().equals("connect")) {
            column12 = "1";
            column11 = "1";
        } else {
            if (checkEndNodeName(endNodeName)) {
                if ((!unCruxFlag)) {
                    column12 = "4";
                    column11 = "1";
                } else {
                    column12 = "0";
                    column11 = "0";
                }
            } else {
                if ((!unCruxFlag) && (!cruxFlag)) {
                    column12 = "102";
                    column11 = "1";
                } else if ((!unCruxFlag) && errFlag) {
                    column12 = "103";
                    column11 = "1";
                } else if ((!unCruxFlag) && newServiceFlag) {
                    column12 = "105";
                    column11 = "1";
                } else if (errFlag) {
                    column12 = "3";
                    column11 = "1";
                } else if (!cruxFlag) {
                    column12 = "2";
                    column11 = "1";
                } else if (newServiceFlag) {
                    column12 = "5";
                    column11 = "1";
                } else {
                    column12 = "4";
                    column11 = "1";
                }
            }
        }
        //呼叫一级结果
        callInfoStatistics.setColumn11(column11);
        //呼叫二级结果
        callInfoStatistics.setColumn12(column12);
        //通话时长
        callInfoStatistics.setColumn13(String.valueOf((callInfoStatistics.getRecordEndTime().getTime() - callInfoStatistics.getRecordStartTime().getTime()) / 1000));
        //外呼结果
        callInfoStatistics.setColumn14(callInfoStatistics.getColumn12().equals("0") ? "0" : "1");

        //add
        boolean flag = addDxResult(callInfoStatistics, obj);


        return callInfoStatistics;
    }

    private boolean checkEndNodeName(String endNodeName) {
        if (endNodeName == null) {
            return false;
        }
        endNodeName = endNodeName.replace("tts", "");
        String[] nodeNames = endNodeNames.split(",");
        for (String item : nodeNames) {
            if (endNodeName.equals(item)) {
                return true;
            }
        }
        return false;
    }

    private void handlerAnswer(CallInfoStatistics callInfoStatistics,
                               JSONObject obj) {
        String answer1 = "";
        String answer2 = "";
        String answer3 = "";
        String answer4 = "";
        String answer5 = "";
        String column23 = null;
        JSONArray content = obj.getJSONArray("content");
        if (content != null) {
            String preNodeName = "";
            for (int index = 0; index < content.size(); index++) {
                JSONObject item = content.getJSONObject(index);
                switch (item.getString("type")) {
                    case "tts":
                        if ((!item.getString("nodeName").endsWith("ttscallBack"))
                            && (!item.getString("nodeName").endsWith("-结束"))
                            && (!item.getString("nodeName").endsWith("-fw"))) {
                            preNodeName = item.getString("nodeName");
                            column23 = item.getString("nodeName");
                        }
                        break;
                    case "speechSuccess":
                    case "speechOverTime":
                        String value = item.getString("content");
                        switch (preNodeName) {
                            case "核身":
                                answer1 = value;
                                break;
                            case "是否收到保单":
                                answer2 = value;
                                break;
                            case "是否满意":
                                answer3 = value;
                                break;
                            case "推荐打分":
                                answer4 = value;
                                break;
                            case "进一步改善":
                                answer5 = value;
                                break;
                            case "推荐理由":
                                answer5 = value;
                                break;
                            case "中等评价":
                                answer5 = value;
                                break;
                            default:
                                break;
                        }
                        break;
                    case "matchSucess":
                        String tmpValue = item.getString("nodeName").replaceAll("match", "");
                        switch (tmpValue) {
                            case "核身-非本人":
                            case "核身-信息错误":
                            case "核身-是本人":
                                callInfoStatistics.setColumn17(answer1);
                                break;
                            case "是否收到保单-没时间收":
                            case "是否收到保单-没有收到保单":
                            case "是否收到保单-有收到保单":
                                callInfoStatistics.setColumn18(answer2);
                                break;
                            case "是否满意-不满意":
                            case "是否满意-满意":
                                callInfoStatistics.setColumn19(answer3);
                                break;
                            case "推荐打分-6分及以下":
                            case "推荐打分-9-10分":
                            case "推荐打分-7-8分":
                                callInfoStatistics.setColumn20(answer4);
                                break;
                            case "进一步改善-客户任意回答":
                            case "推荐理由-客户任意回答":
                            case "中等评价-客户任意回答":
                                callInfoStatistics.setColumn21(answer5);
                                break;
                            case "不满意原因咨询-客户任意回答":
                                callInfoStatistics.setColumn22(item.getString("content"));
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        callInfoStatistics.setColumn23(column23);
    }

    private CallInfoStatistics processCallBack(JSONObject obj) {
        CallInfoStatistics callInfoStatistics = new CallInfoStatistics();
        initBaseData(callInfoStatistics, obj);
        JSONObject customerInfo = obj.getJSONObject("customerInfo");
        if (customerInfo == null) {
            return null;
        }
        String column1 = customerInfo.getString("分公司");
        callInfoStatistics.setColumn1(column1);
        String column2 = customerInfo.getString("职场");
        callInfoStatistics.setColumn2(column2);
        String column3 = customerInfo.getString("片区");
        callInfoStatistics.setColumn3(column3);
        String column4 = customerInfo.getString("团队");
        callInfoStatistics.setColumn4(column4);
        String column5 = customerInfo.getString("坐席工号");
        callInfoStatistics.setColumn5(column5);
        String column6 = customerInfo.getString("坐席姓名");
        callInfoStatistics.setColumn6(column6);
        String column7 = customerInfo.getString("车牌");
        callInfoStatistics.setColumn7(column7);
        String column8 = customerInfo.getString("业务类型");
        callInfoStatistics.setColumn8(column8);
        String column9 = customerInfo.getString("回访类型");
        callInfoStatistics.setColumn9(column9);
        String column25 = customerInfo.getString("渠道标识");
        if (column25 != null && !column25.equals("")) {
            if (column25.equals("R")) {
                callInfoStatistics.setColumn25("R");
            } else {
                callInfoStatistics.setColumn25("其它");
            }
        } else {
            callInfoStatistics.setColumn25("其它");
        }


        //回访一级结果
        String column10;
        if (callInfoStatistics.getIsConnect().equals("connect")) {
            column10 = "0";
        } else {
            column10 = "1";
        }
        callInfoStatistics.setColumn10(column10);

        callInfoStatistics.setColumn17("-");
        callInfoStatistics.setColumn18("-");
        callInfoStatistics.setColumn19("-");
        callInfoStatistics.setColumn20("-");
        callInfoStatistics.setColumn21("-");

        String column24 = "";

        //回访二级结果
        String column11;
        if (!callInfoStatistics.getIsConnect().equals("connect")) {
            column11 = "2";
        } else {
            JSONObject resInfo = obj.getJSONObject("resInfo");
            boolean successFlag = false;
            boolean transferFlag = false;
            if (resInfo != null) {
                for (String key : resInfo.keySet()) {
                    if (key.startsWith("成功_")) {
                        if (resInfo.getString(key).equals("success")) {
                            successFlag = true;
                        }
                    } else if (key.startsWith("转人工_")) {
                        if (resInfo.getString(key).equals("success")) {
                            transferFlag = true;
                        }
                    }
                }
                column24 = resInfo.getString("callResult");
            }

            if (transferFlag) {
                column11 = "1";
            } else if (successFlag) {
                column11 = "0";
            } else {
                column11 = "3";
            }

            if ((resInfo != null) && (resInfo.getString("黑名单") != null)) {
                column11 = "4";
            }

            handlerAnswer(callInfoStatistics, obj);
        }
        callInfoStatistics.setColumn11(column11);

        //通话时长
        callInfoStatistics.setColumn12(String.valueOf((callInfoStatistics.getRecordEndTime().getTime() - callInfoStatistics.getRecordStartTime().getTime()) / 1000));

        //外呼结果
        callInfoStatistics.setColumn13(callInfoStatistics.getColumn11().endsWith("0") ? "0" : "1");
        String column14 = customerInfo.getString("生效日期");
        callInfoStatistics.setColumn14(column14);
        String column15 = customerInfo.getString("回访名单任务生成日期");
        callInfoStatistics.setColumn15(column15);
        String column16 = customerInfo.getString("名单ID");
        callInfoStatistics.setColumn16(column16);

        if (column24 == null) {
            column24 = callInfoStatistics.getColumn23();
        }
        callInfoStatistics.setColumn24(column24);

        dataImportService.updateCallInfo(callInfoStatistics);

        return callInfoStatistics;
    }

    private CallInfoStatistics processIntention(JSONObject obj) {
        CallInfoStatistics callInfoStatistics = new CallInfoStatistics();
        initBaseData(callInfoStatistics, obj);
        JSONObject customerInfo = obj.getJSONObject("customerInfo");
        if (customerInfo == null) {
            return null;
        }
        String column1 = customerInfo.getString("分公司");
        callInfoStatistics.setColumn1(column1);
        String column2 = customerInfo.getString("营销活动名称");
        callInfoStatistics.setColumn2(column2);
        String column3 = customerInfo.getString("客户姓名");
        callInfoStatistics.setColumn3(column3);
        String column4 = customerInfo.getString("车牌号");
        callInfoStatistics.setColumn4(column4);
        String column16 = customerInfo.getString("终保日期");
        callInfoStatistics.setColumn16(column16);
        String column17 = customerInfo.getString("名单类型");
        callInfoStatistics.setColumn17(column17);
        String column19 = customerInfo.getString("isLast");
        callInfoStatistics.setColumn19(column19);
        String column21 = customerInfo.getString("firstCallNumber");
        callInfoStatistics.setColumn21(column21);

        //意向分类
        String column5;

        //是否全流程
        String column6 = "";

        //呼叫结果
        String column7 = "未接通";

        JSONObject resInfo = obj.getJSONObject("resInfo");
        if (resInfo != null) {

            column6 = "否";
            column7 = "不考虑";

            if (resInfo.getString("低级") != null) {
                column6 = "是";
                column7 = resInfo.getString("低级");
            }

            if (resInfo.getString("中级") != null) {
                column6 = "是";
                column7 = resInfo.getString("中级");
            }

            if (resInfo.getString("高级") != null) {
                column6 = "是";
                column7 = resInfo.getString("高级");
            }

            if (column6.equals("否")) {
                if (resInfo.getString("问题") != null) {
                    column7 = resInfo.getString("问题");
                    if (column7.equals("机器人")) {
                        column6 = "是";
                    }
                }
            }
            if ((resInfo.getString("endNodeName") != null) && resInfo.getString("endNodeName").startsWith("机器人")) {
                column6 = "是";
                column7 = "机器人";
            }
        }

        int count = 0;

        //非秒挂但未说话
        String column15 = "非秒挂未说话";

        JSONArray contents = obj.getJSONArray("content");
        boolean noMeet = true;
        boolean noMatch = true;
        if (contents != null) {
            for (int index = 0; index < contents.size(); index++) {
                JSONObject item = contents.getJSONObject(index);
                if (item.getString("type").equals("tts")) {
                    count++;
                }
                if (noMeet && item.getString("type").equals("speechSuccess")
                    && !item.getString("content").equals("")
                    && !item.getString("content").equals("null")) {
                    noMeet = false;
                }

                if (noMatch && item.getString("type").equals("matchSucess")) {
                    noMatch = false;
                }
            }
        }

        if (count > 0 && (count == 1 || noMeet || noMatch)) {
            column7 = "可接通";
        }
        if (noMeet) {
            callInfoStatistics.setColumn15(column15);
        }

        String callResultCode = IntentionListService.getResultCode(column7);

        String[] highLevel = {"1"};
        String[] middleLevel = {"2", "3", "4", "5", "6", "7", "8", "9",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "29", "32", "33", "34", "35"};
        String[] lowLevel = {"19", "20", "21", "22", "23"};
        String[] blackLevel = {"24", "25", "26", "27", "28"};
        String[] connectLevel = {"30"};
        if (Arrays.asList(highLevel).contains(callResultCode)) {
            column5 = "高级";
        } else if (Arrays.asList(middleLevel).contains(callResultCode)) {
            column5 = "中级";
        } else if (Arrays.asList(lowLevel).contains(callResultCode)) {
            column5 = "低级";
        } else if (Arrays.asList(blackLevel).contains(callResultCode)) {
            column5 = "黑名单";
        } else if (Arrays.asList(connectLevel).contains(callResultCode)) {
            column5 = "接通";
        } else {
            column5 = "未接通";
        }

        callInfoStatistics.setColumn5(column5);
        callInfoStatistics.setColumn6(column6);
        callInfoStatistics.setColumn7(column7);

        //通话时长
        callInfoStatistics.setColumn8(String.valueOf((callInfoStatistics.getRecordEndTime().

            getTime() - callInfoStatistics.getRecordStartTime().

            getTime()) / 1000));

        //外呼结果
        callInfoStatistics.setColumn9(column7);

        String column10 = customerInfo.getString("营销活动ID");
        callInfoStatistics.setColumn10(column10);

        String column11 = customerInfo.getString("名单ID");
        callInfoStatistics.setColumn11(column11);

        //流程交互次数
        callInfoStatistics.setColumn12(String.valueOf(count));

        JSONObject callInfo = obj.getJSONObject("callInfo");
        if (callInfo == null) {
            return null;
        }
        callInfoStatistics.setCallNumber(callInfo.getString("customerId"));

        String column13 = callInfo.getString("taskName");
        callInfoStatistics.setColumn13(column13);

        String column14 = callInfo.getString("taskId");
        callInfoStatistics.setColumn14(column14);

        callInfoStatistics.setColumn18(callInfo.getString("callCount"));

        InsuredIntentionOneMonth insuredIntentionOneMonth = new InsuredIntentionOneMonth();
        insuredIntentionOneMonth.setCallNumber(callInfo.getString("customerId"));
        insuredIntentionOneMonth.setFst(callInfoStatistics.getChannelStartTime());
        insuredIntentionOneMonth.setCallResult(callInfoStatistics.getColumn7());
        insuredIntentionOneMonth.setIsConnect(callInfoStatistics.getIsConnect());
        insuredIntentionOneMonth.setIntentionLevel(callInfoStatistics.getColumn5());
        insuredIntentionOneMonth.setTimes(1);
        dataImportService.saveInsuredIntention(insuredIntentionOneMonth);
        log.info("save one month record success");

        return callInfoStatistics;
    }

    private void lifeNode1(JSONArray content,
                           CallInfoStatistics callInfoStatistics) {
        //主流程挂机节点
        String column8 = "";

        //匹配失败挂机
        String column9 = "";
        for (int index = content.size() - 1; index >= 0; index--) {
            JSONObject item = content.getJSONObject(index);
            if (column8.equals("") && item.getString("type").equals("tts")
                && (!item.getString("nodeName").endsWith("ttscallBack"))
                && (!item.getString("nodeName").endsWith("-结束"))
                && (!item.getString("nodeName").endsWith("-fw"))) {
                column8 = item.getString("nodeName");
            }

            if (column9.equals("") &&
                (item.getString("type").equals("matchSucess")
                    || item.getString("type").equals("matchError"))) {
                if (item.getString("type").equals("matchSucess")) {
                    column9 = "-";
                } else if (item.getString("type").equals("matchError")) {
                    for (int j = index - 1; j >= 0; j--) {
                        JSONObject cItem = content.getJSONObject(j);
                        if (cItem.getString("type").equals("tts")) {
                            column9 = "匹配失败";
                            break;
                        }
                    }
                }
            }

            if ((!column8.equals("")) && (!column9.equals(""))) {
                break;
            }
        }
        callInfoStatistics.setColumn8(column8);
        callInfoStatistics.setColumn9(column9);
    }

    private void lifeNode2(JSONArray content,
                           CallInfoStatistics callInfoStatistics) {
        String column12 = "";
        String column13 = "0";
        String preNode = "";
        long preTime = 0;
        boolean calculate = false;
        boolean breakFlag = false;
        for (int index = 0; index < content.size(); index++) {
            JSONObject item = content.getJSONObject(index);
            String type = item.getString("type");
            switch (type) {
                case "tts":
                    if (!(item.getString("nodeName").endsWith("ttscallBack")
                        || item.getString("nodeName").endsWith("-fw")
                        || item.getString("nodeName").endsWith("-结束"))) {
                        breakFlag = false;
                        if (calculate) {
                            column12 = item.getString("nodeName");
                            preNode = item.getString("nodeName");
                            preTime = item.getLongValue("intervalSeconds");
                        }
                    }
                    break;
                case "calculate":
                    calculate = item.getString("content").endsWith("最后值为[0]");
                    break;
                case "hangup":
                case "error":
                case "end":
                    if (!preNode.equals("") && preTime != 0 && !breakFlag) {
                        long current = item.getLongValue("intervalSeconds");
                        DecimalFormat df = new DecimalFormat("0.00");
                        column13 = df.format((float) (current - preTime) / 1000);
                    }
                    break;
                case "breakoff":
                    if (!preNode.equals("") && preTime != 0) {
                        long current = item.getLongValue("intervalSeconds");
                        DecimalFormat df = new DecimalFormat("0.00");
                        column13 = df.format((float) (current - preTime) / 1000);
                    }
                    breakFlag = true;
                    break;
                default:
                    break;
            }
        }
        callInfoStatistics.setColumn12(column12);
        callInfoStatistics.setColumn13(column13);
    }

    private CallInfoStatistics processLifeIns(JSONObject obj) {
        CallInfoStatistics callInfoStatistics = new CallInfoStatistics();
        initBaseData(callInfoStatistics, obj);
        JSONObject callInfo = obj.getJSONObject("callInfo");
        callInfoStatistics.setColumn1(callInfo.getString("ruleName"));
        callInfoStatistics.setColumn2(callInfo.getString("callCount"));
        String column14 = callInfo.getString("taskName");
        callInfoStatistics.setColumn14(column14);

        callInfoStatistics.setColumn15(callInfo.getString("projectId"));
        callInfoStatistics.setColumn16(callInfo.getString("ruleId"));
        callInfoStatistics.setColumn17(callInfo.getString("taskId"));
        callInfoStatistics.setColumn18(String.valueOf(
            dataImportService.queryCallCount(
                callInfoStatistics.getCallNumber())));

        //接触情况
        String column3 = "否";
        //活动意向
        String column4 = "否";
        //电话意向
        String column5 = "否";
        //挂机节点
        String column6 = "";

        //呼叫结果
        String column7 = "";


        //地址
        String column10 = "";

        String address = "";

        String defaultAddress = "";

        JSONObject resInfo = obj.getJSONObject("resInfo");
        if (resInfo != null) {
            for (String key : resInfo.keySet()) {
                switch (key) {
                    case "接触情况":
                        column3 = resInfo.getString(key);
                        break;
                    case "活动意向":
                        column4 = resInfo.getString(key);
                        break;
                    case "电话意向":
                        column5 = resInfo.getString(key);
                        break;
                    case "endNodeName":
                        column6 = resInfo.getString(key);
                        break;
                    default:
                        break;
                }
            }

            String callResult = resInfo.getString("callResult");
            if (callResult == null || callResult.equals("")) {
                column7 = column6;
            } else {
                column7 = callResult;
            }
            address = resInfo.getString("地址");
            defaultAddress = resInfo.getString("问题显示");
        }

        if (address == null) {
            address = "";
        }


        JSONArray content = obj.getJSONArray("content");
        if (content != null) {

            lifeNode1(content, callInfoStatistics);

            for (int index = content.size() - 1; index >= 0; index--) {
                JSONObject item = content.getJSONObject(index);
                if (item.getString("type").equals("speechSuccess") && item.getString("nodeName").startsWith("询问地址")) {
                    String text = item.getString("content");
                    log.debug("识别文本：{}", String.format("[%s]%s", defaultAddress, text));
                    column10 = addressApi.getAddress(String.format("[%s]%s", defaultAddress, text));
                    log.debug("识别地址：{}", column10);
                    break;
                }
            }

            lifeNode2(content, callInfoStatistics);
        }
        String column11 = "";
        if (column10 == null || column10.equals("")) {
            column10 = address;
            if (address.equals("非归属地")) {
                column11 = "否";
            } else if (address.equals("是归属地")) {
                column11 = "是";
            }
        } else if (column10.startsWith(defaultAddress)) {
            column11 = "是";
        } else {
            column11 = column10;
        }
        callInfoStatistics.setColumn3(column3);
        callInfoStatistics.setColumn4(column4);
        callInfoStatistics.setColumn5(column5);
        callInfoStatistics.setColumn6(column6);
        callInfoStatistics.setColumn7(column7);
        callInfoStatistics.setColumn10(column10);
        callInfoStatistics.setColumn11(column11);

        return callInfoStatistics;
    }

    boolean addDxResult(CallInfoStatistics callInfoStatistics, JSONObject obj) {


        String httpCode = codeTypeService.queryContentByCodeAndType("dxresult", "httpDxResult");
        if (httpCode == null || httpCode.equals("")) {
            log.info("未配置返回电销结果接口地址");
            return true;
        }


        JSONObject customerInfo = obj.getJSONObject("customerInfo");
//        log.info("customerInfoJSON===="+JSON.toJSONString(customerInfo));
        JSONObject callInfo = obj.getJSONObject("callInfo");
//        log.info("callInfoJSON ==== "+JSON.toJSONString(callInfo));
        SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        if (callInfoStatistics.getIsConnect().equals("unconnect") && callInfo.getString("callCount").equals(1)) {
            return true;
        }
        try {
            TransformerHandler handler = factory.newTransformerHandler();
            Transformer transformer = handler.getTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "no");

            StringWriter stringWriter = new StringWriter();
            Result streamResult = new StreamResult(stringWriter);
            handler.setResult(streamResult);
            AttributesImpl attr = new AttributesImpl();

            handler.startDocument();
            handler.startElement("", "", "document", attr);
            handler.startElement("", "", "request", attr);

            handler.startElement("", "", "body", attr);

            String ringDate = getStrByDate(callInfoStatistics.getChannelStartTime());
            createElement(handler, attr, "ringDate", ringDate);
            createElement(handler, attr, "connId", callInfoStatistics.getUuid());
            //没有
            createElement(handler, attr, "dnis", callInfoStatistics.getCallNumber());
            createElement(handler, attr, "customerName", customerInfo.getString("投保人姓名"));
            createElement(handler, attr, "workNo", callInfoStatistics.getColumn5());
            createElement(handler, attr, "salesId", customerInfo.getString("salesId"));

            createElement(handler, attr, "trafficApplicationNo", customerInfo.getString("交强险投保单号"));
            createElement(handler, attr, "businessApplicationNo", customerInfo.getString("商业险投保单号"));
            createElement(handler, attr, "noncarApplicationNo", customerInfo.getString("乐驾投保单号"));
            createElement(handler, attr, "regScale", customerInfo.getString("regScale"));
            createElement(handler, attr, "vehicleLicense", callInfoStatistics.getColumn6());
//            createElement(handler, attr, "regScale", customInfo.getString("regScale"));
            //分公司代码
            createElement(handler, attr, "subCompany", customerInfo.getString("subCompanyCode"));

            if (callInfoStatistics.getIsConnect() != "unconnect") {
                String answerDtae = getStrByDate(callInfoStatistics.getRecordStartTime());
                createElement(handler, attr, "answerDtae", answerDtae);
                String enddate = getStrByDate(callInfoStatistics.getRecordEndTime());
                createElement(handler, attr, "endDate", enddate);
                createElement(handler, attr, "recordLength", callInfoStatistics.getColumn13());
                createElement(handler, attr, "callResult1", callInfoStatistics.getColumn11());
                createElement(handler, attr, "callResult2", callInfoStatistics.getColumn12());
            } else {
                createElement(handler, attr, "callResult1", "1");
                createElement(handler, attr, "callResult2", "1");
            }


            handler.endElement("", "", "body");
            handler.endElement("", "", "request");
            handler.endElement("", "", "document");
            handler.endDocument();
            log.info("cpic发送数据-" + stringWriter.toString());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("application/xml;charset=UTF-8"));
            HttpEntity entity = new HttpEntity(stringWriter.toString(), headers);
            RestTemplate restTemplate = new RestTemplate();
            //"http://10.186.127.235:31004/webservice/NewCpicTelSaleService/orderConfirmationResults.xml"
            ResponseEntity<String> responseEntity = restTemplate
                .exchange(httpCode,
                    HttpMethod.POST, entity, String.class);
            log.info("接收数据-" + responseEntity.getBody());
        } catch (SAXException e) {
            log.info("创建xml工厂有问题。。。。。");
        } catch (TransformerConfigurationException e) {
            log.info("组合xml文件有问题。。。。。");
        }

        return true;
    }

    private static void createElement(TransformerHandler handler, AttributesImpl attr, String targetName, String value) {
        try {
            handler.startElement("", "", targetName, attr);
            if (StringUtils.isEmpty(value)) {
                handler.endElement("", "", targetName);
                return;
            }
            char[] valueChar = value.toCharArray();
            handler.characters(valueChar, 0, valueChar.length);
            handler.endElement("", "", targetName);
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }

    private String getStrByDate(Date date) {
        SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = st.format(date);
        return str;
    }


}
