package com.hy.cpic.mapper.oracle;

import com.hy.cpic.entities.BranchCallNumConfig;
import com.hy.cpic.mapper.IomMapper;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.BranchCallNumConfigPage;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.CallNumConfigPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BranchCallNumConfigMapper extends IomMapper<BranchCallNumConfig> {

    void save(BranchCallNumConfig branchCallNumConfig);

    int edit(BranchCallNumConfig branchCallNumConfig);

    int editByBranchName(BranchCallNumConfig branchCallNumConfig);

    int deleteConfig(BranchCallNumConfig branchCallNumConfig);

    List<BranchCallNumConfigPage> queryById(BranchCallNumConfigPage branchCallNumConfigPage);

    List<BranchCallNumConfigPage> queryBranchCallNumConfig(BranchCallNumConfigPage branchCallNumConfigPage);

    List<BranchCallNumConfigPage> queryBranchCallNumConfigByName(BranchCallNumConfigPage branchCallNumConfigPage);

    List<BranchCallNumConfigPage> queryBranchCallNumConfigByNameAndId(BranchCallNumConfigPage branchCallNumConfigPage);

    List<BranchCallNumConfigPage> queryByCallNumConfigId(@Param("callNumConfigId") String callNumConfigId);

    List<CallNumConfigPage> queryCallNumConfigById(BranchCallNumConfigPage branchCallNumConfigPage);

    List<BranchCallNumConfigPage> querySurplusNum(@Param("callNumConfigId") String callNumConfigId, @Param("businessTypeList") List<String> businessTypeList);
}
