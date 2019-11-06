package com.hy.cpic.reporting.callnumconfig.insuredIntention.service;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.cpic.base.api.ICMApi;
import com.hy.cpic.base.api.IPMApi;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.entities.BranchCallNumConfig;
import com.hy.cpic.entities.CallNumConfig;
import com.hy.cpic.entities.InsuredIntentionMetaInfo;
import com.hy.cpic.mapper.oracle.BranchCallNumConfigMapper;
import com.hy.cpic.mapper.oracle.CallNumConfigMapper;
import com.hy.cpic.mapper.oracle.InsuredIntentionMetaInfoMapper;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.BranchCallNumConfigPage;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.CallNumConfigPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wkl
 * @since 2018/12/24.
 */
@Slf4j
@Service
public class CallNumConfigService {
    private final CallNumConfigMapper callNumConfigMapper;
    private final BranchCallNumConfigMapper branchCallNumConfigMapper;
    private final InsuredIntentionMetaInfoMapper insuredIntentionMetaInfoMapper;
    private final IPMApi ipmApi;
    private final ICMApi icmApi;
    private final SortMetaService sortMetaService;

    public static final String NAME = "投保意向";
    @Value("${system.icm}")
    private String icmUrl;

    @Autowired
    public CallNumConfigService(CallNumConfigMapper callNumConfigMapper,
                                BranchCallNumConfigMapper branchCallNumConfigMapper, IPMApi ipmApi, ICMApi icmApi,
                                InsuredIntentionMetaInfoMapper insuredIntentionMetaInfoMapper,
                                SortMetaService sortMetaService) {
        this.callNumConfigMapper = callNumConfigMapper;
        this.branchCallNumConfigMapper = branchCallNumConfigMapper;
        this.ipmApi = ipmApi;
        this.icmApi = icmApi;
        this.insuredIntentionMetaInfoMapper = insuredIntentionMetaInfoMapper;
        this.sortMetaService = sortMetaService;
    }

    public PageResult saveCallNumConfig(CallNumConfigPage callNumConfigPage) {
        if (callNumConfigMapper.queryCallNumConfigByName(callNumConfigPage).isEmpty()) {
            CallNumConfig callNumConfig = new CallNumConfig();
            callNumConfig.setName(callNumConfigPage.getName().replace(" ", ""));
            callNumConfig.setPath(callNumConfigPage.getPath().replace(" ", ""));
            callNumConfig.setRegexp(callNumConfigPage.getRegexp());
            callNumConfig.setRuleId(callNumConfigPage.getRuleId());
            BranchCallNumConfig branchCallNumConfig = new BranchCallNumConfig();
            branchCallNumConfig.setCallNumConfigId(callNumConfigPage.getId());
            int callMaxNum = callNumConfigMapper.queryCallMaxNum(branchCallNumConfig);
            if (callMaxNum > callNumConfigPage.getCallMaxNum()) {
                return PageResult.fail("最大量不可小于分公司总量！");
            }
            callNumConfig.setCallMaxNum(callNumConfigPage.getCallMaxNum());
            callNumConfig.setProjectId(callNumConfigPage.getProjectId());
            callNumConfig.setBusinessType(callNumConfigPage.getBusinessType());
            callNumConfig.setFst(new Date());
            callNumConfig.setLmt(new Date());
            callNumConfig.setFoid(callNumConfigPage.getFoid());
            callNumConfig.setLoid(callNumConfigPage.getLoid());
            callNumConfig.setValidState(CallNumConfig.ValidState.valid);
            callNumConfigMapper.save(callNumConfig);
            return PageResult.success("保存成功！");
        } else {
            return PageResult.fail("名称不可重复！");
        }

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
        BranchCallNumConfig branchCallNumConfig = new BranchCallNumConfig();
        branchCallNumConfig.setCallNumConfigId(callNumConfigPage.getId());
        int callMaxNum = callNumConfigMapper.queryCallMaxNum(branchCallNumConfig);
        if (callMaxNum > callNumConfigPage.getCallMaxNum()) {
            return PageResult.fail("最大量不可小于分公司总量！");
        }
        callNumConfig.setCallMaxNum(callNumConfigPage.getCallMaxNum());
        callNumConfig.setRepeatNum(callNumConfigPage.getRepeatNum());
        callNumConfig.setBusinessType(callNumConfigPage.getBusinessType());
        callNumConfig.setLmt(new Date());
        callNumConfig.setLoid(callNumConfigPage.getLoid());
        callNumConfigMapper.edit(callNumConfig);
        return PageResult.success("编辑成功！");
    }

    public PageResult deleteCallNumConfig(String id) {
        BranchCallNumConfig branchCallNumConfig = new BranchCallNumConfig();
        branchCallNumConfig.setCallNumConfigId(id);
        if (callNumConfigMapper.queryBranchCallNumConfigById(branchCallNumConfig).isEmpty()) {
            CallNumConfig callNumConfig = new CallNumConfig();
            callNumConfig.setId(id);
            callNumConfig.setValidState(CallNumConfig.ValidState.unValid);
            callNumConfigMapper.deleteConfig(callNumConfig);
            return PageResult.success("删除成功");
        } else {
            return PageResult.fail("存在分公司数据，无法删除！");
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public Page queryCallNumConfig(CallNumConfigPage callNumConfigPage, String token) {
        PageHelper.startPage(callNumConfigPage.getCurrent(), callNumConfigPage.getPageSize()); //分页利器
        callNumConfigPage.setName(NAME);
        List<CallNumConfigPage> callNumConfigPages = callNumConfigMapper.queryCallNumConfigByName(callNumConfigPage);
        if (callNumConfigPages.isEmpty()) {
            CallNumConfig callNumConfig = new CallNumConfig();
            callNumConfig.setName(NAME);
            callNumConfig.setValidState(CallNumConfig.ValidState.valid);
            callNumConfig.setTotalNum((long) 0);
            callNumConfig.setRepeatNum(2);
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
                String projectName = ipmApi.getProjectName(token, data.getProjectId());
                data.setProjectName(projectName);
                String foidName = ipmApi.getUserName(token, data.getFoid());
                data.setFoidName(foidName);
                String loidName = ipmApi.getUserName(token, data.getLoid());
                data.setLoidName(loidName);
                int totalNum = callNumConfigMapper.queryTotalNum(data.getId());
                data.setTotalNum((long) totalNum);
            });
        }
        return (Page) callNumConfigPages;
    }

    public List<CallNumConfigPage> queryAllCallNumConfigList() {
        return callNumConfigMapper.queryCallNumConfigList();
    }

    public PageResult saveBranchCallNumConfig(BranchCallNumConfigPage branchCallNumConfigPage) {
        branchCallNumConfigPage.setBranchName(branchCallNumConfigPage.getBranchName().replace(" ", ""));
        if (branchCallNumConfigMapper.queryBranchCallNumConfigByName(branchCallNumConfigPage).isEmpty()) {
            if (branchCallNumConfigPage.getCallNum() > branchCallNumConfigPage.getCallMaxNum()) {
                return PageResult.fail("呼叫量不可大于最大呼叫量！");
            }
            List<CallNumConfigPage> callNumConfigPages =
                branchCallNumConfigMapper.queryCallNumConfigById(branchCallNumConfigPage);
            if (branchCallNumConfigPage.getCallMaxNum() > callNumConfigPages.get(0).getCallMaxNum()) {
                return PageResult.fail("分公司最大呼叫量不可大于总呼叫量！");
            }
            BranchCallNumConfig branchCallNumConfig = new BranchCallNumConfig();
            branchCallNumConfig.setBranchName(branchCallNumConfigPage.getBranchName().replace(" ", ""));
            branchCallNumConfig.setCallNum(branchCallNumConfigPage.getCallNum());
            branchCallNumConfig.setCallMaxNum(branchCallNumConfigPage.getCallMaxNum());
            branchCallNumConfig.setFst(new Date());
            branchCallNumConfig.setLmt(new Date());
            branchCallNumConfig.setFoid(branchCallNumConfigPage.getFoid());
            branchCallNumConfig.setLoid(branchCallNumConfigPage.getLoid());
            branchCallNumConfig.setValidState(BranchCallNumConfig.ValidState.valid);
            branchCallNumConfig.setCallNumConfigId(branchCallNumConfigPage.getCallNumConfigId());
            int totalCallMaxNum = callNumConfigMapper.queryCallMaxNum(branchCallNumConfig);
            if (branchCallNumConfigPage.getCallMaxNum() + totalCallMaxNum > callNumConfigPages.get(0).getCallMaxNum()) {
                return PageResult.fail("分公司最大呼叫总量不可大于总呼叫量！");
            }
            branchCallNumConfigMapper.save(branchCallNumConfig);
            return PageResult.success("保存成功！");
        } else {
            return PageResult.fail("名称不可重复！");
        }

    }

    public PageResult editBranchCallNumConfig(BranchCallNumConfigPage branchCallNumConfigPage) {
        if (StringUtils.isEmpty(branchCallNumConfigPage.getBranchId())) {
            return PageResult.fail("该数据不存在！");
        }
        branchCallNumConfigPage.setBranchName(branchCallNumConfigPage.getBranchName().replace(" ", ""));
        if (branchCallNumConfigMapper.queryBranchCallNumConfigByNameAndId(branchCallNumConfigPage).isEmpty()) {
            if (branchCallNumConfigPage.getCallNum() > branchCallNumConfigPage.getCallMaxNum()) {
                return PageResult.fail("呼叫量不可大于最大呼叫量！");
            }
            List<CallNumConfigPage> callNumConfigPages =
                branchCallNumConfigMapper.queryCallNumConfigById(branchCallNumConfigPage);
            if (branchCallNumConfigPage.getCallMaxNum() > callNumConfigPages.get(0).getCallMaxNum()) {
                return PageResult.fail("分公司最大呼叫量不可大于总呼叫量！");
            }
            BranchCallNumConfig branchCallNumConfig = new BranchCallNumConfig();
            branchCallNumConfig.setBranchId(branchCallNumConfigPage.getBranchId());
            branchCallNumConfig.setBranchName(branchCallNumConfigPage.getBranchName().replace(" ", ""));
            branchCallNumConfig.setCallNum(branchCallNumConfigPage.getCallNum());
            branchCallNumConfig.setCallMaxNum(branchCallNumConfigPage.getCallMaxNum());
            branchCallNumConfig.setLmt(new Date());
            branchCallNumConfig.setLoid(branchCallNumConfigPage.getLoid());
            branchCallNumConfig.setCallNumConfigId(branchCallNumConfigPage.getCallNumConfigId());
            int totalCallMaxNum = callNumConfigMapper.queryCallMaxNum(branchCallNumConfig);
            if (branchCallNumConfigPage.getCallMaxNum() + totalCallMaxNum > callNumConfigPages.get(0).getCallMaxNum()) {
                return PageResult.fail("分公司最大呼叫总量不可大于总呼叫量！");
            }
            branchCallNumConfigMapper.edit(branchCallNumConfig);
            return PageResult.success("编辑成功！");
        } else {
            return PageResult.fail("名称不可重复！");
        }
    }

    public String deleteBranchCallNumConfig(String branchId) {
        BranchCallNumConfig branchCallNumConfig = new BranchCallNumConfig();
        branchCallNumConfig.setBranchId(branchId);
        branchCallNumConfig.setValidState(BranchCallNumConfig.ValidState.unValid);
        branchCallNumConfigMapper.deleteConfig(branchCallNumConfig);
        return ("删除成功");
    }

    public Page queryBranchCallNumConfig(BranchCallNumConfigPage branchCallNumConfigPage, String token) {
        PageHelper.startPage(branchCallNumConfigPage.getCurrent(), branchCallNumConfigPage.getPageSize()); //分页利器
        List<String> businessTypeList = new ArrayList<>();
        String callNumConfigName = "";
        if (!StringUtils.isEmpty(branchCallNumConfigPage.getBranchName()))
            branchCallNumConfigPage.setBranchName(branchCallNumConfigPage.getBranchName().trim());
        List<CallNumConfigPage> callNumConfigPages =
            branchCallNumConfigMapper.queryCallNumConfigById(branchCallNumConfigPage);
        if (null != callNumConfigPages && !callNumConfigPages.isEmpty()) {
            callNumConfigName = callNumConfigPages.get(0).getName();
            if (!StringUtils.isEmpty(callNumConfigPages.get(0).getBusinessType()))
                businessTypeList = Arrays.asList(callNumConfigPages.get(0).getBusinessType().split(","));
        }

        String callNumConfigId = branchCallNumConfigPage.getCallNumConfigId();
        List<BranchCallNumConfigPage> surplusNumMap =
            branchCallNumConfigMapper.querySurplusNum(callNumConfigId, businessTypeList);

        Map<String, Integer> map = new HashMap<>();
        surplusNumMap.forEach(data -> {
            String branchName = data.getBranchName();
            Integer surplusNum = data.getSurplusNum();
            map.put(branchName, surplusNum);
        });


        List<BranchCallNumConfigPage> branchCallNumConfigPages =
            branchCallNumConfigMapper.queryBranchCallNumConfigByName(branchCallNumConfigPage);
        for (BranchCallNumConfigPage data : branchCallNumConfigPages) {
            data.setCallNumConfigName(callNumConfigName);
            String foidName = ipmApi.getUserName(token, data.getFoid());
            data.setFoidName(foidName);
            String loidName = ipmApi.getUserName(token, data.getLoid());
            data.setLoidName(loidName);
            data.setSurplusNum(map.get(data.getBranchName()) == null ? 0 : map.get(data.getBranchName()));
        }
        return (Page) branchCallNumConfigPages;
    }

    @Transactional(rollbackFor = Exception.class)
    public PageResult importBranchCallNum(String callNumConfigId, MultipartFile branchCallNumFile)
        throws IOException {
        if (!Objects.requireNonNull(branchCallNumFile.getOriginalFilename()).endsWith(".xlsx")) {
            return PageResult.fail("文件不是xlsx格式！");
        }

        try (FileInputStream inputStream = (FileInputStream) branchCallNumFile.getInputStream()) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int firstRowNum = sheet.getFirstRowNum(), lastRowNum = sheet.getLastRowNum();

            for (int i = firstRowNum + 1; i < lastRowNum + 1; i++) {
                XSSFRow row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                String company = getCellValue(row.getCell(0));
                if (company.isEmpty())
                    return PageResult.fail("第" + (i + 1) + "行分公司为空！");
                if (company.length() > 20)
                    return PageResult.fail("第" + (i + 1) + "行分公司长度过长！");
                String callNum = getCellValue(row.getCell(1));
                if (callNum.isEmpty())
                    return PageResult.fail("第" + (i + 1) + "行呼叫量为空！");
                String regexp = "^0|[0-9]*[1-9][0-9]*$";
                Pattern pattern = Pattern.compile(regexp);
                Matcher matcher = pattern.matcher(callNum);
                if (!StringUtils.isEmpty(callNum)) {
                    if (!matcher.matches())
                        return PageResult.fail("第" + (i + 1) + "行呼叫量请输入0或正整数");
                }
                String callMaxNum = getCellValue(row.getCell(2));
                if (callMaxNum.isEmpty()) {
                    return PageResult.fail("第" + (i + 1) + "行省份为空");
                }
                if (!StringUtils.isEmpty(callMaxNum)) {
                    if (!matcher.matches())
                        return PageResult.fail("第" + (i + 1) + "行最大量请输入0或正整数");
                }

                BranchCallNumConfigPage branchCallNumConfigPage = new BranchCallNumConfigPage();
                branchCallNumConfigPage.setBranchName(company.replace(" ", ""));
                branchCallNumConfigPage.setCallNum(Integer.valueOf(callNum));
                branchCallNumConfigPage.setCallMaxNum(Integer.valueOf(callMaxNum));
                branchCallNumConfigPage.setCallNumConfigId(callNumConfigId);
                if (branchCallNumConfigMapper.queryBranchCallNumConfigByName(branchCallNumConfigPage).isEmpty()) {
                    if (branchCallNumConfigPage.getCallNum() > branchCallNumConfigPage.getCallMaxNum()) {
                        return PageResult.fail("第" + (i + 1) + "呼叫量不可大于最大呼叫量！");
                    }
                    List<CallNumConfigPage> callNumConfigPages =
                        branchCallNumConfigMapper.queryCallNumConfigById(branchCallNumConfigPage);
                    if (branchCallNumConfigPage.getCallMaxNum() > callNumConfigPages.get(0).getCallMaxNum()) {
                        return PageResult.fail("分公司最大呼叫量不可大于总呼叫量！");
                    }
                    BranchCallNumConfig branchCallNumConfig = new BranchCallNumConfig();
                    branchCallNumConfig.setBranchName(branchCallNumConfigPage.getBranchName().replace(" ", ""));
                    branchCallNumConfig.setCallNum(branchCallNumConfigPage.getCallNum());
                    branchCallNumConfig.setCallMaxNum(branchCallNumConfigPage.getCallMaxNum());
                    branchCallNumConfig.setFst(new Date());
                    branchCallNumConfig.setLmt(new Date());
                    branchCallNumConfig.setValidState(BranchCallNumConfig.ValidState.valid);
                    branchCallNumConfig.setCallNumConfigId(branchCallNumConfigPage.getCallNumConfigId());
                    int totalCallMaxNum = callNumConfigMapper.queryCallMaxNum(branchCallNumConfig);
                    if (branchCallNumConfigPage.getCallMaxNum() + totalCallMaxNum > callNumConfigPages.get(0).getCallMaxNum()) {
                        return PageResult.fail("分公司最大呼叫总量不可大于总呼叫量！");
                    }
                    branchCallNumConfigMapper.save(branchCallNumConfig);
                } else {
                    if (branchCallNumConfigPage.getCallNum() > branchCallNumConfigPage.getCallMaxNum()) {
                        return PageResult.fail("第" + (i + 1) + "呼叫量不可大于最大呼叫量！");
                    }
                    List<CallNumConfigPage> callNumConfigPages =
                        branchCallNumConfigMapper.queryCallNumConfigById(branchCallNumConfigPage);
                    if (branchCallNumConfigPage.getCallMaxNum() > callNumConfigPages.get(0).getCallMaxNum()) {
                        return PageResult.fail("分公司最大呼叫量不可大于总呼叫量！");
                    }
                    BranchCallNumConfig branchCallNumConfig = new BranchCallNumConfig();
                    branchCallNumConfig.setBranchName(branchCallNumConfigPage.getBranchName().replace(" ", ""));
                    branchCallNumConfig.setCallNum(branchCallNumConfigPage.getCallNum());
                    branchCallNumConfig.setCallMaxNum(branchCallNumConfigPage.getCallMaxNum());
                    branchCallNumConfig.setLmt(new Date());
                    branchCallNumConfig.setCallNumConfigId(branchCallNumConfigPage.getCallNumConfigId());
                    int totalCallMaxNum = callNumConfigMapper.queryCallMaxNum(branchCallNumConfig);
                    if (branchCallNumConfigPage.getCallMaxNum() + totalCallMaxNum > callNumConfigPages.get(0).getCallMaxNum()) {
                        return PageResult.fail("分公司最大呼叫总量不可大于总呼叫量！");
                    }
                    branchCallNumConfigMapper.editByBranchName(branchCallNumConfig);
                }
            }
        }
        return PageResult.success("导入成功");
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue().trim();
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue()).trim();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula().trim();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue())).trim(); //日期型
                } else {
                    return String.valueOf(new DecimalFormat("0").format(cell.getNumericCellValue())).trim();//数字型
                }
            default:
                return "";
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "${import.oracle.cron3:0 */5 * * * ?}")
    public void scanFileScheduled() throws SocketException {
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

        String path = callNumConfigPage.getPath();
        path = path.endsWith("/") ? path : path + "/";

        File root = new File(path);
        if (!root.isDirectory()) {
            throw new RuntimeException(root + " is no directory.");
        }
        final Pattern p = Pattern.compile(callNumConfigPage.getRegexp());
        File[] listFiles = root.listFiles(file -> (p.matcher(file.getName()).matches() && file.isFile()));

        for (File file : listFiles != null ? listFiles : new File[0]) {
            dealFile(file, root, callNumConfigPage);
        }

    }

    private void dealFile(File file, File root, CallNumConfigPage callNumConfigPage) {
        File okFile = new File(file.getPath().substring(0, file.getPath().lastIndexOf('.')) + ".OK");
        if (new File(root.getPath() + "/.failure/" + okFile.getName()).exists() ||
            new File(root.getPath() + "/.failure/" + file.getName()).exists() ||
            new File(root.getPath() + "/.success/" + file.getName()).exists() ||
            new File(root.getPath() + "/.success/" + okFile.getName()).exists()) {
            log.warn("file {} repeated emergence, skip", file.getAbsolutePath());
            return;
        }
        if (!okFile.exists()) {
            return;
        }

        saveData(okFile, file, root, callNumConfigPage);

        if (callNumConfigPage.getRepeatNum() >= 2) {
            int num = 0;
            List<String> list = insuredIntentionMetaInfoMapper
                .getRepeatCallNumber(callNumConfigPage.getRepeatNum(), callNumConfigPage.getId());
            if (CollectionUtils.isNotEmpty(list)) {
                int len = list.size();
                for (int i = 0; i < len; i += 1000) {
                    List<String> subList = list.subList(i, i + 1000 > len ? len : i + 1000);
                    insuredIntentionMetaInfoMapper.moveRepeatCallNumber(subList, callNumConfigPage.getId());
                    num += insuredIntentionMetaInfoMapper
                        .clearRepeatCallNumber(subList, callNumConfigPage.getId());
                }
                log.info("clear {} repeat call number", num);
            }
        }
        try {
            FileUtils.moveToDirectory(okFile, new File(root.getPath() + "/.success"), true);
            FileUtils.moveToDirectory(file, new File(root.getPath() + "/.success"), true);
        } catch (IOException e) {
            log.error("", e);
        }

    }

    private void saveData(File okFile, File file, File root, CallNumConfigPage callNumConfigPage) {
        boolean first = true;
        int branchIndex = -1, businessTypeIndex = -1, expireDateIndex = -1, callNumberIndex = -1,
            activityNameIndex = -1;
        int callNumber1Index = -1;
        int callNumber2Index = -1;
        try (RandomAccessFile in = new RandomAccessFile(file, "rw"); FileLock ignored = in.getChannel().tryLock()) {
            if (ignored == null) {
                log.info("{} han been locked by other process.", file.getPath());
                return;
            } else {
                log.info("{} lock success.", file.getPath());
            }
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
                        } else if ("名单类型".equals(header)) {
                            businessTypeIndex = i;
                        } else if ("终保日期".equals(header)) {
                            expireDateIndex = i;
                        } else if ("客户手机号1".equals(header)) {
                            callNumberIndex = i;
                        } else if ("客户手机号2".equals(header)) {
                            callNumber1Index = i;
                        } else if ("客户手机号3".equals(header)) {
                            callNumber2Index = i;
                        } else if ("营销活动名称".equals(header)) {
                            activityNameIndex = i;
                        }
                    }
                    if (branchIndex < 0) {
                        FileUtils.moveToDirectory(okFile, new File(root.getPath() + "/.failure"), true);
                        FileUtils.moveToDirectory(file, new File(root.getPath() + "/.failure"), true);
                        throw new RuntimeException("文件无分公司字段");
                    }
                    continue;
                }
                DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                InsuredIntentionMetaInfo metaInfo = new InsuredIntentionMetaInfo();
                String branchName = split[branchIndex];
                String businessType = businessTypeIndex < 0 ? "" : split[businessTypeIndex];
                Date expireDate = expireDateIndex < 0 ? null : fmt.parse(split[expireDateIndex]);
                String callNumber = callNumberIndex < 0 ? "" : split[callNumberIndex];
                String callNumber1 = callNumber1Index < 0 ? "" : split[callNumber1Index];
                String callNumber2 = callNumber2Index < 0 ? "" : split[callNumber2Index];
                String activityName = activityNameIndex < 0 ? "" : split[activityNameIndex];

                metaInfo.setBranchName(branchName);
                metaInfo.setBusinessType(businessType);
                metaInfo.setExpireDate(expireDate);
                metaInfo.setMetaInfo(str);
                metaInfo.setCallNumConfigId(callNumConfigPage.getId());
                metaInfo.setCallNumber(callNumber);
                metaInfo.setCallNumber1(callNumber1);
                metaInfo.setCallNumber2(callNumber2);
                metaInfo.setFst(new Date());
                metaInfo.setActivityName(activityName);
                insuredIntentionMetaInfoMapper.save(metaInfo);
            }
        } catch (IOException | ParseException e) {
            log.error("{}", e);
        }
    }

    @Scheduled(cron = "${import.oracle.cron4:0 0 7 * * ?}")
    public void sortMetaInfoScheduled() throws IOException, InterruptedException {
        sortMetaService.sortMetaInfo("spring");
    }

    public void sortMetaInfoManual() throws IOException, InterruptedException {
        sortMetaService.sortMetaInfo("manual");
    }

    public List getActivityNameList() {
        return insuredIntentionMetaInfoMapper.getActivityNameList();
    }

    public int removeActivityName(String activityName) {
        insuredIntentionMetaInfoMapper.moveActivityName(activityName);
        return insuredIntentionMetaInfoMapper.deleteByActivityName(activityName);
    }
}
