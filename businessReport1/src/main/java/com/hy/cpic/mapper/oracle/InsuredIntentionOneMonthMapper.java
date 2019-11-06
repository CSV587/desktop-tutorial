package com.hy.cpic.mapper.oracle;

import com.hy.cpic.entities.InsuredIntentionOneMonth;
import com.hy.cpic.mapper.IomMapper;
import com.hy.cpic.reporting.callnumconfig.insuredIntention.page.InsuredIntentionMonthInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuredIntentionOneMonthMapper extends IomMapper<InsuredIntentionOneMonth> {

    void save(InsuredIntentionOneMonth insuredIntentionOneMonth);

    int edit(InsuredIntentionOneMonth insuredIntentionOneMonth);

    int updateTimes(InsuredIntentionOneMonth insuredIntentionOneMonth);

    List<InsuredIntentionMonthInfo> calledTimesByMonth(@Param("callNumberList") List<String> callNumberList);

    int batchClearNotInMonth();

    List<InsuredIntentionOneMonth> detailByCallNumber(@Param("callNumberList") List<String> callNumberList);

}
