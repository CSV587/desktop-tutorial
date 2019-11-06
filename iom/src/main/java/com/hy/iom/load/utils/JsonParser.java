package com.hy.iom.load.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hy.iom.entities.*;
import com.hy.iom.service.CallContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 解析Json
 */
public class JsonParser {
    private static Logger log = LoggerFactory.getLogger(JsonParser.class);

    /**
     * .
     * 判断是否为数字
     *
     * @param str 字符串
     * @return 结果
     */
    private static boolean isInteger(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 解析一个文件中的数据，并根据不同的数据结构保存到不同的List中：
     * RecordInfo属性固定
     * callContent属性固定
     * ResInfo属性不固定
     * CustomerInfo属性不固定
     */
    public static Map<String, List<?>> parseCallDetails(final List<String> recordInfos) throws IOException {
        Map<String, List<?>> map = new HashMap<>();
        List<RecordInfo> ls_recordInfo = new ArrayList<>();
        List<CallContent> ls_callContent = new ArrayList<>();
        List<CustomerInfo> ls_customerInfo = new ArrayList<>();
        List<ResInfo> ls_resInfo = new ArrayList<>();
        List<InfoReflex> ls_infoReflex = new ArrayList<>();
        List<CallContentDetail> ls_callContentDetail = new ArrayList<>();
        List<SceneThroughDetail> ls_throughDetail = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        Map<String, Map<String, InfoReflex>> infoMap = new HashMap<>();
        for (String next : recordInfos) {
            if (StringUtils.isEmpty(next) || next.equals("")) {
                continue;
            }
            log.debug("line==" + next);
            //解析每行的JSON数据
            String uuid = parseUuid(mapper, next);
            log.info("uuid=" + uuid);
            if ((uuid == null) || uuid.equals("")) {
                log.warn("uuid为空，数据异常，丢弃。{}", next);
                continue;
            }

            CallContentDetail callContentDetail = new CallContentDetail();
            callContentDetail.setUuid(uuid);

            String tradeState = parseTradeState(mapper, next);
            String callResult = parseCallResult(mapper, next);
            String blackListFlag = parseBlackList(mapper, next);

            if (isInteger(blackListFlag)
                && !blackListFlag.equals("0")) {
                blackListFlag = "是";
            } else {
                blackListFlag = "否";
            }

            String endNodeName = "";
            CallDetails callDetails = mapper.readValue(next, CallDetails.class);
            log.debug("callDetails==" + callDetails);
            int turnCount = 0;
            List<CallContent> content = callDetails.getContent();
            SceneThroughDetail sceneThroughDetail = null;
            boolean matchFlag = false;
            boolean recFlag = false;
            boolean ttsFlag = false;
            if (null != content) {
                int seq = 0;
                for (CallContent cc : content) {
                    cc.setUuid(uuid);
                    if ("tts".equals(cc.getType())) {
                        if (!(cc.getNodeName().endsWith("ttscallBack")
                            || cc.getNodeName().endsWith("-结束")
                            || cc.getNodeName().endsWith("-fw"))) {
                            endNodeName = cc.getNodeName();
                            if (sceneThroughDetail != null) {
                                if (!recFlag) {
                                    sceneThroughDetail.setThroughFlag(SceneThroughDetail.ThroughState.noSpeak);
                                } else if (matchFlag) {
                                    sceneThroughDetail.setThroughFlag(SceneThroughDetail.ThroughState.through);
                                }
                            }
                            sceneThroughDetail = new SceneThroughDetail();
                            sceneThroughDetail.setUuid(uuid);
                            sceneThroughDetail.setNodeName(cc.getNodeName());
                            sceneThroughDetail.setThroughFlag(SceneThroughDetail.ThroughState.notThrough);
                            sceneThroughDetail.setSeq(seq);
                            seq++;
                            ls_throughDetail.add(sceneThroughDetail);
                        }
                        if (!ttsFlag) {
                            ttsFlag = true;
                        }
                    } else {
                        ttsFlag = false;
                    }
                    if ("matchSucess".equals(cc.getType())) {
                        matchFlag = true;
                    }
                    if ("matchError".equals(cc.getType())) {
                        matchFlag = false;
                    }
                    if ("speechSuccess".equals(cc.getType())) {
                        turnCount++;
                        recFlag = true;
                    }
                    if ("speechOverTime".equals(cc.getType())) {
                        recFlag = false;
                    }
                    if ("hangup".equals(cc.getType())) {
                        if (sceneThroughDetail != null) {
                            sceneThroughDetail.setThroughFlag(SceneThroughDetail.ThroughState.hangup);
                        }
                    }
                    if ("end".equals(cc.getType())) {
                        if (sceneThroughDetail != null) {
                            sceneThroughDetail.setThroughFlag(SceneThroughDetail.ThroughState.through);
                        }
                    }
                    ls_callContent.add(cc);
                }
            }
            log.info("endNodeName=" + endNodeName);

            RecordInfo recordInfo = callDetails.getRecordInfo();
            BeanUtils.copyProperties(callDetails.getCallInfo(), recordInfo);
            String projectId = recordInfo.getProjectId();
            JsonNode callInfo = mapper.readTree(next).path("callInfo");
            if (callInfo != null) {
                JsonNode customerId = callInfo.path("customerId");
                if (customerId != null) {
                    recordInfo.setCallNumber(customerId.textValue());
                }
            }
            recordInfo.setEndNodeName(endNodeName);
            recordInfo.setTradeState(tradeState);
            recordInfo.setCallResult(callResult);
            recordInfo.setBlackListFlag(blackListFlag);
            log.info("recordInfo total = " + recordInfo);
            recordInfo.setTurnCount(turnCount);
            ls_recordInfo.add(recordInfo);

            JsonNode jsonNode = mapper.readTree(next);
            List<CustomerInfo> customerInfos = parseCustomerInfo(uuid, jsonNode);
            ls_customerInfo.addAll(customerInfos);
            ls_resInfo.addAll(parseResInfo(uuid, jsonNode));

            for (CustomerInfo customerInfo : customerInfos) {
                String name = customerInfo.getName();
                InfoReflex infoReflex = new InfoReflex();
                infoReflex.setProjectId(projectId);
                infoReflex.setName(name);
                infoReflex.setState("0");
                Map<String, InfoReflex> item = infoMap.computeIfAbsent(projectId, k -> new HashMap<>());
                item.putIfAbsent(name, infoReflex);
            }

            callContentDetail.setCustomInfo(list2JSONArray(customerInfos).toJSONString());
            callContentDetail.setCallContent(processContent(content, uuid).toJSONString());
            ls_callContentDetail.add(callContentDetail);
        }
        for (String pId : infoMap.keySet()) {
            Map<String, InfoReflex> iMap = infoMap.get(pId);
            for (String name : iMap.keySet()) {
                ls_infoReflex.add(iMap.get(name));
            }
        }
        for (RecordInfo recordInfo : ls_recordInfo) {
            String uuid = recordInfo.getUuid();
            String callState = getCallState(ls_callContent, uuid);
            recordInfo.setCallState(callState);//统计差错类型
        }

        map.put("throughDetail", ls_throughDetail);
        map.put("recordInfoList", ls_recordInfo);
        map.put("callContentList", ls_callContent);
        map.put("customerInfoList", ls_customerInfo);
        map.put("resInfoList", ls_resInfo);
        map.put("infoReflex", ls_infoReflex);
        map.put("callContentDetailList", ls_callContentDetail);

        return map;
    }

    private static JSONArray processContent(List<CallContent> contents, String uuid) {
        List<CallContent> list = new ArrayList<>();
        Map<Integer, CallContent> map = new TreeMap<>();
        List<String> typeList = Arrays.asList("tts", "speechSuccess", "matchSucess", "speechOverTime", "matchError", "error", "hangup", "end", "dtmfSuccess", "dtmfError", "dtmfOverTime");
        if (contents != null && contents.size() > 0) {
            for (CallContent callContent : contents) {
                if (typeList.contains(callContent.getType())) {
                    map.put(callContent.getSeq(), callContent);
                }
            }
        }
        for (int seq : map.keySet()) {
            list.add(map.get(seq));
        }
        List<CallContentMatchResult> res = CallContentService.transState(CallContentService.processState(list, uuid));
        return list2JSONArray(res);
    }

    private static JSONArray list2JSONArray(List list) {
        JSONArray array = new JSONArray();
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSON(obj).toString());
                array.add(jsonObject);
            }
        }
        return array;
    }

    /**
     * 解析ResInfo数据结构
     * ResInfo属性不固定，采用key-value形式保存
     */
    private static List<ResInfo> parseResInfo(String uuid, JsonNode jsonNode) {
        List<ResInfo> ls_resInfo = new ArrayList<>();
        try {
            JsonNode resInfo = jsonNode.path("resInfo");
            log.info("resInfo=" + resInfo);
            Iterator<String> resInfo_fieldNames = resInfo.fieldNames();
            while (resInfo_fieldNames.hasNext()) {
                String fieldName = resInfo_fieldNames.next();
                String value = resInfo.get(fieldName).asText();
                log.info("fieldName=" + fieldName + ";value=" + value);
                ls_resInfo.add(new ResInfo(uuid, fieldName, value));
            }
        } catch (Exception e) {
            log.error("解析json转换ResInfo错误：" + e.getMessage());
        }
        return ls_resInfo;
    }

    /**
     * 解析CustomerInfo数据结构
     * CustomerInfo属性不固定，采用key-value形式保存
     */
    private static List<CustomerInfo> parseCustomerInfo(String uuid, JsonNode jsonNode) {
        List<CustomerInfo> ls_customerInfo = new ArrayList<>();
        try {
            JsonNode customerInfo = jsonNode.path("customerInfo");
            log.info("customerInfo=" + customerInfo);
            Iterator<String> fieldNames = customerInfo.fieldNames();

            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                String value = customerInfo.get(fieldName).asText();
                log.info("fieldName=" + fieldName + ";value=" + value);
                ls_customerInfo.add(new CustomerInfo(uuid, fieldName, value));
            }
        } catch (Exception e) {
            log.error("解析json转换CustomerInfo错误：" + e.getMessage());
        }
        return ls_customerInfo;
    }

    private static String parseUuid(ObjectMapper mapper, String line) throws IOException {
        return mapper.readTree(line).path("recordInfo").path("uuid").asText();
    }

    private static String parseCallResult(ObjectMapper mapper, String line) throws IOException {
        JsonNode resInfo = mapper.readTree(line).path("resInfo");
        if (resInfo != null) {
            return resInfo.path("callResult").asText();
        }
        return null;
    }

    private static String parseTradeState(ObjectMapper mapper, String line) throws IOException {
        JsonNode resInfo = mapper.readTree(line).path("resInfo");
        if (resInfo != null) {
            return resInfo.path("tradeState").asText();
        }
        return null;
    }

    private static String parseBlackList(ObjectMapper mapper, String line) throws IOException {
        JsonNode resInfo = mapper.readTree(line).path("resInfo");
        if (resInfo != null) {
            return resInfo.path("黑名单").asText();
        }
        return null;
    }

    private static String getCallState(List<CallContent> callContents, String uuid) {
        String state = "connectSuccess";
        if (callContents != null && callContents.size() > 0) {
            TreeMap<Integer, CallContent> treeMap = new TreeMap<>();
            for (CallContent callContent : callContents) {
                if (callContent.getUuid().equals(uuid)) {
                    treeMap.put(callContent.getSeq(), callContent);
                }
            }
            List<CallContent> callContentList = new ArrayList<>(treeMap.values());
            List<CallContentMatchResult> callContentMatchResults = CallContentService.processState(callContentList, uuid);
            for (CallContentMatchResult result : callContentMatchResults) {
                if (org.apache.commons.lang3.StringUtils.isBlank(result.getMatchNode())) {
                    state = "connectSuccess";
                } else if (result.getMatchNode().equals("matchError")) {
                    state = "matchError";
                    break;
                } else {
                    if (result.getMatchNode().equals("speechOverTime")) {
                        state = "speechOverTime";
                        break;
                    } else if (result.getMatchNode().equals("error")) {
                        state = "error";
                        break;
                    }
                }
            }
        }
        return state;
    }

}
