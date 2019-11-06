package com.hy.controller;

import com.hy.reporting.callback.service.CallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-25
 * user: lxg
 * package_name: com.hy.controller
 */
@Slf4j
@Controller
@RequestMapping("/connector")
public class ConnectorController {
    /**
     * .
     * callBackService
     */
    private final CallBackService callBackService;

    /**
     * .
     * 构造函数
     *
     * @param service CallBackService
     */
    public ConnectorController(final CallBackService service) {
        this.callBackService = service;
    }

    /**
     * .
     * 名单派发通知
     *
     * @param xmlStr 消息体
     * @return 处理结果
     */
    @ResponseBody
    @PostMapping(value = "notice")
    public String notice(@RequestBody final String xmlStr) {
        log.debug("xmlStr:{}", xmlStr);
        long startTime = System.currentTimeMillis();
        String result = callBackService.parseFile(xmlStr);
        long endTime = System.currentTimeMillis();
        log.debug("导入耗时:{}ms", endTime - startTime);
        return result;
    }


    /**
     * .
     * 测试录音文件上传
     *
     * @param xmlStr 消息体
     * @return 处理结果
     * @throws Exception Exception
     */
    @ResponseBody
    @PostMapping(value = "noticeRecord")
    public String testNotice(@RequestBody final String xmlStr)
        throws Exception {
        log.debug("xmlStr:{}", xmlStr);
        long startTime = System.currentTimeMillis();
        String result = callBackService.testParseFile(xmlStr);
        long endTime = System.currentTimeMillis();
        log.debug("导入耗时:{}ms", endTime - startTime);
        return result;
    }


    /**
     * .
     * 测试问卷回传
     *
     * @param xmlStr 消息体
     * @return 处理结果
     * @throws Exception Exception
     */
    @ResponseBody
    @PostMapping(value = "noticePaper")
    public String testPaperNotice(@RequestBody final String xmlStr)
        throws Exception {
        log.debug("xmlStr:{}", xmlStr);
        long startTime = System.currentTimeMillis();
        String result = callBackService.testParsePaper(xmlStr);
        long endTime = System.currentTimeMillis();
        log.debug("导入耗时:{}ms", endTime - startTime);
        return result;
    }


    /**
     * .
     * 测试回访历史回传
     *
     * @param xmlStr 消息体
     * @return 处理结果
     * @throws Exception Exception
     */
    @ResponseBody
    @PostMapping(value = "noticeVisitHistory")
    public String testVisitHistoryNotice(@RequestBody final String xmlStr)
        throws Exception {
        log.debug("xmlStr:{}", xmlStr);
        long startTime = System.currentTimeMillis();
        String result = callBackService.testParseVisitHistory(xmlStr);
        long endTime = System.currentTimeMillis();
        log.debug("导入耗时:{}ms", endTime - startTime);
        return result;
    }
}
