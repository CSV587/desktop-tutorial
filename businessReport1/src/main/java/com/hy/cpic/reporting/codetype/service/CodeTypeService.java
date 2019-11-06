package com.hy.cpic.reporting.codetype.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.cpic.base.api.IPMApi;
import com.hy.cpic.base.page.PageResult;
import com.hy.cpic.entities.CodeType;
import com.hy.cpic.mapper.oracle.CodeTypeMapper;
import com.hy.cpic.reporting.codetype.page.CodeTypePage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author wkl
 * @since 2019/07/09.
 */
@Service
public class CodeTypeService {

    private final CodeTypeMapper codeTypeMapper;
    private final IPMApi ipmApi;

    public CodeTypeService(CodeTypeMapper codeTypeMapper, IPMApi ipmApi) {
        this.codeTypeMapper = codeTypeMapper;
        this.ipmApi = ipmApi;
    }

    public PageResult saveCodeType(CodeTypePage codeTypePage, String token) {
        if (codeTypeMapper.queryCodeTypeByContent(codeTypePage).isEmpty()) {
            CodeType codeType = new CodeType();
            codeType.setType(codeTypePage.getType().replace(" ", ""));
            codeType.setCode(codeTypePage.getCode().replace(" ", ""));
            codeType.setContent(codeTypePage.getContent().replace(" ", ""));
            codeType.setRemark(StringUtils.isEmpty(codeTypePage.getRemark()) ? codeTypePage.getRemark() : codeTypePage.getRemark().replace(" ", ""));
            codeType.setFst(new Date());
            codeType.setLmt(new Date());
            String userId = ipmApi.getUserIdByToken(token);
            codeType.setFoid(userId);
            codeType.setLoid(userId);
            codeTypeMapper.save(codeType);
            return PageResult.success("保存成功！");
        } else {
            return PageResult.fail("名称不可重复！");
        }

    }

    public PageResult editCodeType(CodeTypePage codeTypePage, String token) {
        if (codeTypeMapper.queryCodeTypeByContent(codeTypePage).isEmpty()) {
            CodeType codeType = new CodeType();
            codeType.setId(codeTypePage.getId());
            codeType.setType(codeTypePage.getType().replace(" ", ""));
            codeType.setCode(codeTypePage.getCode().replace(" ", ""));
            codeType.setContent(codeTypePage.getContent().replace(" ", ""));
            codeType.setRemark(StringUtils.isEmpty(codeTypePage.getRemark()) ? codeTypePage.getRemark() : codeTypePage.getRemark().replace(" ", ""));
            codeType.setLmt(new Date());
            String userId = ipmApi.getUserIdByToken(token);
            codeType.setLoid(userId);
            codeTypeMapper.edit(codeType);
            return PageResult.success("编辑成功！");
        } else {
            return PageResult.fail("名称不可重复！");
        }
    }

    public PageResult delete(String id) {
        if (!codeTypeMapper.queryCodeTypeById(id).isEmpty()) {
            CodeType codeType = new CodeType();
            codeType.setId(id);
            codeTypeMapper.deleteCodeType(codeType);
            return PageResult.success("删除成功");
        } else {
            return PageResult.fail("该数据不存在！");
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public Page queryCodeTypeList(CodeTypePage codeTypePage, String token) {
        PageHelper.startPage(codeTypePage.getCurrent(), codeTypePage.getPageSize()); //分页利器
        List<CodeTypePage> codeTypePages = codeTypeMapper.queryCodeTypeList(codeTypePage);
        codeTypePages.forEach(data -> {
            String foidName = ipmApi.getUserName(token, data.getFoid());
            data.setFoidName(foidName);
            String loidName = ipmApi.getUserName(token, data.getLoid());
            data.setLoidName(loidName);
        });
        return (Page) codeTypePages;
    }

    public String queryContentByCodeAndType(String code, String type) {
        String content = "";
        CodeTypePage codeTypePage = new CodeTypePage();
        codeTypePage.setCode(code);
        codeTypePage.setType(type);
        List<CodeTypePage> codeTypePages = codeTypeMapper.queryCodeTypeByContent(codeTypePage);
        if (!codeTypePages.isEmpty()) {
            for (CodeTypePage data : codeTypePages) {
                content = data.getContent();
            }
        }
        return content;
    }
}
