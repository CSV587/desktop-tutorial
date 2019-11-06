package com.hy.base.api;

import com.hy.base.page.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * .
 * api访问接口
 */
@RestController
public class ApiController {

    /**
     * .
     * ipm项目访问接口
     */
    private final IPMApi ipmApi;
    /**
     * .
     * icm访问接口
     */
    private final ICMApi icmApi;

    /**
     * .
     * 构造函数
     *
     * @param api1 ipmApi
     * @param api2 icmApi
     */
    public ApiController(final IPMApi api1,
                         final ICMApi api2) {
        this.ipmApi = api1;
        this.icmApi = api2;
    }

    /**
     * .
     * 项目列表
     *
     * @param token token字符
     * @return 返回项目列表
     */
    @RequestMapping(value = "/connector/get_project",
        method = RequestMethod.GET,
        produces = "application/json;charset=UTF-8")
    public ResponseResult queryUserProject(@RequestParam final String token) {
        return ResponseResult.success("查询成功",
            ipmApi.queryUserProjectList(token));
    }

    /**
     * .
     * 项目对应规则列表
     *
     * @param projectId 项目Id
     * @return 规则列表
     */
    @RequestMapping(value = "/connector/get_rule_list",
        method = RequestMethod.GET,
        produces = "application/json;charset=UTF-8")
    public ResponseResult queryProjectRule(
        @RequestParam final String projectId) {
        return ResponseResult.success("查询成功",
            icmApi.getRuleList(projectId));
    }

    /**
     * .
     * 任务列表
     *
     * @param ruleId 规则Id
     * @return 字符串
     */
    @RequestMapping(value = "/connector/get_task_list",
        method = RequestMethod.GET,
        produces = "application/json;charset=UTF-8")
    public ResponseResult queryRuleTask(@RequestParam final String ruleId) {
        return ResponseResult.success("查询成功",
            icmApi.getTaskList(ruleId));
    }

    /**
     * .
     * 流程列表
     *
     * @param projectId 项目Id
     * @return 字符串
     */
    @RequestMapping(value = "/connector/get_flow_list",
        method = RequestMethod.GET,
        produces = "application/json;charset=UTF-8")
    public ResponseResult queryProjectFlow(
        @RequestParam final String projectId) {
        return ResponseResult.success("查询成功",
            icmApi.getFlowList(projectId));
    }
}
