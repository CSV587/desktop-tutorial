package com.hy.cpic.mapper.oracle;

import com.hy.cpic.entities.CallInfoStatistics;
import com.hy.cpic.mapper.IomMapper;
import com.hy.cpic.reporting.extractData.insuredIntention.page.InsuredIntentionDataPage;
import com.hy.cpic.reporting.insuredIntentionRecord.page.InsuredIntentionRecordPage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuredIntentionDataMapper extends IomMapper<CallInfoStatistics> {

    List<InsuredIntentionDataPage> queryInsuredIntentionData(InsuredIntentionDataPage insuredIntentionDataPage);

    List<InsuredIntentionRecordPage> queryInsuredIntentionRecord(InsuredIntentionRecordPage insuredIntentionRecordPage);
}
