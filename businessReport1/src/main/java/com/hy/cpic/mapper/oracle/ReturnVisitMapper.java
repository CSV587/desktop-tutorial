package com.hy.cpic.mapper.oracle;

import com.hy.cpic.entities.ReturnVisitMetaInfo;
import com.hy.cpic.mapper.IomMapper;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.BranchCallNumConfigPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnVisitMapper extends IomMapper<ReturnVisitMetaInfo> {

    void save(ReturnVisitMetaInfo returnVisitMetaInfo);

    List<ReturnVisitMetaInfo> sort(@Param("callNumConfigId") String callNumConfigId,
                                   @Param("branchName") String branchName,
                                   @Param("callMaxNum") int callMaxNum);

    List<ReturnVisitMetaInfo> sortPolling(@Param("callNumConfigId") String callNumConfigId,
                                          @Param("callMaxNum") int callMaxNum);

    void copyToHistory(@Param("id") String id);

    void deleteById(@Param("id") String id);

    void moveRepeatCallNumber(@Param("list") List<String> list, @Param("callNumConfigId") String callNumConfigId);

    int clearRepeatCallNumber(@Param("list") List<String> list, @Param("callNumConfigId") String callNumConfigId);

    List<String> getRepeatCallNumber(@Param("repeatNum") int repeatNum, @Param("callNumConfigId") String callNumConfigId);

    List<ReturnVisitMetaInfo> queryBranchNameByCallNumConfigId(@Param("callNumConfigId") String callNumConfigId);

    int getBranchCount(@Param("callNumConfigId") String callNumConfigId,
                       @Param("branchName") String branchName,
                       @Param("callMaxNum") int callMaxNum);

    int getAgentIdCount(@Param("callNumConfigId") String callNumConfigId,
                        @Param("agentId") String agentId,
                        @Param("callMaxNum") int callMaxNum);

    List<BranchCallNumConfigPage> queryBranchInfoByCallNumConfigId(@Param("callNumConfigId") String callNumConfigId);

    int queryTotalNum(@Param("callNumConfigId") String callNumConfigId);
}
