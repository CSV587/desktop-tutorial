package com.hy.cpic.mapper.oracle;

import com.hy.cpic.entities.BranchCallNumConfig;
import com.hy.cpic.entities.CallNumConfig;
import com.hy.cpic.mapper.IomMapper;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.BranchCallNumConfigPage;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.CallNumConfigPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallNumConfigMapper extends IomMapper<CallNumConfig> {

    void save(CallNumConfig callNumConfig);

    int edit(CallNumConfig callNumConfig);

    int deleteConfig(CallNumConfig callNumConfig);

    List<CallNumConfigPage> queryCallNumConfig(CallNumConfigPage callNumConfigPage);

    int queryCallMaxNum(BranchCallNumConfig branchCallNumConfig);

    List<CallNumConfigPage> queryCallNumConfigByName(CallNumConfigPage callNumConfigPage);

    List<BranchCallNumConfigPage> queryBranchCallNumConfigById(BranchCallNumConfig branchCallNumConfig);

    List<CallNumConfigPage> queryCallNumConfigList();

    CallNumConfigPage queryValidCallNumConfig(@Param("name") String name);

    CallNumConfigPage queryValidCallNumConfigLock(@Param("name") String name);

    void increaseNum(@Param(value = "id") String id, @Param(value = "totalNum") Integer totalNum);

    int queryTotalNum(@Param("callNumConfigId") String callNumConfigId);
}
