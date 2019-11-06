package com.hy.cpic.base.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    private final IPMApi ipmApi;
    private final ICMApi icmApi;

    public ApiController(IPMApi ipmApi, ICMApi icmApi) {
        this.ipmApi = ipmApi;
        this.icmApi = icmApi;
    }

    /**
     * 项目列表
     *
     * @return
     */
    @RequestMapping(value = "/connector/get_project", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String queryUserProject(@RequestParam String token) {
        return ipmApi.queryUserProjectList(token);
    }

    /**
     * 项目列表
     *
     * @return
     */
    @RequestMapping(value = "/connector/get_rule_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String queryProjectRule(@RequestParam String projectId) {
        return icmApi.getRuleList(projectId);
    }

    /**
     * 实时监控
     */
    @RequestMapping(value = "/api/callMonitor", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String requestMonitor(@RequestParam String token) {
        return ipmApi.queryUserProjectList(token);
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

}
