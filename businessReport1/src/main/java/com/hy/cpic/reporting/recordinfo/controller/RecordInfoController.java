package com.hy.cpic.reporting.recordinfo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.cpic.base.page.ChartResult;
import com.hy.cpic.base.utils.AudioUtils;
import com.hy.cpic.base.utils.DownloadUtils;
import com.hy.util.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 16:03 2018/8/24
 * @ Description ：录音调听详情列表
 * @ Modified By ：
 * @ Version     ：1.0
 */
@Slf4j
@Controller
@RequestMapping("/recordInfo/")
public class RecordInfoController {

    @Value("${system.iom}")
    private String iomUrl;

    /**
     * .
     * 设置audioUtils
     */
    private final AudioUtils audioUtils;

    /**
     * .
     * 构造函数
     *
     * @param utils AudioUtils
     */
    public RecordInfoController(final AudioUtils utils) {
        this.audioUtils = utils;
    }

    /**
     * 对话详情
     *
     * @param uuid 唯一标识
     * @return 返回结果
     */
    @ResponseBody
    @GetMapping("/callContent/{uuid}")
    public ChartResult recordInfoDetail(@PathVariable("uuid") String uuid) {
        String url = iomUrl + "/api/callContent/" + uuid;
        return getChartResult(url);
    }

    /**
     * 客户基本信息
     *
     * @param uuid 唯一标识
     * @return 返回结果
     */
    @ResponseBody
    @GetMapping("/customerInfo/{uuid}")
    public ChartResult customerInfo(@PathVariable("uuid") String uuid) {
        String url = iomUrl + "/api/customerInfo/" + uuid;
        return getChartResult(url);
    }

    /**
     * 录音基本信息
     *
     * @param uuid 唯一标识
     * @return 返回结果
     */
    @ResponseBody
    @GetMapping("/voiceInfo/{uuid}")
    public ChartResult voiceInfo(@PathVariable("uuid") String uuid) {
        String url = iomUrl + "/api/voiceInfo/" + uuid;
        return getChartResult(url);
    }

    private ChartResult getChartResult(String url) {
        String res;
        try {
            res = HttpRequestUtils.httpGet(url, null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONArray resultArray = jsonObject.getJSONArray("data");
            return ChartResult.success(resultArray);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ChartResult.failure("后台异常");
        }
    }

    /**
     * .
     * 根据UUID获取录音归属项目下用户信息的key
     *
     * @param uuid 唯一标识
     * @return 返回结果
     */
    @ResponseBody
    @GetMapping("/customerInfoState/{uuid}")
    public ChartResult customerInfoState(@PathVariable("uuid") String uuid) {
        String url = iomUrl + "/api/customerInfoState/" + uuid;
        return getChartResult(url);
    }

    /**
     * 获取项目下所有用户信息的key
     *
     * @param projectId 项目Id
     * @return 文本
     */
    @ResponseBody
    @GetMapping("/customInfoNames")
    public ChartResult customInfoNames(@RequestParam("projectId") String projectId) {
        String url = iomUrl + "/api/customInfoNames/" + projectId;
        return getChartResult(url);
    }

    @RequestMapping(value = "/download/{uuid}")
    public void downloadResource(@PathVariable String uuid, HttpServletResponse response,
                                 HttpServletRequest request) {
        String fileName = "/tmp/" + uuid + ".wav";
        File file = new File(fileName);
        if (!file.exists()) {
            String url = iomUrl + "/api/download/" + uuid;
            try {
                DownloadUtils.downLoadFromUrl(url, uuid + ".wav", "/tmp", "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (file.exists()) {
            File voiceFile = null;
            try {
                if (file.getName().endsWith(".wav")) {
                    voiceFile = File.createTempFile("temp", ".mp3");
                    if (audioUtils.wav2Mp3(file, voiceFile)) {
                        DownloadUtils.downloadFile(voiceFile, response, request);
                    } else {
                        DownloadUtils.downloadFile(file, response, request);
                    }
                } else {
                    DownloadUtils.downloadFile(file, response, request);
                }
            } catch (Exception e) {
                log.error("录音处理发生异常{}", e.getMessage());
            } finally {
                if (voiceFile != null && voiceFile.exists()) {
                    if (voiceFile.delete()) {
                        log.info("文件删除成功{}", voiceFile.getName());
                    }
                }
                if (file.exists()) {
                    if (file.delete()) {
                        log.info("文件删除成功{}", file.getName());
                    }
                }
            }
        } else {
            log.error("录音文件不存在 路径:{} uuid:{}", file.getAbsolutePath(), uuid);
        }
    }

}
