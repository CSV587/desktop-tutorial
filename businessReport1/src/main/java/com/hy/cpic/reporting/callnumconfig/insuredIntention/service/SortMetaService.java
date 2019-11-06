package com.hy.cpic.reporting.callnumconfig.insuredIntention.service;

import com.alibaba.fastjson.JSONObject;
import com.hy.cpic.base.api.ICMApi;
import com.hy.cpic.base.api.IPMApi;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.entities.CallInfoStatistics;
import com.hy.cpic.entities.InsuredIntentionMetaInfo;
import com.hy.cpic.entities.InsuredIntentionOneMonth;
import com.hy.cpic.mapper.oracle.*;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.BranchCallNumConfigPage;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.CallNumConfigPage;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.InsuredIntentionMonthInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wkl
 * @since 2018/12/24.
 */
@Slf4j
@Service
public class SortMetaService {
    private final CallNumConfigMapper callNumConfigMapper;
    private final BranchCallNumConfigMapper branchCallNumConfigMapper;
    private final InsuredIntentionMetaInfoMapper insuredIntentionMetaInfoMapper;
    private final IPMApi ipmApi;
    private final ICMApi icmApi;
    private final InsuredIntentionOneMonthMapper insuredIntentionOneMonthMapper;
    private final CallInfoStatisticsMapper callInfoStatisticsMapper;

    private static final String NAME = "投保意向";
    @Value("${system.icm}")
    private String icmUrl;

    @Autowired
    public SortMetaService(CallNumConfigMapper callNumConfigMapper,
                           BranchCallNumConfigMapper branchCallNumConfigMapper, IPMApi ipmApi, ICMApi icmApi,
                           InsuredIntentionMetaInfoMapper insuredIntentionMetaInfoMapper, InsuredIntentionOneMonthMapper insuredIntentionOneMonthMapper, CallInfoStatisticsMapper callInfoStatisticsMapper) {
        this.callNumConfigMapper = callNumConfigMapper;
        this.branchCallNumConfigMapper = branchCallNumConfigMapper;
        this.ipmApi = ipmApi;
        this.icmApi = icmApi;
        this.insuredIntentionMetaInfoMapper = insuredIntentionMetaInfoMapper;
        this.insuredIntentionOneMonthMapper = insuredIntentionOneMonthMapper;
        this.callInfoStatisticsMapper = callInfoStatisticsMapper;
    }


    @Transactional(rollbackFor = Exception.class)
    public void sortMetaInfo(String execSource) throws IOException, InterruptedException {
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
        if (StringUtils.isAnyEmpty(callNumConfigPage.getPath(), callNumConfigPage.getRegexp(),
            callNumConfigPage.getRuleId(), callNumConfigPage.getProjectId(), callNumConfigPage.getExecIp())) {
            log.error("Please init " + NAME + " config first.");
            return;
        }

        boolean isExecMachine = false;
        if ("spring".equals(execSource)) {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    if (addr instanceof Inet6Address) continue;

                    log.info("{} sort meta info find IP : {}", CallNumConfigService.NAME, addr.getHostAddress());

                    if (addr.getHostAddress().equals(callNumConfigPage.getExecIp())) {
                        isExecMachine = true;
                    }
                }
            }
            if (!isExecMachine) {
                log.info("生成任务，本机IP非执行IP", callNumConfigPage.getExecIp());
                return;
            }

            if (callNumConfigPage.getTaskTime() != null &&
                DateUtils.isSameDay(new Date(), callNumConfigPage.getTaskTime())) {
                log.info("{}, 重复执行退出", NAME);
                return;
            }
        }

        List<BranchCallNumConfigPage> branchCallNumConfigPages =
            branchCallNumConfigMapper.queryByCallNumConfigId(callNumConfigPage.getId());

        if (CollectionUtils.isEmpty(branchCallNumConfigPages)) {
            return;
        }

        List<String> businessTypes = null;
        if (StringUtils.isNotEmpty(callNumConfigPage.getBusinessType())) {
            businessTypes = Arrays.asList(callNumConfigPage.getBusinessType().split(","));
        }

        File tmpDir = Files.createTempDirectory("tmp").toFile();
        int totalNum = 0;
        for (BranchCallNumConfigPage branchCallNumConfigPage : branchCallNumConfigPages) {
            List<InsuredIntentionMetaInfo> metaInfos = insuredIntentionMetaInfoMapper
                .sort(callNumConfigPage.getId(), branchCallNumConfigPage.getBranchName(),
                    branchCallNumConfigPage.getCallNum(), businessTypes);
            if (CollectionUtils.isEmpty(metaInfos)) {
                continue;
            }
            File branchSortFile =
                new File(tmpDir.getPath() + "/" + branchCallNumConfigPage.getBranchId() + ".record");

            if (!branchSortFile.createNewFile()) {
                throw new RuntimeException(branchSortFile.getAbsolutePath() + " create error");
            }

            totalNum += metaInfos.size();

            List<String> callNumberList = new ArrayList<>();
            for (InsuredIntentionMetaInfo metaInfo : metaInfos) {
                if (StringUtils.isNotEmpty(metaInfo.getCallNumber()))
                    callNumberList.add(metaInfo.getCallNumber());
                if (StringUtils.isNotEmpty(metaInfo.getCallNumber1()))
                    callNumberList.add(metaInfo.getCallNumber1());
                if (StringUtils.isNotEmpty(metaInfo.getCallNumber2()))
                    callNumberList.add(metaInfo.getCallNumber2());
            }

            Map<String, InsuredIntentionOneMonth> detailMonthMap = new HashMap<>();//号码：号码的拨打信息

            Map<String, Integer> map = new HashMap<>();//号码：拨打次数
            int length = callNumberList.size();
            log.info("call number list size is {}", callNumberList.size());
            for (int i = 0; i < length; i += 1000) {
                List<String> subList = callNumberList.subList(i, i + 1000 > length ? length : i + 1000);
                List<InsuredIntentionMonthInfo> calledTimesByCallNumber = insuredIntentionOneMonthMapper.calledTimesByMonth(subList);
                calledTimesByCallNumber.forEach(data -> {
                    String callNumber = data.getCallNumber();
                    Integer calledTimes = data.getCalledTimes();
                    map.put(callNumber, calledTimes);
                });

                List<InsuredIntentionOneMonth> detailMonthList = insuredIntentionOneMonthMapper.detailByCallNumber(subList);
                detailMonthList.forEach(data -> {
                    String callNumber = data.getCallNumber();
                    detailMonthMap.put(callNumber, data);
                });
            }
            log.info("{} record in 30 days", map.size());
            log.info("{} connect record in 30 days", detailMonthMap.size());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try (FileOutputStream fos = new FileOutputStream(branchSortFile);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
                for (InsuredIntentionMetaInfo metaInfo : metaInfos) {
                    Pair<String, String> callResult = getCallResult(metaInfo, detailMonthMap);
                    String metaInfoStr = metaInfo.getMetaInfo();
                    String[] metaList = metaInfoStr.split("\\|");
                    List<String> metaArr = Arrays.asList(metaList);
                    List arrList = new ArrayList(metaArr);
                    int metaLength = metaList.length;
                    if (metaLength >= 13 && metaLength <= 15) {
                        arrList.add(6, "");
                        arrList.add(7, "");
                        arrList.add(12, "");
                        arrList.add(13, "");
                        if (metaLength == 13) {
                            arrList.add(17, "");
                            arrList.add(18, "");
                        } else if (metaLength == 14) {
                            arrList.add(18, "");
                        }
                        metaInfoStr = StringUtils.join(arrList, "|");

                    }
                    if (callResult != null) {
                        CallInfoStatistics callInfoStatistics = new CallInfoStatistics();
                        callInfoStatistics.setProType("投保意向");
                        callInfoStatistics.setCallNumber(callResult.getValue0());
                        callInfoStatistics.setChannelStartTime(detailMonthMap.get(callResult.getValue0()).getFst());
                        callInfoStatistics.setChannelEndTime(detailMonthMap.get(callResult.getValue0()).getFst());
                        callInfoStatistics.setRecordStartTime(detailMonthMap.get(callResult.getValue0()).getFst());
                        callInfoStatistics.setRecordEndTime(detailMonthMap.get(callResult.getValue0()).getFst());
                        callInfoStatistics.setIsConnect(detailMonthMap.get(callResult.getValue0()).getIsConnect());
                        callInfoStatistics.setColumn1(metaInfo.getBranchName());
                        callInfoStatistics.setColumn2(metaInfo.getActivityName());
                        callInfoStatistics.setColumn3(metaList[4]);
                        callInfoStatistics.setColumn4(metaList[3]);
                        callInfoStatistics.setColumn5(detailMonthMap.get(callResult.getValue0()).getIntentionLevel());
                        callInfoStatistics.setColumn6("未接通".equals(detailMonthMap.get(callResult.getValue0()).getIntentionLevel()) ? "" : "是");
                        callInfoStatistics.setColumn7(callResult.getValue1());
                        callInfoStatistics.setColumn9(callResult.getValue1());
                        callInfoStatistics.setColumn10(metaList[0]);
                        callInfoStatistics.setColumn11(metaList[2]);
                        callInfoStatistics.setColumn16(metaInfo.getExpireDate() == null ? metaList[12] : sdf.format(metaInfo.getExpireDate()));
                        callInfoStatistics.setColumn17(metaInfo.getBusinessType());
                        callInfoStatistics.setColumn20("manual");
                        callInfoStatisticsMapper.save(callInfoStatistics);
                        log.info("insert info id is {}", callInfoStatistics.getId());
                        log.info("insert metaInfo success.");
                    } else {
                        String times = canCall(metaInfo, map);
                        osw.write(metaInfoStr);
                        osw.write(times == null ? " " : times);
                        osw.write('\n');
                    }
                    insuredIntentionMetaInfoMapper.copyToHistory(metaInfo.getId());
                    insuredIntentionMetaInfoMapper.deleteById(metaInfo.getId());
                }
            }
        }

        if (totalNum == 0) {
            log.info("No insured intention call meta info found.");
            return;
        } else {
            log.info("Generate call tasks, {}", totalNum);
        }

        callNumConfigMapper.increaseNum(callNumConfigPage.getId(), 0);

        File resultFile = new File(tmpDir.getPath() + "/result.txt");
        if (!resultFile.createNewFile()) {
            throw new RuntimeException(resultFile.getAbsolutePath() + " create error");
        }

        FileUtils
            .write(resultFile, "营销活动ID|营销活动名称|名单ID|车牌号|客户姓名|客户手机号1|客户手机号2|客户手机号3|客户性别|分公司|分公司代码|归属地1|归属地2|归属地3|终保日期|名单类型|名单三级业务类型|中支公司代码|中支公司|拨打次数1|拨打次数2|拨打次数3\n",
                true);

        String[] command1 = {"bash", "-c",
            String.format("paste -d \\\\n %s | sed '/^\\s*$/d' >> %s", tmpDir.getPath() + "/*.record",
                resultFile.getAbsolutePath())};

        ProcessBuilder proc = new ProcessBuilder(command1);
        proc.start().waitFor();

        HttpEntity entity = MultipartEntityBuilder.create()
            .addPart("callFile", new FileBody(resultFile))
            .addPart("taskName", new StringBody(callNumConfigPage.getName()
                + DateFormatUtils.format(new Date(), "_yyyyMMddHHmmss"), ContentType.APPLICATION_JSON))
            .addPart("ruleId", new StringBody(callNumConfigPage.getRuleId(), ContentType.APPLICATION_JSON))
            .build();

        HttpPost request = new HttpPost(icmUrl + "connector/add_task");
        request.setEntity(entity);

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(request);
        String respondContent = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);

        FileUtils.deleteDirectory(tmpDir);

        PageResult pageResult = JSONObject.parseObject(respondContent, PageResult.class);
        if (pageResult.getCode() < 0) {
            throw new RuntimeException(pageResult.getMessage());
        } else {
            log.info(pageResult.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "${import.oracle.cron8:0 0 22 * * ?}")
    public void batchClearNotInMonth() {
        int size = insuredIntentionOneMonthMapper.batchClearNotInMonth();
        log.info("clear monthTable record {}", size);
    }

    private Pair<String, String> getCallResult(InsuredIntentionMetaInfo metaInfo, Map<String, InsuredIntentionOneMonth> detailMonthMap) {
        List<InsuredIntentionOneMonth> list = new ArrayList<>();

        if (detailMonthMap.containsKey(metaInfo.getCallNumber()) && StringUtils.isNotEmpty(detailMonthMap.get(metaInfo.getCallNumber()).getCallResult())) {
            list.add(detailMonthMap.get(metaInfo.getCallNumber()));
        }
        if (detailMonthMap.containsKey(metaInfo.getCallNumber1()) && StringUtils.isNotEmpty(detailMonthMap.get(metaInfo.getCallNumber1()).getCallResult())) {
            list.add(detailMonthMap.get(metaInfo.getCallNumber1()));
        }
        if (detailMonthMap.containsKey(metaInfo.getCallNumber2()) && StringUtils.isNotEmpty(detailMonthMap.get(metaInfo.getCallNumber2()).getCallResult())) {
            list.add(detailMonthMap.get(metaInfo.getCallNumber2()));
        }

        Pair<String, String> pair = null;
        if (!list.isEmpty()) {
            list.sort((a, b) -> a.getFst().before(b.getFst()) ? -1 : 1);
            pair = new Pair<>(list.get(0).getCallNumber(), list.get(0).getCallResult());
        }

        return pair;
    }

    private String canCall(InsuredIntentionMetaInfo metaInfo, Map<String, Integer> map) {
        if ((!map.containsKey(metaInfo.getCallNumber()) || map.get(metaInfo.getCallNumber()) < 3)
            || (!map.containsKey(metaInfo.getCallNumber1()) || map.get(metaInfo.getCallNumber1()) < 3)
            || (!map.containsKey(metaInfo.getCallNumber2()) || map.get(metaInfo.getCallNumber2()) < 3)) {
            return "|" + (!map.containsKey(metaInfo.getCallNumber()) ? "0" : map.get(metaInfo.getCallNumber()).toString())
                + "|" + (!map.containsKey(metaInfo.getCallNumber1()) ? "0" : map.get(metaInfo.getCallNumber1()).toString())
                + "|" + (!map.containsKey(metaInfo.getCallNumber2()) ? "0" : map.get(metaInfo.getCallNumber2()).toString());
        }
        return null;
    }
}
