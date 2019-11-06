package com.hy.cpic.reporting.callnumconfig.returnVisit.service;

import com.alibaba.fastjson.JSONObject;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.entities.ReturnVisitData;
import com.hy.cpic.entities.ReturnVisitMetaInfo;
import com.hy.cpic.mapper.oracle.BranchCallNumConfigMapper;
import com.hy.cpic.mapper.oracle.CallInfoStatisticsMapper;
import com.hy.cpic.mapper.oracle.CallNumConfigMapper;
import com.hy.cpic.mapper.oracle.ReturnVisitDataMapper;
import com.hy.cpic.mapper.oracle.ReturnVisitMapper;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.CallNumConfigPage;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.service.CallNumConfigService;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.CannotAcquireLockException;
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
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * @author wkl
 * @since 2019/3/5.
 */
@Slf4j
@Service
public class SortMetaInfoService {

    private static final String NAME = "回访";
    @Value("${system.icm}")
    private String icmUrl;

    private final ReturnVisitMapper returnVisitMapper;
    private final CallNumConfigMapper callNumConfigMapper;
    private final ReturnVisitDataMapper returnVisitDataMapper;

    @Autowired
    public SortMetaInfoService(ReturnVisitMapper returnVisitMapper, CallNumConfigMapper callNumConfigMapper,
                               ReturnVisitDataMapper returnVisitDataMapper,
                               BranchCallNumConfigMapper branchCallNumConfigMapper) {
        this.returnVisitMapper = returnVisitMapper;
        this.callNumConfigMapper = callNumConfigMapper;
        this.returnVisitDataMapper = returnVisitDataMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public void sortMetaInfo(String execSource) throws IOException {
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

        File tmpDir = Files.createTempDirectory("tmp").toFile();
        int totalNum = 0;

        List<ReturnVisitMetaInfo> metaInfoList =
            returnVisitMapper.sortPolling(callNumConfigPage.getId(), callNumConfigPage.getCallMaxNum());

        File resultFile = new File(tmpDir.getPath() + "/result.txt");
        if (!resultFile.createNewFile()) {
            throw new RuntimeException(resultFile.getAbsolutePath() + " create error");
        }

        FileUtils
            .write(resultFile,
                "分公司|车牌|业务类型|生效日期|职场|团队|片区|坐席工号|坐席姓名|回访名单任务生成日期|回访类型|客户姓名|客户性别|客户手机号|名单ID|中支公司|归属地|是否禁拨号码|交强险起保日期|商业险起保日期\n",
                true);

        totalNum += metaInfoList.size();

        for (ReturnVisitMetaInfo metaInfo : metaInfoList) {
            try (FileOutputStream fos = new FileOutputStream(resultFile, true);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {

                ReturnVisitData returnVisitData = new ReturnVisitData();
                returnVisitData.setCenter(metaInfo.getCenter());
                returnVisitData.setCompany(metaInfo.getBranchName());
                returnVisitData.setCallNumber(metaInfo.getCallNumber());
                returnVisitData.setAgentId(metaInfo.getAgentId());
                returnVisitData.setListId(metaInfo.getListId());
                returnVisitData.setCarNumber(metaInfo.getCarNumber());
                returnVisitData.setIsBlackList("n");
                returnVisitData.setIsWhiteList("0".equals(metaInfo.getIsWhiteList()) ? "n" : "y");
                returnVisitData.setIsRepeat(metaInfo.getIsRepeat());
                returnVisitData.setIsReturn("n");
                returnVisitData.setIsConnected("noCall");
                returnVisitData.setCallDate(new Date());
                returnVisitData.setFst(new Date());
                returnVisitDataMapper.save(returnVisitData);

                if ("0".equals(metaInfo.getIsWhiteList()) && "n".equals(metaInfo.getIsRepeat())) {
                    osw.write(metaInfo.getMetaInfo());
                    osw.write('\n');
                }
                returnVisitMapper.copyToHistory(metaInfo.getId());
                returnVisitMapper.deleteById(metaInfo.getId());
            }
        }

        if (totalNum == 0) {
            log.info("No returnVisit call meta info found.");
            return;
        } else {
            log.info("Generate call tasks, {}", totalNum);
        }

        callNumConfigMapper.increaseNum(callNumConfigPage.getId(), 0);

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
            log.warn(pageResult.getMessage());
        } else {
            log.info(pageResult.getMessage());
        }
    }
}
