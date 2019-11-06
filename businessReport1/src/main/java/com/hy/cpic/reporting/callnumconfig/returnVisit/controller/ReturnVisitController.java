package com.hy.cpic.reporting.callnumconfig.returnVisit.controller;

import com.github.pagehelper.Page;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.base.page.TableHeadsFactory;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.BranchCallNumConfigPage;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.CallNumConfigPage;
import com.hy.cpic.reporting.callnumconfig.returnVisit.service.ReturnVisitService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @author wkl
 * @since 2019/3/5.
 */

@Controller
@RequestMapping("/reporting")
public class ReturnVisitController {

    private final ReturnVisitService returnVisitService;

    public ReturnVisitController(ReturnVisitService returnVisitService) {
        this.returnVisitService = returnVisitService;
    }

    @RequestMapping(value = "/returnVisit/index", method = RequestMethod.GET)
    public String index() {
        return "/returnVisit";
    }

    @ResponseBody
    @PostMapping(value = "/returnVisit/queryCallNumConfig")
    public PageResult queryCallNumConfig(@RequestBody CallNumConfigPage callNumConfigPage, @RequestParam String token) {
        Page result = returnVisitService.queryCallNumConfig(callNumConfigPage, token);
        return PageResult.success("success", result, TableHeadsFactory.createCallNumConfigHead());
    }

    @ResponseBody
    @PostMapping(value = "/returnVisit/editCallNumConfig")
    public PageResult editCallNumConfig(@RequestBody CallNumConfigPage callNumConfigPage) {
        return returnVisitService.editCallNumConfig(callNumConfigPage);
    }

    @ResponseBody
    @GetMapping(value = "/returnVisit/deleteCallNumConfig")
    public PageResult deleteCallNumConfig(@RequestParam("id") String id) {
        return returnVisitService.deleteCallNumConfig(id);
    }

    @ResponseBody
    @PostMapping(value = "/returnVisit/initCallNumConfig")
    public PageResult initCallNumConfig() {
        return PageResult.success("success", new Page(), TableHeadsFactory.createCallNumConfigHead());
    }

    @ResponseBody
    @PostMapping(value = "/returnVisit/queryBranchCallNumConfig")
    public PageResult queryBranchCallNumConfig(@RequestBody BranchCallNumConfigPage branchCallNumConfigPage,
                                               @RequestParam String token) {
        Page result = returnVisitService.queryBranchCallNumConfig(branchCallNumConfigPage, token);
        return PageResult.success("success", result, TableHeadsFactory.createReturnVisitBranchCallNumConfigHead());
    }

    @ResponseBody
    @GetMapping(value = "/returnVisit/generateTask")
    public PageResult generateTask() throws IOException {
        returnVisitService.sortMetaInfoManual();
        return PageResult.success("任务生成成功");
    }

    @ResponseBody
    @PostMapping(value = "/returnVisit/initBranchCallNumConfig")
    public PageResult initBranchCallNumConfig() {
        return PageResult.success("success", new Page(), TableHeadsFactory.createReturnVisitBranchCallNumConfigHead());
    }
}
