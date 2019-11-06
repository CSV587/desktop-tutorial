package com.hy.iom.quality.averagetime.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.hy.iom.base.api.IPMApi;
import com.hy.iom.common.page.ChartResult;
import com.hy.iom.common.page.PageResultTM;
import com.hy.iom.quality.averagetime.page.AverageTimePage;
import com.hy.iom.quality.averagetime.service.AverageTimeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * .
 * Created by of liaoxg
 * date: 2019-03-20
 * user: lxg
 * package_name: com.hy.iom.quality.averagetime.controller
 */
@RestController
@RequestMapping("/avgTalkSec")
public class AverageTimeController {

    private final AverageTimeService averageTimeService;
    private final IPMApi api;

    public AverageTimeController(AverageTimeService averageTimeService, IPMApi api) {
        this.averageTimeService = averageTimeService;
        this.api = api;
    }

    @ResponseBody
    @PostMapping(value = "queryPage")
    public PageResultTM query(@RequestBody AverageTimePage page) {
        Page result = averageTimeService.query(page);
        return PageResultTM.success("查询成功", result);
    }

    @ResponseBody
    @PostMapping(value = "add")
    public ChartResult add(@RequestBody AverageTimePage page, @RequestParam String token) {
        String userId = api.getUserId(token);
        page.setFsu(userId);
        page.setLmu(userId);
        JSONObject result = averageTimeService.add(page);
        return formatRes(result);
    }

    @ResponseBody
    @PostMapping(value = "edit")
    public ChartResult edit(@RequestBody AverageTimePage page, @RequestParam String token) {
        String userId = api.getUserId(token);
        page.setLmu(userId);
        JSONObject result = averageTimeService.edit(page);
        return formatRes(result);
    }

    @ResponseBody
    @PostMapping(value = "delByIds")
    public ChartResult delByIds(@RequestBody AverageTimePage page, @RequestParam String token) {
        String userId = api.getUserId(token);
        page.setLmu(userId);
        JSONObject result = averageTimeService.del(page);
        return formatRes(result);
    }

    private ChartResult formatRes(JSONObject result) {
        if (result.getString("code").equals("success")) {
            return ChartResult.success(result);
        } else {
            return ChartResult.failure((String)result.getInnerMap().get("message"));
        }
    }
}
