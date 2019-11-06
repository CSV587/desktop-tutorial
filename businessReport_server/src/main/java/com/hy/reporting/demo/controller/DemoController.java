package com.hy.reporting.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * .
 * Created by of liaoxg
 * date: 2018/9/8
 * user: lxg
 * package_name: com.hy.cpic.reporting.callbackrate.controller
 */
@Controller
@RequestMapping("/reporting/demo")
public class DemoController {

    /**
     * .
     * index页面
     *
     * @return 对应前端文件路径
     */
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "/demo";
    }
}
