package com.hy.cpic.mapper.oracle;

import com.hy.cpic.entities.InsuredIntentionMetaInfo;
import com.hy.cpic.mapper.IomMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zxd
 * @since 2019/1/4.
 */
@Repository
public interface InsuredIntentionMetaInfoMapper extends IomMapper<InsuredIntentionMetaInfo> {
    void save(InsuredIntentionMetaInfo insuredIntentionMetaInfo);

    List<InsuredIntentionMetaInfo> sort(@Param("callNumConfigId") String callNumConfigId,
                                        @Param("branchName") String branchName,
                                        @Param("limit") Integer limit,
                                        @Param("businessTypes") List<String> businessTypes);

    void copyToHistory(@Param("id") String id);

    void deleteById(@Param("id") String id);

    void moveRepeatCallNumber(@Param("list") List<String> list, @Param("callNumConfigId") String callNumConfigId);

    int clearRepeatCallNumber(@Param("list") List<String> list, @Param("callNumConfigId") String callNumConfigId);

    List<String> getRepeatCallNumber(@Param("repeatNum") int repeatNum,
                                     @Param("callNumConfigId") String callNumConfigId);

    List getActivityNameList();

    void moveActivityName(@Param("activityName") String activityName);

    int deleteByActivityName(@Param("activityName") String activityName);
}
