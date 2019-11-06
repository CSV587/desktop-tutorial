package com.hy.reporting.questionnaire.service;

import com.github.pagehelper.Page;
import com.hy.base.api.IPMApi;
import com.hy.error.ErrorUtil;
import com.hy.mapper.oracle.PaperMapper;
import com.hy.reporting.questionnaire.page.*;
import com.hy.util.Num2CN;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/8/12.
 * @Description :
 */
@Slf4j
@Service
public class PaperService {

    private final PaperMapper paperMapper;

    private final IPMApi ipmApi;

    private final Num2CN n2c;

    /**
     * .
     * 构造函数
     *
     * @param paperMapper PaperMapper
     * @param ipmApi      IPMApi
     * @param n2c
     */
    public PaperService(PaperMapper paperMapper, IPMApi ipmApi, Num2CN n2c) {
        this.paperMapper = paperMapper;
        this.ipmApi = ipmApi;
        this.n2c = n2c;
    }


    //将条件json串转为string
    public String parseCondition(List<LinkedHashMap<String, Object>> list) {
        String condition = "";
        for (Map<String, Object> m : list) {
            String sign = null;
            switch (m.get("relation").toString()) {
                case "equal":
                    sign = "=";
                    break;
                case "more":
                    sign = ">";
                    break;
                case "less":
                    sign = "<";
                    break;
                case "neq":
                    sign = "≠";
                    break;
                case "moreEqual":
                    sign = "≥";
                    break;
                case "lessEqual":
                    sign = "≤";
                    break;
            }
            String tmp = m.get("conditionOne").toString() + sign + m.get("conditionTwo").toString();
            condition = condition + tmp + ",";
        }
        return condition.substring(0, condition.length() - 1);
    }

    public List<String> parseRelation(char[] s) {
        List<String> l = new ArrayList<>();
        for (char c : s) {
            if (c == '=') {
                l.add("=");
                l.add("equal");
            }
            if (c == '≠') {
                l.add("≠");
                l.add("neq");
            }
            if (c == '>') {
                l.add(">");
                l.add("more");
            }
            if (c == '<') {
                l.add("<");
                l.add("less");
            }
            if (c == '≥') {
                l.add("≥");
                l.add("moreEqual");
            }
            if (c == '≤') {
                l.add("≤");
                l.add("lessEqual");
            }
        }
        return l;
    }

    public Page selectPaperInfoByCondition(PaperPage paperPage) {
        Page<Map<String, Object>> p = (Page<Map<String, Object>>) paperMapper.selectPaperInfoByCondition(paperPage);
        return p;
    }

    public void addPaper(PaperPage paperPage) {
        paperMapper.addPaper(paperPage);
    }

    public int delPaperById(String id) {
        return paperMapper.delPaperById(id);
    }

    public Page selectQuestionBySort(String sort) {
        Page<QuestionPage> page = new Page();
        List<Map<String, Object>> p = paperMapper.selectQuestionBySort(sort);
        for (Map<String, Object> map : p) {
            QuestionPage qp = new QuestionPage();
            qp.setCreateDate(map.get("CREATEDATE").toString());
            qp.setQuestionId(map.get("QUESTIONID").toString());
            qp.setLoid(map.get("LOID").toString());
            qp.setIdentifier(Integer.valueOf(map.get("IDENTIFIER").toString()));
            qp.setQuestionName("问题" + n2c.cvt(Long.valueOf(map.get("IDENTIFIER").toString()), true));
            List<ChildQuestion2> cq = paperMapper.selectChildQuestionByParentId(qp.getQuestionId());
            for (ChildQuestion2 cq2 : cq) {
                cq2.setQuestionName("问题" + n2c.cvt(Long.valueOf(cq2.getIdentifier()), true));
            }
            ChildQuestion2 ex = new ChildQuestion2();
            ex.setSeq(0);
            ex.setChildId(UUID.randomUUID().toString());
            ex.setQuestionName("问题" + n2c.cvt(Long.valueOf(map.get("IDENTIFIER").toString()), true));
            ex.setChildName("添加一个子问题");
            ex.setIdentifier(Integer.valueOf(map.get("IDENTIFIER").toString()));
            ex.setParentId(map.get("QUESTIONID").toString());
            cq.add(ex);
            qp.setData(cq);
            page.add(qp);
        }
        return page;
    }

    public void addQuestion(String sort, String token) {
        QuestionPage2 qp = new QuestionPage2();
        qp.setId(UUID.randomUUID().toString());
        qp.setCreateDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        int idf = 0;
        Map<String, Object> m = paperMapper.selectMaxIdentifier(sort);
        if (m != null) {
            idf = Integer.parseInt(m.get("count").toString());
        }
        qp.setQuestionName("问题" + n2c.cvt((long) (idf + 1), true));
        qp.setQuestionId(UUID.randomUUID().toString());
        qp.setPaperId(sort);
        qp.setLoid(ipmApi.getUserId(token));
        qp.setIdentifier(idf + 1);
        paperMapper.addQuestion(qp);
    }

    public void addChildQuestion(ChildQuestion2 childQuestion) {
        paperMapper.addChildQuestion(childQuestion);
    }

    public void delChildQuestion(String id) {
        paperMapper.delChildQuestion(id);
    }

    public ChildQuestion3 editChildQuestion(String id) {
        ChildQuestion2 cq2 = paperMapper.editChildQuestion(id);
        String s = cq2.getCondition();
        String[] ss = s.split(",");
        List<Map<String, String>> list = new LinkedList<>();
        for (String tmp : ss) {
            Map<String, String> m = new LinkedHashMap<>();
            List<String> l = parseRelation(tmp.toCharArray());
            int c = tmp.indexOf(l.get(0));
            String left = tmp.substring(0, c);
            String right = tmp.substring(c + 1);
            m.put("conditionOne", left);
            m.put("conditionTwo", right);
            m.put("relation", l.get(1));
            list.add(m);
        }
        ChildQuestion3 cq = new ChildQuestion3();
        cq.setChildId(cq2.getChildId());
        cq.setChildName(cq2.getChildName());
        cq.setQuestionName("问题" + n2c.cvt((long) cq2.getIdentifier(), true));
        cq.setQuestionId(cq2.getQuestionId());
        cq.setCondition(list);
        cq.setSubjectivity(cq2.getSubjectivity());
        cq.setIdentifier(cq2.getIdentifier());
        cq.setSeq(cq2.getSeq());
        return cq;
    }

    public List<String> queryChildIdByCondition(String fid, Map<String, String> m) {
        try {
            List<ChildQuestion4> l = paperMapper.selectChildQuestionByFlowId(fid);
            Map<String, List<ChildQuestion3>> mapChildQuestion = new HashMap<>();
            List<String> questionId = new ArrayList<>();
            for (ChildQuestion4 cq2 : l) {
                String s = cq2.getCondition();
                String[] ss = s.split(",");
                List<Map<String, String>> list = new LinkedList<>();
                for (String tmp : ss) {
                    Map<String, String> mc = new LinkedHashMap<>();
                    List<String> t = parseRelation(tmp.toCharArray());
                    int c = tmp.indexOf(t.get(0));
                    String left = tmp.substring(0, c).trim();
                    String right = tmp.substring(c + 1).trim();
                    mc.put("conditionOne", left);
                    mc.put("relation", t.get(1));
                    mc.put("conditionTwo", right);
                    list.add(mc);
                }
                ChildQuestion3 cq = new ChildQuestion3();
                cq.setCondition(list);
                cq.setChildId(cq2.getChildId());
                cq.setParentId(cq2.getParentId());
                List<ChildQuestion3> items = mapChildQuestion.get(cq2.getParentId());
                if (items == null) {
                    items = new ArrayList<>();
                    questionId.add(cq2.getParentId());
                }
                items.add(cq);
                mapChildQuestion.put(cq2.getParentId(), items);
            }

            List<String> idList = new LinkedList<>();
            int index = 0;
            for (String parentId : questionId) {
                List<ChildQuestion3> items = mapChildQuestion.get(parentId);
                idList.add(index, items.get(0).getChildId());
                for (ChildQuestion3 item : items) {
                    boolean flag = true;
                    for (Map<String, String> conditions : item.getCondition()) {
                        String name = conditions.get("conditionOne");
                        String relate = conditions.get("relation");
                        String value = conditions.get("conditionTwo");
                        switch (relate) {
                            case "equal":
                                if (!value.equals(m.get(name))) {
                                    flag = false;
                                }
                                break;
                            case "more":
                                if (Integer.parseInt(m.get(name)) < Integer.parseInt(value) ||
                                    Integer.parseInt(m.get(name)) == Integer.parseInt(value)) {
                                    flag = false;
                                }
                                break;
                            case "less":
                                if (Integer.parseInt(m.get(name)) > Integer.parseInt(value) ||
                                    Integer.parseInt(m.get(name)) == Integer.parseInt(value)) {
                                    flag = false;
                                }
                                break;
                            case "neq":
                                if (value.equals(m.get(name))) {
                                    flag = false;
                                }
                                break;
                            case "moreEqual":
                                if (Integer.parseInt(m.get(name)) < Integer.parseInt(value)) {
                                    flag = false;
                                }
                                break;
                            case "lessEqual":
                                if (Integer.parseInt(m.get(name)) > Integer.parseInt(value)) {
                                    flag = false;
                                }
                                break;
                        }
                        if (!flag) {
                            break;
                        }
                    }
                    if (flag) {
                        idList.set(index, item.getChildId());
                        break;
                    }
                }
                index++;
            }
            return idList;
        } catch (Exception e) {
            log.error("{}", ErrorUtil.getStackTrace(e));
            return new ArrayList<>();
        }
    }
}
