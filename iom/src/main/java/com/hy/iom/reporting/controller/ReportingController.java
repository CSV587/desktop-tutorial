package com.hy.iom.reporting.controller;

import com.hy.iom.common.page.*;
import com.hy.iom.entities.*;
import com.hy.iom.reporting.page.RecordInfoPage;
import com.hy.iom.reporting.utils.ReportingUtils;
import com.hy.iom.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author yuzhiping
 * @date 2018年12月27日 
 * Description：
 * 控制器优化
 */
@RestController
@RequestMapping("/reporting")
public class ReportingController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private final RecordInfoService recordInfoService;
    private final CustomerInfoService customerInfoService;
    private final CallContentService callContentService;
    private final InfoReflexService infoReflexService;

    @Autowired
    public ReportingController(RecordInfoService recordInfoService, CustomerInfoService customerInfoService, CallContentService callContentService, InfoReflexService infoReflexService) {
        this.recordInfoService = recordInfoService;
        this.customerInfoService = customerInfoService;
        this.callContentService = callContentService;
        this.infoReflexService = infoReflexService;
    }
       
    /**
     * 呼叫列表-调听录音：
     * 
     * 根据UUID获取录音归属项目下用户信息的key
     *
     * @param uuid 唯一标识
     * @return 文本
     */
    @GetMapping("/customerInfoState/{uuid}")
    public ChartResult customerInfoState(@PathVariable("uuid") String uuid) {
    	log.info(uuid+"************");
        String projectId = recordInfoService.selectProjectIdByUuid(uuid);
        if (StringUtils.isNotBlank(projectId))
            return customInfoNames(projectId);
        else
            return ChartResult.failure("查询失败");
    }
    
    /**
     * 呼叫列表-调听录音：
     * 
     * 对话详情
     *
     * @param uuid 唯一标识
     * @return 结果
     */
    @GetMapping("/callContent/{uuid}")
    public ChartResult recordInfoDetail(@PathVariable("uuid") String uuid) {
        return ChartResult.success(ReportingUtils.toListMap(callContentService.selectMatchResultByUuid(uuid)));
    }
  
    /**
     * 呼叫列表-调听录音：
     * 
     * 客户基本信息
     *
     * @param uuid 唯一标识
     * @return 文本
     */
    @GetMapping("/customerInfo/{uuid}")
    public ChartResult customerInfo(@PathVariable("uuid") String uuid) {
        return ChartResult.success(ReportingUtils.toListMap(customerInfoService.selectByUuid(new CustomerInfo(uuid))));
    }
    
    /**
     * 呼叫列表-调听录音：
     * 
     * 录音基本信息
     *
     * @param uuid 唯一标识
     * @return 文本
     */
    @GetMapping("/voiceInfo/{uuid}")
    public ChartResult voiceInfo(@PathVariable("uuid") String uuid) {
        return ChartResult.success(ReportingUtils.toListMap(recordInfoService.selectByUuid(new RecordInfoPage(uuid))));
    }
   
    /**
     * 获取项目下所有用户信息的key
     *
     * @param projectId 项目Id
     * @return 结果
     */
    @GetMapping("/customInfoNames")
    public ChartResult customInfoNames(@RequestParam("projectId") String projectId) {
        return ChartResult.success(ReportingUtils.toListMap(infoReflexService.selectInfoByProjectId(projectId)));
    }

    /**
     * 修改明细显示状态
     *
     * @param projectId 项目Id
     * @return 文本
     */
    @GetMapping("/changeState")
    public ChartResult changeState(@RequestParam("projectId") String projectId, @RequestParam("name") String name, @RequestParam("state") String state) {
        return ChartResult.success(infoReflexService.modifyState(projectId, name, state));
    }
}


