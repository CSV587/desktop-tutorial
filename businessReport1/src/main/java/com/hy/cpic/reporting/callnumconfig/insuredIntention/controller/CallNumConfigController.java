package com.hy.cpic.reporting.callnumconfig.insuredIntention.controller;

import com.github.pagehelper.Page;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.base.page.TableHeadsFactory;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.BranchCallNumConfigPage;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.CallNumConfigPage;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.service.CallNumConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author wkl
 * @since 2018/12/24.
 */
@Controller
@RequestMapping("/reporting")
public class CallNumConfigController {
    private final CallNumConfigService callNumConfigService;

    @Autowired
    public CallNumConfigController(CallNumConfigService callNumConfigService) {
        this.callNumConfigService = callNumConfigService;
    }

    @RequestMapping(value = "/insuredintention/index", method = RequestMethod.GET)
    public String index() {
        return "/insuredintention";
    }

    @ResponseBody
    @PostMapping(value = "/callNumConfig/query")
    public PageResult queryCallNumConfig(@RequestBody CallNumConfigPage callNumConfigPage, @RequestParam String token) {
        Page result = callNumConfigService.queryCallNumConfig(callNumConfigPage, token);
        return PageResult.success("success", result, TableHeadsFactory.createCallNumConfigHead());
    }

    @ResponseBody
    @GetMapping(value = "/callNumConfig/queryAllList")
    public PageResult queryAllList() {
        List result = callNumConfigService.queryAllCallNumConfigList();
        return PageResult.success("success", result);
    }

    @ResponseBody
    @PostMapping(value = "/callNumConfig/save")
    public PageResult saveCallNumConfig(@RequestBody CallNumConfigPage callNumConfigPage) {
        return callNumConfigService.saveCallNumConfig(callNumConfigPage);
    }

    @ResponseBody
    @PostMapping(value = "/callNumConfig/edit")
    public PageResult editCallNumConfig(@RequestBody CallNumConfigPage callNumConfigPage) {
        return callNumConfigService.editCallNumConfig(callNumConfigPage);
    }

    @ResponseBody
    @GetMapping(value = "/callNumConfig/delete")
    public PageResult deleteCallNumConfig(@RequestParam("id") String id) {
        return callNumConfigService.deleteCallNumConfig(id);
    }

    @ResponseBody
    @PostMapping(value = "/callNumConfig/init")
    public PageResult initCallNumConfig() {
        return PageResult.success("success", new Page(), TableHeadsFactory.createCallNumConfigHead());
    }

    @ResponseBody
    @PostMapping(value = "/branchCallNumConfig/query")
    public PageResult queryBranchCallNumConfig(@RequestBody BranchCallNumConfigPage branchCallNumConfigPage,
                                               @RequestParam String token) {
        Page result = callNumConfigService.queryBranchCallNumConfig(branchCallNumConfigPage, token);
        return PageResult.success("success", result, TableHeadsFactory.createBranchCallNumConfigHead());
    }

    @ResponseBody
    @PostMapping(value = "/branchCallNumConfig/save")
    public PageResult saveBranchCallNumConfig(@RequestBody BranchCallNumConfigPage branchCallNumConfigPage) {
        return callNumConfigService.saveBranchCallNumConfig(branchCallNumConfigPage);
    }

    @ResponseBody
    @PostMapping(value = "/branchCallNumConfig/edit")
    public PageResult editBranchCallNumConfig(@RequestBody BranchCallNumConfigPage branchCallNumConfigPage) {
        return callNumConfigService.editBranchCallNumConfig(branchCallNumConfigPage);
    }

    @ResponseBody
    @GetMapping(value = "/branchCallNumConfig/delete")
    public PageResult deleteBranchCallNumConfig(@RequestParam("branchId") String branchId) {
        String result = callNumConfigService.deleteBranchCallNumConfig(branchId);
        return PageResult.success(result);
    }

    @ResponseBody
    @GetMapping(value = "/branchCallNumConfig/generateTask")
    public PageResult generateTask() throws IOException, InterruptedException {
        callNumConfigService.sortMetaInfoManual();
        return PageResult.success("任务生成成功");
    }

    @ResponseBody
    @PostMapping(value = "/branchCallNumConfig/init")
    public PageResult initBranchCallNumConfig() {
        return PageResult.success("success", new Page(), TableHeadsFactory.createBranchCallNumConfigHead());
    }

    @ResponseBody
    @GetMapping(value = "/branchCallNumConfig/getActivityNameList")
    public PageResult getActivityNameList() {
        return PageResult.success("success", callNumConfigService.getActivityNameList());
    }

    @ResponseBody
    @PostMapping(value = "/branchCallNumConfig/removeActivityName")
    public PageResult removeActivityName(@RequestBody BranchCallNumConfigPage branchCallNumConfigPage) {
        int num = callNumConfigService.removeActivityName(branchCallNumConfigPage.getActivityName());
        return PageResult.success("移除成功:" + num + "条");
    }

    @ResponseBody
    @PostMapping(value = "/branchCallNumConfig/importBranchCallNum")
    public PageResult importBranchCallNum(@RequestParam("callNumConfigId") String callNumConfigId,
                                          @RequestParam("branchCallNumFile") MultipartFile branchCallNumFile) throws IOException {
        return callNumConfigService.importBranchCallNum(callNumConfigId, branchCallNumFile);
    }
}
