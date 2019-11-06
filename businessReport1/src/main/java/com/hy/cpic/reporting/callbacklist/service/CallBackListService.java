package com.hy.cpic.reporting.callbacklist.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.cpic.base.utils.DateUtils;
import com.hy.cpic.base.utils.PageUtils;
import com.hy.cpic.mapper.oracle.CallInfoStatisticsMapper;
import com.hy.cpic.reporting.callbacklist.page.CallBackListPage;
import com.hy.cpic.reporting.intentionlist.service.IntentionListService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class CallBackListService {
    private final CallInfoStatisticsMapper callInfoStatisticsMapper;

    @Autowired
    public CallBackListService(CallInfoStatisticsMapper callInfoStatisticsMapper) {
        this.callInfoStatisticsMapper = callInfoStatisticsMapper;
    }

    public Page query(CallBackListPage callBackListPage) {
        PageHelper.startPage(callBackListPage.getCurrent(), callBackListPage.getPageSize()); //分页利器
        callBackListPage.setStartTime(DateUtils.startOfDay(callBackListPage.getStartTime()));
        callBackListPage.setEndTime(DateUtils.endOfDay(callBackListPage.getEndTime()));
        List<CallBackListPage> callBackListPages = callInfoStatisticsMapper.queryCallBackList(callBackListPage);
        tranCallResult(callBackListPages);
        PageUtils.randomId(callBackListPages);
        return (Page) callBackListPages;
    }

    public List<CallBackListPage> queryAll(CallBackListPage callBackListPage) {
        callBackListPage.setStartTime(DateUtils.startOfDay(callBackListPage.getStartTime()));
        callBackListPage.setEndTime(DateUtils.endOfDay(callBackListPage.getEndTime()));
        List<CallBackListPage> callBackListPages = callInfoStatisticsMapper.queryCallBackList(callBackListPage);
        tranCallResult(callBackListPages);
        return callBackListPages;
    }

    private void tranCallResult(List<CallBackListPage> callBackListPages) {
        for (CallBackListPage page : callBackListPages) {
            if ("1".equals(page.getCallResult1())) {
                page.setCallResult1("失败");
            } else {
                page.setCallResult1("成功");
            }

            if (StringUtils.isNotBlank(page.getCallResult2())) {
                switch (page.getCallResult2()) {
                    case "0":
                        page.setCallResult2("成功");
                        break;
                    case "1":
                        page.setCallResult2("转人工");
                        break;
                    case "2":
                        page.setCallResult2("失败");
                        break;
                    case "3":
                        page.setCallResult2("拒访");
                        break;
                    case "4":
                        page.setCallResult2("黑名单");
                        break;
                    default:
                        break;
                }
            }
            if (StringUtils.isNotBlank(page.getBusinessType())) {
                switch (page.getBusinessType()) {
                    case "11":
                        page.setBusinessType("续保");
                        break;
                    case "12":
                        page.setBusinessType("转介绍");
                        break;
                    case "13":
                        page.setBusinessType("网销");
                        break;
                    case "14":
                        page.setBusinessType("呼出");
                        break;
                    case "15":
                        page.setBusinessType("推介");
                        break;
                    case "16":
                        page.setBusinessType("呼入");
                        break;
                    case "17":
                        page.setBusinessType("测试");
                        break;
                    case "21":
                        page.setBusinessType("节日回访");
                        break;
                    case "22":
                        page.setBusinessType("成功件回访");
                        break;
                    case "23":
                        page.setBusinessType("退保");
                        break;
                    case "24":
                        page.setBusinessType("批改回访");
                        break;
                    case "25":
                        page.setBusinessType("延保产品");
                        break;
                    case "31":
                        page.setBusinessType("延保产品");
                        break;
                    default:
                        page.setBusinessType("");
                }
            }
            if (StringUtils.isNotBlank(page.getCallBackType())) {
                switch (page.getCallBackType()) {
                    case "01":
                        page.setCallBackType("成功件回访");
                        break;
                    default:
                        page.setCallBackType("成功件回访");
                }
            }
        }
    }

    public void createListTxt(String onePath, String twoPath) {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        CallBackListPage callBackListPage = new CallBackListPage();
        callBackListPage.setStartTime(DateUtils.startOfDay(dt));
        callBackListPage.setEndTime(DateUtils.endOfDay(dt));
        List<Map<String, Object>> queryCallBackListMap = callInfoStatisticsMapper.queryCallBackListMap(callBackListPage);
        int index = 1;
        List<String> titleNames = new ArrayList<>();
        StringBuilder allText = new StringBuilder();
        IntentionListService.writeResultTxt(queryCallBackListMap, index, titleNames, allText);
        if (allText.length() > 0) {
            String filePath = onePath + File.separator + "DX_AUTO_RESULT_HF_" + sdf.format(dt) + ".txt";
            IntentionListService.writeResultFile(twoPath, allText, filePath);
        }
    }
}
