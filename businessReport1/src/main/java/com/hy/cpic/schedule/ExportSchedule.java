package com.hy.cpic.schedule;

import com.hy.cpic.reporting.callbacklist.service.CallBackListService;
import com.hy.cpic.reporting.intentionlist.service.IntentionListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * .
 * Created by of liaoxg
 * date: 2018/10/29
 * user: lxg
 * package_name: com.hy.cpic.schedule
 */
@Service
public class ExportSchedule {
    private static Logger log = LoggerFactory.getLogger(ExportSchedule.class);
    private final CallBackListService callBackListService;
    private final IntentionListService intentionListService;

    @Value(value = "${import.oracle.tmpPath}")
    private String tmpPath;

    @Value(value = "${import.oracle.destPath}")
    private String destPath;

    @Value(value = "${import.oracle.tmpPath1}")
    private String tmpPath1;

    @Value(value = "${import.oracle.destPath1}")
    private String destPath1;

    @Autowired
    public ExportSchedule(CallBackListService callBackListService, IntentionListService intentionListService) {
        this.callBackListService = callBackListService;
        this.intentionListService = intentionListService;
    }

    @Scheduled(cron = "${import.oracle.cron1}")
    private void createListTxt() {
        callBackListService.createListTxt(tmpPath, destPath);
    }

    @Scheduled(cron = "${import.oracle.cron2}")
    private void createIntentionListTxt() {
        intentionListService.createListTxt(tmpPath1, destPath1);
    }

}
