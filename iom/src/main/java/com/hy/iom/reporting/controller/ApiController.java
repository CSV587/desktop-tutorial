package com.hy.iom.reporting.controller;

import com.hy.iom.base.api.ICMApi;
import com.hy.iom.base.api.IPMApi;
import com.hy.iom.common.page.ChartResult;
import com.hy.iom.entities.CustomerInfo;
import com.hy.iom.entities.RecordInfo;
import com.hy.iom.reporting.page.RecordInfoPage;
import com.hy.iom.reporting.utils.ReportingUtils;
import com.hy.iom.service.CallContentService;
import com.hy.iom.service.CustomerInfoService;
import com.hy.iom.service.InfoReflexService;
import com.hy.iom.service.RecordInfoService;
import com.hy.iom.voice.controller.VoiceController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class ApiController {

    private final IPMApi ipmApi;
    private final ICMApi icmApi;
    private final CustomerInfoService customerInfoService;
    private final CallContentService callContentService;
    private final RecordInfoService recordInfoService;
    private final InfoReflexService infoReflexService;

    @Autowired
    public ApiController(IPMApi ipmApi, ICMApi icmApi, CustomerInfoService customerInfoService, CallContentService callContentService, RecordInfoService recordInfoService, InfoReflexService infoReflexService) {
        this.ipmApi = ipmApi;
        this.icmApi = icmApi;
        this.customerInfoService = customerInfoService;
        this.callContentService = callContentService;
        this.recordInfoService = recordInfoService;
        this.infoReflexService = infoReflexService;
    }

    /**
     * 项目列表
     *
     * @return 字符串
     */
    @RequestMapping(value = "/connector/get_project", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String queryUserProject(@RequestParam String token) {
        return ipmApi.queryUserProjectList(token);
    }

    /**
     * 项目列表
     *
     * @return 字符串
     */
    @RequestMapping(value = "/connector/get_rule_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String queryProjectRule(@RequestParam String projectId) {
        return icmApi.getRuleList(projectId);
    }

    /**
     * 流程列表
     *
     * @return 字符串
     */
    @RequestMapping(value = "/connector/get_flow_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String queryProjectFlow(@RequestParam String projectId) {
        return icmApi.getFlowList(projectId);
    }

    /**
     * 任务列表
     *
     * @return 字符串
     */
    @RequestMapping(value = "/connector/get_task_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String queryRuleTask(@RequestParam String ruleId) {
        return icmApi.getTaskList(ruleId);
    }

    /**
     * 实时监控
     */
    @RequestMapping(value = "/api/callMonitor", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String requestMonitor(@RequestParam String token) {
        return ipmApi.queryUserProjectList(token);
    }

    /**
     * 客户基本信息
     *
     * @param uuid 唯一标识
     * @return 文本
     */
    @GetMapping("/api/customerInfo/{uuid}")
    public ChartResult customerInfo(@PathVariable("uuid") String uuid) {
        return ChartResult.success(ReportingUtils.toListMap(customerInfoService.selectByUuid(new CustomerInfo(uuid))));
    }

    /**
     * 对话详情
     *
     * @param uuid 唯一标识
     * @return 文本
     */
    @GetMapping("/api/callContent/{uuid}")
    public ChartResult recordInfoDetail(@PathVariable("uuid") String uuid) {
        return ChartResult.success(ReportingUtils.toListMap(callContentService.selectMatchResultByUuid(uuid)));
    }

    /**
     * 录音基本信息
     *
     * @param uuid 唯一标识
     * @return 文本
     */
    @GetMapping("/api/voiceInfo/{uuid}")
    public ChartResult voiceInfo(@PathVariable("uuid") String uuid) {
        return ChartResult.success(ReportingUtils.toListMap(recordInfoService.selectByUuid(new RecordInfoPage(uuid))));
    }

    /**
     * 根据UUID获取录音归属项目下用户信息的key
     *
     * @param uuid 唯一标识
     * @return 文本
     */
    @GetMapping("/api/customerInfoState/{uuid}")
    public ChartResult customerInfoState(@PathVariable("uuid") String uuid) {
        String projectId = recordInfoService.selectProjectIdByUuid(uuid);
        if (StringUtils.isNotBlank(projectId))
            return customInfoNames(projectId);
        else
            return ChartResult.failure("查询失败");
    }


    /**
     * 获取项目下所有用户信息的key
     *
     * @param projectId 项目Id
     * @return 文本
     */
    @GetMapping("/api/customInfoNames")
    public ChartResult customInfoNames(@RequestParam("projectId") String projectId) {
        return ChartResult.success(ReportingUtils.toListMap(infoReflexService.selectInfoByProjectId(projectId)));
    }

    @RequestMapping(value = "/api/download/{uuid}")
    public void downloadResource(@PathVariable String uuid, HttpServletResponse response,
                                 HttpServletRequest request) {
        List<RecordInfo> recordInfos = recordInfoService.selectByUuid(new RecordInfoPage(uuid));
        VoiceController.getRecordByUuid(uuid, response, request, recordInfos);
    }
}
