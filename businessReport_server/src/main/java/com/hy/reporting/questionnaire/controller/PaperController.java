package com.hy.reporting.questionnaire.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.base.api.IPMApi;
import com.hy.base.page.PageBody;
import com.hy.base.page.ResponseResult;
import com.hy.mapper.oracle.PaperMapper;
import com.hy.reporting.questionnaire.page.*;
import com.hy.reporting.questionnaire.service.PaperService;
import com.hy.util.Num2CN;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/8/12.
 * @Description :
 */
@Slf4j
@Controller
@RequestMapping("/reporting/paper")
public class PaperController {

    private final IPMApi ipmApi;

    private final PaperMapper paperMapper;

    private final PaperService paperService;

    private final Num2CN n2c;

    /**
     * .
     * 构造函数
     */
    public PaperController(final IPMApi ipmApi, PaperMapper paperMapper, PaperService paperService, Num2CN n2c) {
        this.ipmApi = ipmApi;
        this.paperMapper = paperMapper;
        this.paperService = paperService;
        this.n2c = n2c;
    }

    /**
     * .
     * index页面
     *
     * @return 对应前端文件路径
     */
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "/questionNaire";
    }

    /**
     * .
     * 调查问卷列表查询
     */
    @ResponseBody
    @RequestMapping("/paperInfo")
    public ResponseResult paperInfo(@RequestBody PaperPage paperPage) {
        PageHelper.startPage(paperPage.getCurrent(), paperPage.getPageSize());
        Page page = paperService.selectPaperInfoByCondition(paperPage);
        PageBody body = new PageBody();
        body.setValue(page);
        body.setCount(page.getTotal());
        return ResponseResult.success("success", body);
    }

    /**
     * .
     * 调查问卷添加
     */
    @ResponseBody
    @RequestMapping("/addPaper")
    public ResponseResult addPaper(@RequestBody PaperPage paperPage, @RequestParam String token) {
        if(paperMapper.sameNameJugde(paperPage) == 0) {
            paperPage.setLoid(ipmApi.getUserName(token));
            paperPage.setId(UUID.randomUUID().toString());
            paperPage.setSort(UUID.randomUUID().toString());
            paperPage.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            paperService.addPaper(paperPage);
            return ResponseResult.success("添加成功！");
        }
        return ResponseResult.fail("同一项目、流程下问卷名称重复！");
    }

    /**
     * .
     * 调查问卷删除
     */
    @ResponseBody
    @GetMapping("/delPaper/{id}")
    public ResponseResult delPaper(@PathVariable("id") String id) {
        int count = paperService.delPaperById(id);
        return ResponseResult.success("删除成功！");
    }

    /**
     * .
     * 调查问卷编辑
     */
    @ResponseBody
    @GetMapping("/editPaper/{sort}")
    public ResponseResult editPaper(@PathVariable String sort) {
        Page page = paperService.selectQuestionBySort(sort);
        return ResponseResult.success("success", page);
    }

    /**
     * .
     * 添加父问题
     */
    @ResponseBody
    @GetMapping("/addQustion/{sort}")
    public ResponseResult addQustion(@PathVariable("sort") String sort, @RequestParam String token) {
        paperService.addQuestion(sort, token);
        return ResponseResult.success("success");
    }

    /**
     * .
     * 添加子问题
     */
    @ResponseBody
    @PostMapping("/addChildQuestion")
    public ResponseResult addChildQuestion(@RequestBody ChildQuestion childQuestion) {
        ChildQuestion2 cq = new ChildQuestion2();
        cq.setCondition(paperService.parseCondition(childQuestion.getCondition()));
        cq.setQuestionName("问题" + n2c.cvt((long) (childQuestion.getIdentifier()), true));
        cq.setChildName(childQuestion.getChildName());
        cq.setChildId(UUID.randomUUID().toString());
        cq.setIdentifier(childQuestion.getIdentifier());
        cq.setSubjectivity(childQuestion.getSubjectivity());
        cq.setParentId(childQuestion.getQuestionId());
        cq.setSeq(1);
        paperService.addChildQuestion(cq);
        return ResponseResult.success("添加成功！");
    }

    /**
     * .
     * 删除父问题
     */
    @ResponseBody
    @GetMapping("/delQustion/{id}/{idf}")
    public ResponseResult delQustion(@PathVariable("id") String id, @PathVariable("idf") int idf) {
        if(paperMapper.hasChildQuestion(id) == 0){
            paperMapper.updateQuestionIdentifier(idf, id);
            paperMapper.updateChildQuestionIdentifier(idf, id);
            paperMapper.delQuestion(id);
            return ResponseResult.success("删除成功！");
        }
        return ResponseResult.fail("无法删除含有子问题的选项！");
    }

    /**
     * .
     * 修改子问题后的保存
     */
    @ResponseBody
    @PostMapping("/saveChildQuestion")
    public ResponseResult saveChildQuestion(@RequestBody ChildQuestion childQuestion) {
        ChildQuestion2 cq = new ChildQuestion2();
        cq.setCondition(paperService.parseCondition(childQuestion.getCondition()));
        cq.setChildName(childQuestion.getChildName());
        cq.setChildId(childQuestion.getChildId());
        cq.setIdentifier(childQuestion.getIdentifier());
        cq.setSubjectivity(childQuestion.getSubjectivity());
        cq.setParentId(childQuestion.getQuestionId());
        paperMapper.saveChildQuestion(cq);
        return ResponseResult.success("编辑成功！");
    }

    /**
     * .
     * 编辑子问题
     */
    @ResponseBody
    @GetMapping("/editChildQuestion/{childId}")
    public ResponseResult editChildQuestion(@PathVariable("childId") String id) {
        ChildQuestion3 cq3 = paperService.editChildQuestion(id);
        return ResponseResult.success("success", cq3);
    }

    /**
     * .
     * 删除子问题
     */
    @ResponseBody
    @GetMapping("/delChildQuestion/{id}")
    public ResponseResult delChildQuestion(@PathVariable("id") String id) {
        paperService.delChildQuestion(id);
        return ResponseResult.success("删除成功！");
    }

    /**
     * .
     * 获取父问题列表
     */
    @ResponseBody
    @GetMapping("/get_question_list/{id}")
    public ResponseResult getAllQuestion(@PathVariable("id") String id) {
        List<Map<String,String>> l = paperMapper.selectAllQuestionByPaperId(id);
        List<Map<String,String>> ll = new ArrayList<>();
        for(Map m : l) {
            Map<String,String> mm = new LinkedHashMap<>();
            mm.put("questionId",m.get("questionId").toString());
            mm.put("questionName","问题" + n2c.cvt(Long.valueOf(m.get("identifier").toString()),true));
            ll.add(mm);
        }
        return ResponseResult.success("success", ll);
    }

    /**
     * .
     * 保存问卷
     */
    @ResponseBody
    @GetMapping("/savePaper")
    public ResponseResult savePaper() {
        return ResponseResult.success("保存成功！");
    }

    /**
     * .
     * 根据flowId和Condition map查询相匹配的子问题
     */
    @ResponseBody
    @PostMapping("/queryChildIdByCondition")
    public ResponseResult queryChildIdByCondition(@RequestParam String flowId, @RequestBody Map<String, String> map) {
        return ResponseResult.success("success", paperService.queryChildIdByCondition(flowId, map));
    }

}
