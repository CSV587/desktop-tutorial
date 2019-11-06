package com.hy.reporting.callback.controller;

import com.github.pagehelper.Page;
import com.hy.base.bean.BeanTools;
import com.hy.base.excel.ReportExcel;
import com.hy.base.page.PageBody;
import com.hy.base.page.ResponseResult;
import com.hy.base.utils.PageUtils;
import com.hy.error.ErrorUtil;
import com.hy.reporting.callback.page.CallBackPage;
import com.hy.reporting.callback.service.CallBackService;
import com.hy.task.CallBackVisitTask;
import com.hy.task.IssuedTask;
import com.hy.task.RecordTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-08
 * user: lxg
 * package_name: com.hy.reporting.callback.controller
 */
@Slf4j
@Controller
@RequestMapping("/reporting/callBack")
public class CallBackController {

    /**
     * .
     * 录音文件任务
     */
    private final RecordTask recordTask;

    /**
     * .
     * excel文件任务
     */
    private final IssuedTask issuedTask;

    /**
     * .
     * excel文件任务
     */
    private final CallBackVisitTask callBackVisitTask;


    /**
     * .
     * callBackService
     */
    private final CallBackService callBackService;

    /**
     * .
     * 构造函数
     * 依赖注入
     *
     * @param service      CallBackService
     * @param exportRecord ExportRecordFiles
     * @param task         IssuedTask
     * @param task1        CallBackVisitTask
     */
    public CallBackController(final CallBackService service,
                              final RecordTask exportRecord,
                              final IssuedTask task,
                              final CallBackVisitTask task1
    ) {
        this.callBackService = service;
        this.recordTask = exportRecord;
        this.issuedTask = task;
        this.callBackVisitTask = task1;
    }

    /**
     * .
     * index页面
     *
     * @return 对应前端文件路径
     */
    @GetMapping(value = "index")
    public String index() {
        return "/failureReturn";
    }


    /**
     * .
     * 查询数据
     *
     * @param callBackPage CallBackPage
     * @return 返回指定页数据
     */
    @ResponseBody
    @PostMapping(value = "query")
    public ResponseResult query(
        @RequestBody final CallBackPage callBackPage) {
        Page page = callBackService.query(callBackPage);
        PageBody body = new PageBody();
        body.setValue(page.getResult());
        body.setCount(page.getTotal());
        body.setTHeader(PageUtils.getExcelTitle(CallBackPage.class));
        return ResponseResult.success("success", body);
    }

    /**
     * .
     * 初始化字段头
     *
     * @return 结果
     */
    @ResponseBody
    @PostMapping(value = "init")
    public ResponseResult init() {
        PageBody body = new PageBody();
        body.setTHeader(PageUtils.getExcelTitle(CallBackPage.class));
        body.setValue(new ArrayList());
        body.setCount((long) 0);
        return ResponseResult.success("success", body);
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
        CallBackPage page = new CallBackPage();
        try {
            BeanTools.transMap2Bean(page, objectMap);
            List<CallBackPage> pages = callBackService.queryAll(page);
            ReportExcel.excelExport(pages,
                "失效回访明细报表",
                CallBackPage.class,
                1,
                response,
                request);
        } catch (Exception e) {
            log.error("下载excel报错:{}", ErrorUtil.getStackTrace(e));
        }
    }


    /**
     * .
     * 录音导出
     *
     * @return ResponseResult
     */
    @ResponseBody
    @GetMapping("/saveRecord")
    public ResponseResult saveRecord() {
        recordTask.exportRecord();
        return ResponseResult.success("操作成功", null);
    }

    /**
     * .
     * 呼叫名单生成
     *
     * @throws Exception Exception
     */
    @ResponseBody
    @GetMapping("/createCallList")
    public void createCallList() throws Exception {
        issuedTask.postPaper();
    }


    /**
     * .
     * 上传录音文件
     */
    @ResponseBody
    @GetMapping("/uploadRecord")
    public void uploadRecord() {
        recordTask.uploadRecord();
    }

    /**
     * .
     * 回传问卷
     */
    @ResponseBody
    @GetMapping("/writeBack")
    public void writeBack() {
        callBackVisitTask.writeBackTask();
    }

    /**
     * .
     * 无效数据回传问卷
     */
    @ResponseBody
    @GetMapping("/invalid")
    public void invalidTask() {
        callBackVisitTask.invalidTask();
    }

}
