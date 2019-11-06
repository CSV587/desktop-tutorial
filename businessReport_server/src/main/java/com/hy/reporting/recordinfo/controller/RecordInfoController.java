package com.hy.reporting.recordinfo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.base.api.BasicConfig;
import com.hy.base.page.ResponseResult;
import com.hy.base.utils.DownloadUtils;
import com.hy.error.ErrorUtil;
import com.hy.util.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * .
 *
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

    /**
     * .
     * 对话详情
     *
     * @param uuid 唯一标识
     * @return 返回结果
     */
    @ResponseBody
    @GetMapping("/callContent/{uuid}")
    public ResponseResult recordInfoDetail(
        @PathVariable("uuid") final String uuid) {
        String url = BasicConfig.getIomUrl() + "/api/callContent/" + uuid;
        return getResponseResult(url);
    }

    /**
     * .
     * 客户基本信息
     *
     * @param uuid 唯一标识
     * @return 返回结果
     */
    @ResponseBody
    @GetMapping("/customerInfo/{uuid}")
    public ResponseResult customerInfo(
        @PathVariable("uuid") final String uuid) {
        String url = BasicConfig.getIomUrl() + "/api/customerInfo/" + uuid;
        return getResponseResult(url);
    }

    /**
     * .
     * 录音基本信息
     *
     * @param uuid 唯一标识
     * @return 返回结果
     */
    @ResponseBody
    @GetMapping("/voiceInfo/{uuid}")
    public ResponseResult voiceInfo(
        @PathVariable("uuid") final String uuid) {
        String url = BasicConfig.getIomUrl() + "/api/voiceInfo/" + uuid;
        return getResponseResult(url);
    }

    /**
     * .
     * 获取url对应返回结果
     *
     * @param url url地址
     * @return ResponseResult
     */
    private ResponseResult getResponseResult(final String url) {
        String res;
        try {
            res = HttpRequestUtils.httpGet(url, null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONArray resultArray = jsonObject.getJSONArray("data");
            return ResponseResult.success("查询成功", resultArray);
        } catch (Exception e) {
            log.error(ErrorUtil.getStackTrace(e));
            return ResponseResult.fail("后台异常");
        }
    }

    /**
     * .
     * 流形式下载音频文件
     *
     * @param uuid     uuid
     * @param response HttpServletResponse
     * @param request  HttpServletRequest
     */
    @RequestMapping(value = "/download/{uuid}")
    public void downloadResource(@PathVariable final String uuid,
                                 final HttpServletResponse response,
                                 final HttpServletRequest request) {
        String fileName = "/tmp/" + uuid + ".wav";
        File file = new File(fileName);
        if (!file.exists()) {
            String url = BasicConfig.getIomUrl() + "/api/download/" + uuid;
            try {
                DownloadUtils.downLoadFromUrl(url, uuid + ".wav", "/tmp", "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (file.exists()) {
            DownloadUtils.downloadFile(file, response, request);
            if (file.delete()) {
                log.debug("删除录音文件");
            }
        } else {
            log.error("录音文件不存在 路径:{} uuid:{}", file.getAbsolutePath(), uuid);
        }
    }
}
