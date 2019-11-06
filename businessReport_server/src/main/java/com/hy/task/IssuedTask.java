package com.hy.task;

import com.hy.base.api.BasicConfig;
import com.hy.base.excel.ExcelUtil;
import com.hy.entity.IdType;
import com.hy.error.ErrorUtil;
import com.hy.reporting.callback.entities.CallBackEntities;
import com.hy.reporting.callback.service.CallBackService;
import com.hy.util.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-18
 * user: lxg
 * package_name: com.hy.task
 */
@Slf4j
@Service
@PropertySource(value = "classpath:scheduler.properties", encoding = "UTF-8")
public class IssuedTask {
    /**
     * .
     * 回访数据service 服务
     */
    private final CallBackService callBackService;

    /**
     * .
     *
     * @param service CallBackService
     */
    public IssuedTask(final CallBackService service) {
        this.callBackService = service;
    }

    /**
     * .
     * 临时路径
     */
    @Value("${tmpPath:/tmp}")
    private String tmpPath;


    /**
     * .
     */
    @Value("${issued.ruleId}")
    private String ruleId;

    /**
     * .
     * 下发名单到icm
     *
     * @throws Exception Exception
     */
    @Scheduled(cron = "${issued.cron}")
    public void postPaper()
        throws Exception {
        List<CallBackEntities> callBackEntitiesList
            = callBackService.queryAllPendingData();
        if (callBackEntitiesList.isEmpty()) {
            log.info("无可上传数据，不上传文件");
            return;
        }
        List<CallBackEntities> callList = disposeWordSet(callBackEntitiesList);
        Workbook wb = ExcelUtil.exportExcel(
            callList,
            null,
            CallBackEntities.class,
            0,
            "导入数据");
        String filePath = String.format("%s%s%s.xlsx",
            tmpPath,
            File.separatorChar,
            UUID.randomUUID().toString());
        File file = new File(filePath);
        //创建文件流
        OutputStream stream = new FileOutputStream(file);
        //写入数据
        wb.write(stream);
        //关闭文件流
        stream.close();
        log.debug("写入文件{}成功,共{}条记录", filePath, callBackEntitiesList.size());

        String addTaskUrl = BasicConfig.getIcmUrl() + "/connector/add_task";
        SimpleDateFormat source = new SimpleDateFormat("yyyyMMddHHmmss");

        HashMap<String, Object> reqParam = new HashMap<>();
        reqParam.put("taskName", "task-" + source.format(new Date()));
        reqParam.put("ruleId", ruleId);
        HashMap<String, File> fileParam = new HashMap<>();
        fileParam.put("callFile", file);
        String result =
            HttpRequestUtils.httpPost(
                addTaskUrl, reqParam, fileParam);
        log.info("生成任务返回结果：{}", result);
        FileUtils.deleteQuietly(file);
        log.debug("删除文件{}成功", filePath);
    }

    /**
     * .
     * 处理词集
     *
     * @param callBackEntitiesList 集合
     * @return CallBackEntities 集合
     */
    private List<CallBackEntities> disposeWordSet(
        final List<CallBackEntities> callBackEntitiesList) {
        List<CallBackEntities> callList = new ArrayList<>();
        SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd");
        for (CallBackEntities entities : callBackEntitiesList) {
            String date = entities.getApplicantBirthday();
            entities.setYear(String.format("%s年", date.substring(0, 4)));
            String[] value = date.split("-");
            String month = value[1];
            String day = value[2];
            StringBuilder stringBuilder = getWordSet(month, day);
            stringBuilder.append("|").append(month).append(day);
            if ("01".equals(month)) {
                StringBuilder exclude = getWordSet("11", day);
                stringBuilder.append("&!(").append(exclude).append(")");
            } else if ("02".equals(month)) {
                StringBuilder exclude = getWordSet("12", day);
                stringBuilder.append("&!(").append(exclude).append(")");
            }

            if ("01".equals(day)) {
                for (int i = 10; i < 20; i++) {
                    StringBuilder exclude
                        = getWordSet(month, String.valueOf(i));
                    stringBuilder.append("&!(").append(exclude).append(")");
                }
            } else if ("10".equals(day)) {
                for (int i = 11; i < 20; i++) {
                    StringBuilder exclude
                        = getWordSet(month, String.valueOf(i));
                    stringBuilder.append("&!(").append(exclude).append(")");
                }
            } else if ("02".equals(day)) {
                for (int i = 20; i < 30; i++) {
                    StringBuilder exclude
                        = getWordSet(month, String.valueOf(i));
                    stringBuilder.append("&!(").append(exclude).append(")");
                }
            } else if ("20".equals(day)) {
                for (int i = 21; i < 30; i++) {
                    StringBuilder exclude
                        = getWordSet(month, String.valueOf(i));
                    stringBuilder.append("&!(").append(exclude).append(")");
                }
            } else if ("03".equals(day)) {
                for (int i = 30; i <= 31; i++) {
                    StringBuilder exclude
                        = getWordSet(month, String.valueOf(i));
                    stringBuilder.append("&!(").append(exclude).append(")");
                }
            } else if ("30".equals(day)) {
                for (int i = 31; i <= 31; i++) {
                    StringBuilder exclude
                        = getWordSet(month, String.valueOf(i));
                    stringBuilder.append("&!(").append(exclude).append(")");
                }
            }
            entities.setApplicantBirthday(stringBuilder.toString());
            IdType idType = IdType.getValue(entities.getApplicantCdTp());
            if (idType == null) {
                callBackService.terminateById(entities.getId());
                log.debug("保单号:{},身份证类型无效为:{}",
                    entities.getPolicyNo(), entities.getApplicantCdTp());
                continue;
            }
            entities.setApplicantCdTp(idType.getName());
            try {
                Date birthday = source.parse(entities.getInsuredBirthday());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(birthday);
                calendar.add(Calendar.YEAR, 18);
                Date oneDay = calendar.getTime();
                if (0 > oneDay.compareTo(new Date())) {
                    entities.setInsuredBirthday("成年");
                } else {
                    entities.setInsuredBirthday("未成年");
                }
            } catch (ParseException e) {
                log.error("{}", ErrorUtil.getStackTrace(e));
            }
            callList.add(entities);
        }
        return callList;
    }

    /**
     * .
     * 获取泛化日期
     *
     * @param month 月
     * @param day   日
     * @return 词集
     */
    private StringBuilder getWordSet(final String month,
                                     final String day) {
        String[] days = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二", "十三", "十四", "十五"
            , "十六", "十七", "十八", "十九", "二十", "二十一", "二十二", "二十三", "二十四", "二十五", "二十六", "二十七", "二十八",
            "二十九", "三十", "三十一"};
        int dayIndex = Integer.parseInt(day);
        int monthIndex = Integer.parseInt(month);
        StringBuilder stringBuilder = new StringBuilder();
        appendItem(stringBuilder, month, day);
        appendItem(stringBuilder, days[monthIndex - 1], day);
        appendItem(stringBuilder, days[monthIndex - 1], String.valueOf(dayIndex));
        appendItem(stringBuilder, String.valueOf(monthIndex), day);
        appendItem(stringBuilder, String.valueOf(monthIndex), days[dayIndex - 1]);
        appendItem(stringBuilder,
            String.valueOf(monthIndex), String.valueOf(dayIndex));
        appendItem(stringBuilder, month, String.valueOf(dayIndex));
        appendItem(stringBuilder, month, days[dayIndex - 1]);
        appendItem(stringBuilder, days[monthIndex - 1], days[dayIndex - 1]);
        if (dayIndex >= 10 && dayIndex < 20) {
            appendItem(stringBuilder, days[monthIndex - 1], "一" + days[dayIndex - 1]);
        }
        return stringBuilder;
    }

    /**
     * .
     * 单元增加日期
     *
     * @param stringBuilder 原始字符
     * @param month         月份
     * @param day           日
     */
    private void appendItem(final StringBuilder stringBuilder,
                            final String month,
                            final String day) {
        String item = String.format("%s月%s", month, day)
            .replace("月0", "月");
        if (item.startsWith("0")) {
            item = item.substring(1);
        }
        if (stringBuilder.indexOf(item) < 0) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("|");
            }
            stringBuilder.append(item);
            stringBuilder.append("|")
                .append(item).append("号");
        }
    }
}
