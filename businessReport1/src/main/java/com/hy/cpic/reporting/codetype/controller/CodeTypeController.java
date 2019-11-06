package com.hy.cpic.reporting.codetype.controller;

import com.github.pagehelper.Page;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.base.page.TableHeadsFactory;
import com.hy.cpic.reporting.codetype.page.CodeTypePage;
import com.hy.cpic.reporting.codetype.service.CodeTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wkl
 * @since 2019/07/09.
 */
@Controller
@RequestMapping("/reporting/codeType")
public class CodeTypeController {

    private final CodeTypeService codeTypeService;

    public CodeTypeController(CodeTypeService codeTypeService) {
        this.codeTypeService = codeTypeService;
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/codetype";
    }

    @ResponseBody
    @PostMapping(value = "/query")
    public PageResult query(@RequestBody CodeTypePage codeTypePage, @RequestParam String token) {
        Page result = codeTypeService.queryCodeTypeList(codeTypePage, token);
        return PageResult.success("success", result, TableHeadsFactory.createCodeTypeHead());
    }

    @ResponseBody
    @PostMapping(value = "/save")
    public PageResult save(@RequestBody CodeTypePage codeTypePage, @RequestParam String token) {
        return codeTypeService.saveCodeType(codeTypePage, token);
    }

    @ResponseBody
    @PostMapping(value = "/edit")
    public PageResult edit(@RequestBody CodeTypePage codeTypePage, @RequestParam String token) {
        return codeTypeService.editCodeType(codeTypePage, token);
    }

    @ResponseBody
    @GetMapping(value = "/delete")
    public PageResult deleteCallNumConfig(@RequestParam("id") String id) {
        return codeTypeService.delete(id);
    }

    @ResponseBody
    @PostMapping(value = "/init")
    public PageResult init() {
        return PageResult.success("success", new Page(), TableHeadsFactory.createCodeTypeHead());
    }
}
