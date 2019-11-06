package com.hy.iom.voice.controller;

import com.hy.iom.entities.RecordInfo;
import com.hy.iom.reporting.page.RecordInfoPage;
import com.hy.iom.service.RecordInfoService;
import com.hy.iom.utils.DownloadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping(value = "/voice")
public class VoiceController {

    private final RecordInfoService recordInfoService;

    private final static Logger log = LoggerFactory.getLogger(VoiceController.class);

    @Autowired
    public VoiceController(RecordInfoService recordInfoService) {
        this.recordInfoService = recordInfoService;
    }

    @RequestMapping(value = "/download/{uuid}")
    public void downloadResource(@PathVariable String uuid, HttpServletResponse response,
                                 HttpServletRequest request) {
        List<RecordInfo> recordInfos = recordInfoService.selectByUuid(new RecordInfoPage(uuid));
        getRecordByUuid(uuid, response, request, recordInfos);
    }

    public static void getRecordByUuid(@PathVariable String uuid, HttpServletResponse response, HttpServletRequest request, List<RecordInfo> recordInfos) {
        if (recordInfos != null && recordInfos.size() > 0) {
            File file = new File(recordInfos.get(0).getRecordPath());
            if (file.exists()) {
                DownloadUtils.downloadFile(file, response, request);
            } else {
                log.error("录音文件不存在 路径:{} uuid:{}", file.getAbsolutePath(), uuid);
            }
        }
    }
}