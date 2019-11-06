package com.hy.cpic.base.api;

import com.hy.cpic.base.page.ChartResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 13:43 2018/8/30
 * @ Description ：获取定制业务前端Mapping路径api类
 * @ Modified By ：
 * @ Version     ：1.0
 */
@Controller
@RequestMapping(value = "/api")
public class PathController {

    /***
     *  cpic 定义映射规则是 保持报表router 与Controller的地址关系为 /reporting/{router}
     *  如果有更复杂映射需求 可以修改此方法
     */
    @ResponseBody
    @RequestMapping(value = "/path/{router}")
    private ChartResult getPath(@PathVariable String router){
        return ChartResult.success("成功",String.format("/reporting/%s/index",router));
    }

}
