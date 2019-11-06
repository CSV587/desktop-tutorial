package com.hy.cpic.reporting.callnumconfig.returnVisit.service;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.cpic.base.api.ICMApi;
import com.hy.cpic.base.api.IPMApi;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.entities.BranchCallNumConfig;
import com.hy.cpic.entities.CallNumConfig;
import com.hy.cpic.entities.ReturnVisitMetaInfo;
import com.hy.cpic.mapper.oracle.CallNumConfigMapper;
import com.hy.cpic.mapper.oracle.ReturnVisitDataMapper;
import com.hy.cpic.mapper.oracle.ReturnVisitMapper;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.BranchCallNumConfigPage;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.CallNumConfigPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author wkl
 * @since 2019/3/5.
 */
@Slf4j
@Service
public class ReturnVisitService {

    public static final String NAME = "回访";
    @Value("${system.icm}")
    private String icmUrl;

    private final CallNumConfigMapper callNumConfigMapper;
    private final SortMetaInfoService sortMetaInfoService;
    private final ReturnVisitMapper returnVisitMapper;
    private final ReturnVisitDataMapper returnVisitDataMapper;
    private final ICMApi icmApi;
    private final IPMApi ipmApi;

    public ReturnVisitService(CallNumConfigMapper callNumConfigMapper, SortMetaInfoService sortMetaInfoService,
                              ReturnVisitMapper returnVisitMapper, ReturnVisitDataMapper returnVisitDataMapper,
                              ICMApi icmApi, IPMApi ipmApi) {
        this.callNumConfigMapper = callNumConfigMapper;
        this.sortMetaInfoService = sortMetaInfoService;
        this.returnVisitMapper = returnVisitMapper;
        this.returnVisitDataMapper = returnVisitDataMapper;
        this.icmApi = icmApi;
        this.ipmApi = ipmApi;
    }

    public PageResult editCallNumConfig(CallNumConfigPage callNumConfigPage) {
        CallNumConfig callNumConfig = new CallNumConfig();
        callNumConfig.setId(callNumConfigPage.getId());
        callNumConfig.setName(callNumConfigPage.getName().replace(" ", ""));
        callNumConfig.setPath(callNumConfigPage.getPath().replace(" ", ""));
        callNumConfig.setRegexp(callNumConfigPage.getRegexp());
        callNumConfig.setRuleId(callNumConfigPage.getRuleId());
        callNumConfig.setProjectId(callNumConfigPage.getProjectId());
        callNumConfig.setExecIp(callNumConfigPage.getExecIp().replace(" ", ""));
        callNumConfig.setCallMaxNum(callNumConfigPage.getCallMaxNum());
        callNumConfig.setRepeatNum(callNumConfigPage.getRepeatNum());
        callNumConfig.setBusinessType(callNumConfigPage.getBusinessType());
        callNumConfig.setLmt(new Date());
        callNumConfig.setLoid(callNumConfigPage.getLoid());
        callNumConfigMapper.edit(callNumConfig);
        return PageResult.success("业务编辑成功！");
    }

    public PageResult deleteCallNumConfig(String id) {
        BranchCallNumConfig branchCallNumConfig = new BranchCallNumConfig();
        branchCallNumConfig.setCallNumConfigId(id);
        if (callNumConfigMapper.queryBranchCallNumConfigById(branchCallNumConfig).isEmpty()) {
            CallNumConfig callNumConfig = new CallNumConfig();
            callNumConfig.setId(id);
            callNumConfig.setValidState(CallNumConfig.ValidState.unValid);
            callNumConfigMapper.deleteConfig(callNumConfig);
            return PageResult.success("业务删除成功");
        } else {
            return PageResult.fail("存在分公司数据，无法删除！");
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public Page queryCallNumConfig(CallNumConfigPage callNumConfigPage, String token) {
        PageHelper.startPage(callNumConfigPage.getCurrent(), callNumConfigPage.getPageSize());
        callNumConfigPage.setName(NAME);
        List<CallNumConfigPage> callNumConfigPages = callNumConfigMapper.queryCallNumConfigByName(callNumConfigPage);
        if (callNumConfigPages.isEmpty()) {
            CallNumConfig callNumConfig = new CallNumConfig();
            callNumConfig.setName(NAME);
            callNumConfig.setValidState(CallNumConfig.ValidState.valid);
            callNumConfig.setRepeatNum(2);
            callNumConfig.setTotalNum((long) 0);
            callNumConfigMapper.save(callNumConfig);
            List<CallNumConfigPage> callNumConfigPageList =
                callNumConfigMapper.queryCallNumConfigByName(callNumConfigPage);
            return (Page) callNumConfigPageList;
        } else {
            JSONArray ruleList = icmApi.ruleList(callNumConfigPages.get(0).getProjectId());
            callNumConfigPages.forEach(data -> {
                for (int i = 0; i < ruleList.size(); i++) {
                    if (ruleList.getJSONObject(i).getString("ruleId").equals(data.getRuleId())) {
                        String ruleName = ruleList.getJSONObject(i).getString("ruleName");
                        data.setRuleName(ruleName);
                    }
                }
                String foidName = ipmApi.getUserName(token, data.getFoid());
                data.setFoidName(foidName);
                String loidName = ipmApi.getUserName(token, data.getLoid());
                data.setLoidName(loidName);
                String projectName = ipmApi.getProjectName(token, data.getProjectId());
                data.setProjectName(projectName);
                int totalNum = returnVisitMapper.queryTotalNum(data.getId());
                data.setTotalNum((long) totalNum);
            });
        }
        return (Page) callNumConfigPages;
    }

    public Page queryBranchCallNumConfig(BranchCallNumConfigPage branchCallNumConfigPage, String token) {
        PageHelper.startPage(branchCallNumConfigPage.getCurrent(), branchCallNumConfigPage.getPageSize());
        List<BranchCallNumConfigPage> branchCallNumConfigPages =
            returnVisitMapper.queryBranchInfoByCallNumConfigId(branchCallNumConfigPage.getCallNumConfigId());
        branchCallNumConfigPages.forEach(data -> {
            String uuid = UUID.randomUUID().toString();
            data.setBranchId(uuid);
            data.setCallNumConfigName(NAME);
        });
        return (Page) branchCallNumConfigPages;
    }

    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "${import.oracle.cron5:0 */5 * * * ?}")
    public void scanFileScheduled() throws IOException {
        CallNumConfigPage page = new CallNumConfigPage();
        page.setValidState(CallNumConfig.ValidState.valid.toString());
        CallNumConfigPage callNumConfigPage = callNumConfigMapper.queryValidCallNumConfig(NAME);
        if (null == callNumConfigPage ||
            StringUtils.isAnyEmpty(callNumConfigPage.getPath(), callNumConfigPage.getRegexp(),
                callNumConfigPage.getRuleId(), callNumConfigPage.getProjectId(), callNumConfigPage.getExecIp())) {
            log.error("Please init " + NAME + " config first.");
            return;
        }

        boolean isExecMachine = false;
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = interfaces.nextElement();
            if (iface.isLoopback() || !iface.isUp()) continue;

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

        String path = callNumConfigPage.getPath();
        path = path.endsWith("/") ? path : path + "/";

        File root = new File(path);
        if (!root.isDirectory()) {
            throw new RuntimeException(root + " is no directory.");
        }
        final Pattern p = Pattern.compile(callNumConfigPage.getRegexp());
        File[] listFiles = root.listFiles(file -> (p.matcher(file.getName()).matches() && file.isFile()));

        if (listFiles == null || listFiles.length == 0) {
            log.info(root + "is Empty");
        }
        for (File file : listFiles != null ? listFiles : new File[0]) {
            dealFile(file, root, callNumConfigPage);
        }
    }


    private void dealFile(File file, File root, CallNumConfigPage callNumConfigPage) throws IOException {
        File okFile = new File(file.getPath().substring(0, file.getPath().lastIndexOf('.')) + ".OK");
        if (okFile.exists()) {
            if (new File(root.getPath() + "/.failure/" + okFile.getName()).exists() ||
                new File(root.getPath() + "/.failure/" + file.getName()).exists() ||
                new File(root.getPath() + "/.success/" + file.getName()).exists() ||
                new File(root.getPath() + "/.success/" + okFile.getName()).exists()) {
                log.warn("file {} repeated emergence, skip", file.getAbsolutePath());
                return;
            }

            saveData(okFile, file, root, callNumConfigPage);
        }
    }

    private void saveData(File okFile, File file, File root, CallNumConfigPage callNumConfigPage) throws IOException {
        try (RandomAccessFile in = new RandomAccessFile(file, "rw");
             FileLock ignored = in.getChannel().tryLock()) {
            if (ignored == null) {
                log.info("{} han been locked by other process.", file.getPath());
                return;
            }
            log.info("{} lock success.", file.getPath());
            boolean first = true;
            int branchIndex = -1, centerIndex = -1, businessTypeIndex = -1, callNumberIndex = -1,
                agentIdIndex = -1, carNumberIndex = -1, isWhiteListIndex = -1, listIdIndex = -1;
            while (true) {
                String line = in.readLine();
                if (line == null || !StringUtils.isNotEmpty(line.trim())) {
                    break;
                }
                String str = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                String[] split = str.split("\\|");
                if (first) {
                    first = false;
                    for (int i = 0; i < split.length; i++) {
                        String header = split[i];
                        if ("分公司".equals(header)) {
                            branchIndex = i;
                        } else if ("职场".equals(header)) {
                            centerIndex = i;
                        } else if ("业务类型".equals(header)) {
                            businessTypeIndex = i;
                        } else if ("客户手机号".equals(header)) {
                            callNumberIndex = i;
                        } else if ("车牌".equals(header)) {
                            carNumberIndex = i;
                        } else if ("坐席工号".equals(header)) {
                            agentIdIndex = i;
                        } else if ("名单ID".equals(header)) {
                            listIdIndex = i;
                        } else if ("是否禁拨号码".equals(header)) {
                            isWhiteListIndex = i;
                        }
                    }
                    if (branchIndex < 0) {
                        throw new RuntimeException("文件无分公司字段");
                    }
                    if (centerIndex < 0) {
                        throw new RuntimeException("文件无职场字段");
                    }
                    if (agentIdIndex < 0) {
                        throw new RuntimeException("文件无坐席工号字段");
                    }
                    if (listIdIndex < 0) {
                        throw new RuntimeException("文件无名单ID字段");
                    }
                    continue;
                }
                String branchName = split[branchIndex];
                String center = split[centerIndex];
                String businessType = businessTypeIndex < 0 ? "" : split[businessTypeIndex];
                String callNumber = callNumberIndex < 0 ? "" : split[callNumberIndex];
                String carNumber = carNumberIndex < 0 ? "" : split[carNumberIndex];
                String agentId = split[agentIdIndex];
                String isWhiteList = isWhiteListIndex < 0 ? "0" : split[isWhiteListIndex];
                String listId = split[listIdIndex];

                ReturnVisitMetaInfo metaInfo = new ReturnVisitMetaInfo();
                metaInfo.setBranchName(branchName);
                metaInfo.setCenter(center);
                metaInfo.setMetaInfo(str);
                metaInfo.setBusinessType(businessType);
                metaInfo.setCallNumber(callNumber);
                metaInfo.setCarNumber(carNumber);
                metaInfo.setListId(listId);
                metaInfo.setAgentId(agentId);
                metaInfo.setIsWhiteList(isWhiteList);
                if ("0".equals(isWhiteList)) {
                    metaInfo.setIsRepeat(
                        returnVisitDataMapper.isCalledInOneMonth(callNumber) > 0 ? "y" : "n");
                }
                metaInfo.setFst(new Date());
                metaInfo.setCallNumConfigId(callNumConfigPage.getId());
                returnVisitMapper.save(metaInfo);
            }

        } catch (Exception e) {
            log.error("{}", e);
            FileUtils.moveToDirectory(okFile, new File(root.getPath() + "/.failure"), true);
            FileUtils.moveToDirectory(file, new File(root.getPath() + "/.failure"), true);
            return;
        }

        FileUtils.moveToDirectory(okFile, new File(root.getPath() + "/.success"), true);
        FileUtils.moveToDirectory(file, new File(root.getPath() + "/.success"), true);
    }

    @Scheduled(cron = "${import.oracle.cron6:0 0 7 * * ?}")
    public void sortMetaInfoScheduled() throws IOException {
        sortMetaInfoService.sortMetaInfo("spring");
    }

    public void sortMetaInfoManual() throws IOException {
        sortMetaInfoService.sortMetaInfo("manual");
    }
}
