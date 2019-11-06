package com.hy.cpic.reporting.intentionlist.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.cpic.base.utils.DateUtils;
import com.hy.cpic.base.utils.PageUtils;
import com.hy.cpic.mapper.oracle.CallInfoStatisticsMapper;
import com.hy.cpic.reporting.intentionlist.page.IntentionListPage;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * .
 * Created by of liaoxg
 * date: 2018/9/8
 * user: lxg
 * package_name: com.hy.cpic.reporting.callbackrate.service
 */
@Service
public class IntentionListService {
    private final CallInfoStatisticsMapper callInfoStatisticsMapper;

    @Autowired
    public IntentionListService(CallInfoStatisticsMapper callInfoStatisticsMapper) {
        this.callInfoStatisticsMapper = callInfoStatisticsMapper;
    }

    public Page query(IntentionListPage intentionListPage) {
        PageHelper.startPage(intentionListPage.getCurrent(), intentionListPage.getPageSize()); //分页利器
        intentionListPage.setStartTime(DateUtils.startOfDay(intentionListPage.getStartTime()));
        intentionListPage.setEndTime(DateUtils.endOfDay(intentionListPage.getEndTime()));
        List<IntentionListPage> intentionListPages = callInfoStatisticsMapper.queryIntentionList(intentionListPage);
        PageUtils.randomId(intentionListPages);
        return (Page) intentionListPages;
    }

    public List<IntentionListPage> queryAll(IntentionListPage intentionListPage) {
        intentionListPage.setStartTime(DateUtils.startOfDay(intentionListPage.getStartTime()));
        intentionListPage.setEndTime(DateUtils.endOfDay(intentionListPage.getEndTime()));
        return callInfoStatisticsMapper.queryIntentionList(intentionListPage);
    }

    public void createListTxt(String onePath, String twoPath) {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        IntentionListPage intentionListPage = new IntentionListPage();
        Calendar startTime = Calendar.getInstance();
        startTime.add(Calendar.MINUTE, -40);// 3分钟之前的时间
        Date startD = startTime.getTime();
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.MINUTE, -10);// 3分钟之前的时间
        Date endD = endTime.getTime();
        intentionListPage.setStartTime(startD);
        intentionListPage.setEndTime(endD);
        List<Map<String, Object>> queryIntentionListMap = callInfoStatisticsMapper.queryIntentionListMap(intentionListPage);
        int index = 1;
        List<String> titleNames = new ArrayList<>();
        StringBuilder allText = new StringBuilder();
        for (Map<String, Object> item : queryIntentionListMap) {
            if (item.get("意向等级") != null) {
                switch (item.get("意向等级").toString()) {
                    case "高级":
                        item.put("意向等级", "1");
                        break;
                    case "中级":
                        item.put("意向等级", "2");
                        break;
                    case "低级":
                        item.put("意向等级", "3");
                        break;
                    case "黑名单":
                        item.put("意向等级", "4");
                        break;
                    case "接通":
                        item.put("意向等级", "5");
                        break;
                    case "未接通":
                        item.put("意向等级", "6");
                        break;
                    default:
                        item.put("意向等级", "");
                }
            }
            if (item.get("是否全流程") != null) {
                switch (item.get("是否全流程").toString()) {
                    case "是":
                        item.put("是否全流程", "1");
                        break;
                    case "否":
                        item.put("是否全流程", "2");
                        break;
                    default:
                        item.put("是否全流程", "");
                }
            }

            if (item.get("呼叫结果") != null) {
                item.put("呼叫结果", getResultCode(item.get("呼叫结果").toString()));
            }
        }
        writeResultTxt(queryIntentionListMap, index, titleNames, allText);

        String filePath = onePath + File.separator + "DX_AUTO_RESULT_YX_" + sdf.format(dt) + ".txt";
        writeResultFile(twoPath, allText, filePath);
    }

    public static void writeResultFile(String twoPath, StringBuilder allText, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                if (file.createNewFile()) {
                    try (FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                         OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
                         BufferedWriter writer = new BufferedWriter(outputStreamWriter);) {
                        writer.write(allText.toString());
                    }
                    FileUtils.copyFileToDirectory(file, new File(twoPath), true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeResultTxt(List<Map<String, Object>> queryIntentionListMap, int index, List<String> titleNames, StringBuilder allText) {
        for (Map<String, Object> item : queryIntentionListMap) {
            if (index == 1) {
                StringBuilder titleName = new StringBuilder();
                for (String key : item.keySet()) {
                    if (titleName.length() > 0) {
                        titleName.append("|");
                    }
                    titleNames.add(key);
                    titleName.append(key);
                }
                allText.append(titleName.toString()).append("\n");
            }
            int count = 0;
            StringBuilder cell = new StringBuilder();
            for (String name : titleNames) {
                if (count > 0) {
                    cell.append("|");
                }
                cell.append(item.get(name) == null ? "" : item.get(name).toString());
                count++;
            }
            allText.append(cell.toString()).append("\n");
            index++;
        }
    }

    public static String getResultCode(String result) {
        String callResultCode = "";
        switch (result.trim()) {
            case "有意向":
                callResultCode = "1";
                break;
            case "价格贵":
                callResultCode = "2";
                break;
            case "咨询什么时候到期":
                callResultCode = "3";
                break;
            case "非本人联系":
                callResultCode = "4";
                break;
            case "联系过我了":
                callResultCode = "5";
                break;
            case "朋友说理赔不好":
                callResultCode = "6";
                break;
            case "自己曾经理赔不满意":
                callResultCode = "7";
                break;
            case "自助办理":
                callResultCode = "8";
                break;
            case "保在4s店":
                callResultCode = "9";
                break;
            case "发短信":
                callResultCode = "10";
                break;
            case "没时间":
                callResultCode = "11";
                break;
            case "再说/未到期":
                callResultCode = "12";
                break;
            case "咨询去年险种":
                callResultCode = "13";
                break;
            case "在上年公司保":
                callResultCode = "14";
                break;
            case "朋友是做保险的":
                callResultCode = "15";
                break;
            case "咨询方案（哪个车）":
                callResultCode = "16";
                break;
            case "快速理赔":
                callResultCode = "17";
                break;
            case "其他公司也有该优惠":
                callResultCode = "18";
                break;
            case "不考虑":
                callResultCode = "19";
                break;
            case "不需要":
                callResultCode = "20";
                break;
            case "非本人（打错）":
                callResultCode = "21";
                break;
            case "已经办理":
                callResultCode = "22";
                break;
            case "没有车":
                callResultCode = "23";
                break;
            case "投诉":
                callResultCode = "24";
                break;
            case "身份质疑":
                callResultCode = "25";
                break;
            case "质疑来电显示":
                callResultCode = "26";
                break;
            case "质疑信息泄漏":
                callResultCode = "27";
                break;
            case "骗子不赔保":
                callResultCode = "28";
                break;
            case "机器人":
                callResultCode = "29";
                break;
            case "可接通":
                callResultCode = "30";
                break;
            case "未接通":
                callResultCode = "31";
                break;
            case "咨询代步服务":
                callResultCode = "32";
                break;
            case "咨询代驾服务":
                callResultCode = "33";
                break;
            case "咨询服务":
                callResultCode = "34";
                break;
            case "转人工":
                callResultCode = "35";
                break;
            default:
        }
        return callResultCode;
    }
}
