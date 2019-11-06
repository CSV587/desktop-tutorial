package com.hy.cpic.reporting.extractData.returnVisit.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.cpic.base.utils.DateUtils;
import com.hy.cpic.base.utils.PageUtils;
import com.hy.cpic.entities.ReturnVisitData;
import com.hy.cpic.mapper.oracle.CallNumConfigMapper;
import com.hy.cpic.mapper.oracle.ReturnVisitDataMapper;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.CallNumConfigPage;
import com.hy.cpic.reporting.codetype.service.CodeTypeService;
import com.hy.cpic.reporting.extractData.returnVisit.page.ReturnVisitDataPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@Service
public class ReturnVisitDataService {

    private static final String NAME = "回访";

    private static final String COMPANY_CODE = "{\"宁波分公司\":\"3040100\",\"西藏分公司\":\"7060100\",\"福建分公司\":\"3070100\",\"湖南分公司\":\"4030100\",\"浙江分公司\":\"3030100\",\"河北分公司\":\"1030100\",\"宁夏分公司\":\"7040100\",\"内蒙古分公司\":\"1050100\",\"常州分公司\":\"3020400\",\"山西分公司\":\"1040100\",\"山东分公司\":\"3090100\",\"无锡分公司\":\"3020300\",\"安徽分公司\":\"3050100\",\"东莞分公司\":\"5010800\",\"深圳分公司\":\"5020100\",\"重庆分公司\":\"6020100\",\"北京分公司\":\"1010100\",\"温州分公司\":\"3030400\",\"江苏分公司\":\"3020100\",\"新疆分公司\":\"7030100\",\"青岛分公司\":\"3100100\",\"黑龙江分公司\":\"2040100\",\"湖北分公司\":\"4020100\",\"云南分公司\":\"6040100\",\"广东分公司\":\"5010100\",\"河南分公司\":\"4010100\",\"四川分公司\":\"6010100\",\"青海分公司\":\"7050100\",\"大连分公司\":\"2020100\",\"吉林分公司\":\"2030100\",\"贵州分公司\":\"6030100\",\"广西分公司\":\"5040100\",\"辽宁分公司\":\"2010100\",\"江西分公司\":\"3060100\",\"上海分公司\":\"3010100\",\"厦门分公司\":\"3080100\",\"陕西分公司\":\"7010100\",\"海南分公司\":\"5030100\",\"苏州分公司\":\"3110100\",\"甘肃分公司\":\"7020100\",\"天津分公司\":\"1020100\"}\n";


    private final ReturnVisitDataMapper returnVisitDataMapper;
    private final CallNumConfigMapper callNumConfigMapper;
    private final CodeTypeService codeTypeService;

    public ReturnVisitDataService(ReturnVisitDataMapper returnVisitDataMapper, CallNumConfigMapper callNumConfigMapper, CodeTypeService codeTypeService) {
        this.returnVisitDataMapper = returnVisitDataMapper;
        this.callNumConfigMapper = callNumConfigMapper;
        this.codeTypeService = codeTypeService;
    }

    public Page query(ReturnVisitDataPage returnVisitDataPage) {
        PageHelper.startPage(returnVisitDataPage.getCurrent(), returnVisitDataPage.getPageSize());
        returnVisitDataPage.setStartTime(DateUtils.startOfDay(returnVisitDataPage.getStartTime()));
        returnVisitDataPage.setEndTime(DateUtils.endOfDay(returnVisitDataPage.getEndTime()));
        if (!StringUtils.isEmpty(returnVisitDataPage.getCenter()))
            returnVisitDataPage.setCenter(returnVisitDataPage.getCenter().trim());
        if (!StringUtils.isEmpty(returnVisitDataPage.getCompany()))
            returnVisitDataPage.setCompany(returnVisitDataPage.getCompany().trim());
        List<ReturnVisitDataPage> returnVisitDataPageList = returnVisitDataMapper.query(returnVisitDataPage);
        if (!CollectionUtils.isEmpty(returnVisitDataPageList)) {
            returnVisitDataPageList.forEach(data -> {
                data.setCoverRate(formatInteger(data.getTaskNum(), data.getReturnNum()));
                data.setConnectedRate(formatInteger(data.getReturnNum(), data.getConnectedNum()));
                data.setAnswerOneRate(formatInteger(data.getConnectedNum(), data.getAnswerOneNum()));
                data.setAnswerTwoRate(formatInteger(data.getConnectedNum(), data.getAnswerTwoNum()));
                data.setAnswerThreeRate(formatInteger(data.getConnectedNum(), data.getAnswerThreeNum()));
                data.setAnswerFourRate(formatInteger(data.getConnectedNum(), data.getAnswerFourNum()));
                data.setAnswerFiveRate(formatInteger(data.getConnectedNum(), data.getAnswerFiveNum()));
                data.setOverRate(formatInteger(data.getConnectedNum(), data.getOverNum()));
                data.setVirtualNumberRate(formatInteger(data.getConnectedNum(), data.getVirtualNumberNum()));
                data.setOnlineRate(formatInteger(data.getConnectedNum(), data.getOnlineNumber()));
            });
        }
        PageUtils.randomId(returnVisitDataPageList);
        return (Page) returnVisitDataPageList;
    }

    private String formatInteger(String total, String single) {
        double rate = "0".equals(total) ? (double) 0 : (double) Integer.valueOf(single) / Integer.valueOf(total) * 100;
        BigDecimal big = new BigDecimal(rate);
        return big.setScale(0, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() + "%";
    }

    public List<ReturnVisitDataPage> queryAll(ReturnVisitDataPage returnVisitDataPage) {
        returnVisitDataPage.setStartTime(DateUtils.startOfDay(returnVisitDataPage.getStartTime()));
        returnVisitDataPage.setEndTime(DateUtils.endOfDay(returnVisitDataPage.getEndTime()));
        if (!StringUtils.isEmpty(returnVisitDataPage.getCenter()))
            returnVisitDataPage.setCenter(returnVisitDataPage.getCenter().trim());
        if (!StringUtils.isEmpty(returnVisitDataPage.getCompany()))
            returnVisitDataPage.setCompany(returnVisitDataPage.getCompany().trim());
        List<ReturnVisitDataPage> returnVisitDataPageList = returnVisitDataMapper.query(returnVisitDataPage);
        if (!CollectionUtils.isEmpty(returnVisitDataPageList)) {
            returnVisitDataPageList.forEach(data -> {
                data.setCoverRate(formatInteger(data.getTaskNum(), data.getReturnNum()));
                data.setConnectedRate(formatInteger(data.getReturnNum(), data.getConnectedNum()));
                data.setAnswerOneRate(formatInteger(data.getConnectedNum(), data.getAnswerOneNum()));
                data.setAnswerTwoRate(formatInteger(data.getConnectedNum(), data.getAnswerTwoNum()));
                data.setAnswerThreeRate(formatInteger(data.getConnectedNum(), data.getAnswerThreeNum()));
                data.setAnswerFourRate(formatInteger(data.getConnectedNum(), data.getAnswerFourNum()));
                data.setAnswerFiveRate(formatInteger(data.getConnectedNum(), data.getAnswerFiveNum()));
                data.setOverRate(formatInteger(data.getConnectedNum(), data.getOverNum()));
                data.setVirtualNumberRate(formatInteger(data.getConnectedNum(), data.getVirtualNumberNum()));
                data.setOnlineRate(formatInteger(data.getConnectedNum(), data.getOnlineNumber()));
            });
        }
        return returnVisitDataPageList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "${import.oracle.cron7:0 30 21 * * ?}")
    public void generateFileSchedule() throws IOException {
        CallNumConfigPage callNumConfigPage;
        try {
            callNumConfigPage = callNumConfigMapper.queryValidCallNumConfigLock(NAME);
        } catch (CannotAcquireLockException e) {
            log.info("{} lock failure", NAME);
            return;
        } catch (Exception e) {
            log.error("{}", e);
            return;
        }
        if (null == callNumConfigPage || StringUtils.isAnyEmpty(callNumConfigPage.getPath(), callNumConfigPage.getRegexp(),
            callNumConfigPage.getRuleId(), callNumConfigPage.getProjectId(), callNumConfigPage.getExecIp())) {
            log.error("Please init " + NAME + " config first.");
            return;
        }

        boolean isExecMachine = false;
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = interfaces.nextElement();
            if (iface.isLoopback() || !iface.isUp())
                continue;

            Enumeration<InetAddress> addresses = iface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();

                if (addr instanceof Inet6Address) continue;

                log.info("{} scan file find IP : {}", NAME, addr.getHostAddress());
                if (addr.getHostAddress().equals(callNumConfigPage.getExecIp())) {
                    isExecMachine = true;
                }
            }
        }
        if (!isExecMachine) {
            log.info("扫描文件，本机IP非执行IP", callNumConfigPage.getExecIp());
            return;
        }

        JSONObject jsonObject = JSONObject.parseObject(COMPANY_CODE);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        ReturnVisitDataPage returnVisitDataPage = new ReturnVisitDataPage();
        returnVisitDataPage.setStartTime(DateUtils.startOfDay(date));
        returnVisitDataPage.setEndTime(DateUtils.endOfDay(date));
        List<ReturnVisitData> unrealNumberList = returnVisitDataMapper.queryUnrealNumber(returnVisitDataPage);

        if (null == unrealNumberList || unrealNumberList.isEmpty()) {
            log.info("No unreal number info found.");
            return;
        } else {
            log.info("Generate unreal number info, {}", unrealNumberList.size());
        }

        String name = "MD_AUTO_RESULT_" + sdf.format(date);

        String unrealNumberPath = codeTypeService.queryContentByCodeAndType("unrealNumberPath", "savePath");
        if (StringUtils.isEmpty(unrealNumberPath)) {
            log.info("please init unrealNumberPath configure");
            return;
        }
        File resultFile = new File(unrealNumberPath + name + ".txt");
        if (!resultFile.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            log.info("unrealNumberPath {} is not present", unrealNumberPath);
            boolean result = resultFile.getParentFile().mkdirs();
            if (!result) {
                throw new RuntimeException("create dir" + unrealNumberPath + "error");
            }
        }

        boolean fileCreateResult = resultFile.createNewFile();
        if (!fileCreateResult) {
            log.info("unrealNumberFile is exist");
        }

        for (int i = 0; i < unrealNumberList.size(); i++) {
            try (FileOutputStream fos = new FileOutputStream(resultFile, i != 0);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
                String companyKey = unrealNumberList.get(i).getCompany();
                if (StringUtils.isEmpty(jsonObject.getString(companyKey))) {
                    log.info("{} is not exist in companyCode", companyKey);
                    return;
                }
                osw.write(jsonObject.getString(companyKey));
                osw.write("|&|");
                osw.write(unrealNumberList.get(i).getCarNumber());
                osw.write("|&|");
                osw.write(unrealNumberList.get(i).getCallNumber());
                osw.write("\r\n");
            } catch (Exception e) {
                log.error("write file error");
            }
        }
    }
}
