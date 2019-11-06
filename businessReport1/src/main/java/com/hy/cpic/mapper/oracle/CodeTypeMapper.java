package com.hy.cpic.mapper.oracle;

import com.hy.cpic.entities.CodeType;
import com.hy.cpic.mapper.IomMapper;
import com.hy.cpic.reporting.codetype.page.CodeTypePage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeTypeMapper extends IomMapper<CodeType> {

    void save(CodeType codeType);

    int edit(CodeType codeType);

    int deleteCodeType(CodeType codeType);

    List<CodeTypePage> queryCodeTypeList(CodeTypePage codeTypePage);

    List<CodeTypePage> queryCodeTypeById(@Param("id") String id);

    List<CodeTypePage> queryCodeTypeByContent(CodeTypePage codeTypePage);
}
