package com.hy.reporting.callcyclemanage.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.base.api.IPMApi;
import com.hy.base.bean.BeanTools;
import com.hy.base.excel.ReportExcel;
import com.hy.base.page.PageBody;
import com.hy.base.page.ResponseResult;
import com.hy.base.utils.PageUtils;
import com.hy.error.ErrorUtil;
import com.hy.mapper.oracle.CallCycleMapper;
import com.hy.reporting.callcyclemanage.page.CallCyclePage;
import com.hy.reporting.callcyclemanage.service.CallCycleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/4.
 * @Description :
 */

@Slf4j
@Controller
@RequestMapping("/reporting/callCycle")
public class CallCycleController {

    private CallCycleMapper callCycleMapper;

    private CallCycleService callCycleService;

    private IPMApi ipmApi;

    /**
     * .
     * 构造函数
     */
    public CallCycleController(final CallCycleMapper callCycleMapper, final CallCycleService callCycleService, final IPMApi ipmApi) {
        this.callCycleMapper = callCycleMapper;
        this.callCycleService = callCycleService;
        this.ipmApi = ipmApi;
    }

    /**
     * .
     * index页面
     *
     * @return 对应前端文件路径
     */
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "/returnPeriodManage";
    }

    /**
     * .
     * 推送任务列表
     */
    @ResponseBody
    @GetMapping("/queryPushTask")
    public ResponseResult queryPushTask() {
        return ResponseResult.success("success", callCycleMapper.queryPushTask());
    }

    /**
     * .
     * 回访任务列表
     */
    @ResponseBody
    @GetMapping("/queryReturnTask")
    public ResponseResult queryReturnTask() {
        return ResponseResult.success("success", callCycleMapper.queryReturnTask());
    }

    /**
     * .
     * 任务状态
     */
    @ResponseBody
    @GetMapping("/queryTaskStatus")
    public ResponseResult queryTaskStatus() {
        List<Map<String, String>> l = new LinkedList<>();
        Map<String, String> m1 = new LinkedHashMap<>();
        m1.put("taskState", "0");
        m1.put("taskStateName","未完成");
        l.add(m1);
        Map<String, String> m2 = new LinkedHashMap<>();
        m2.put("taskState", "1");
        m2.put("taskStateName","已完成");
        l.add(m2);
        return ResponseResult.success("success", l);
    }

    /**
     * .
     * 查询接口
     */
    @ResponseBody
    @PostMapping("/query")
    public ResponseResult query(@RequestBody CallCyclePage callCyclePage) {
        PageHelper.startPage(callCyclePage.getCurrent(), callCyclePage.getPageSize());
        Page<CallCyclePage> p = (Page) callCycleMapper.queryList(callCyclePage);
        for(CallCyclePage ccp : p){
            if(ccp.getTaskState() == 0) {
                ccp.setTaskStateName("未完成");
            } else {
                ccp.setTaskStateName("已完成");
            }
        }
        PageBody pageBody = new PageBody();
        pageBody.setValue(p);
        pageBody.setCount(p.getTotal());
        pageBody.setTHeader(PageUtils.getExcelTitle(CallCyclePage.class));
        return ResponseResult.success("success", pageBody);
    }

    /**
     * .
     * 编辑接口
     */
    @ResponseBody
    @GetMapping("/edit")
    public ResponseResult edit(@RequestParam String pushTaskId) {
        return ResponseResult.success("success", callCycleMapper.queryById(pushTaskId));
    }

    /**
     * .
     * 保存接口
     */
    @ResponseBody
    @PostMapping("/save")
    public ResponseResult save(@RequestBody CallCyclePage callCyclePage, @RequestParam String token) {
        callCycleMapper.save(callCyclePage);
        callCycleMapper.updateEditorAndDate(callCyclePage.getPushTaskId(), ipmApi.getUserName(token), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return ResponseResult.success("保存成功！");
    }

    /**
     * .
     * 终止任务接口
     */
    @ResponseBody
    @GetMapping("/terminate")
    public ResponseResult terminate(@RequestParam String pushTaskId) {
        callCycleService.terminate(pushTaskId);
        return ResponseResult.success("任务已终止！");
    }

    /**
     * .
     * Excel导出
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @GetMapping("/execl")
    public void excel(final HttpServletRequest request,
                      final HttpServletResponse response) {
        Map<String, String[]> objectMap = request.getParameterMap();
        CallCyclePage page = new CallCyclePage();
        try {
            BeanTools.transMap2Bean(page, objectMap);
            List<CallCyclePage> pages = callCycleMapper.queryList(page);
            for(CallCyclePage ccp : pages){
                if(ccp.getTaskState() == 0) {
                    ccp.setTaskStateName("未完成");
                } else {
                    ccp.setTaskStateName("已完成");
                }
            }
            ReportExcel.excelExport(pages, "回访周期管理报表", CallCyclePage.class, 1, response, request);
        } catch (Exception e) {
            log.error("下载excel报错:{}", ErrorUtil.getStackTrace(e));
        }
    }

    /**
     * .
     * 上传入库
     */
    @ResponseBody
    @GetMapping("/uploadandStore")
    public ResponseResult uploadandStore(@RequestBody MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseResult.fail("文件为空！");
            } else {
                callCycleService.store(file);
            }
        } catch (IOException e) {
            log.error("入库失败！");
        }
        return ResponseResult.fail("上传失败！");
    }

}
