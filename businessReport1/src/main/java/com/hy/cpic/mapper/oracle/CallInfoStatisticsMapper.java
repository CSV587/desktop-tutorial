package com.hy.cpic.mapper.oracle;

import com.hy.cpic.entities.CallInfoStatistics;
import com.hy.cpic.mapper.IomMapper;
import com.hy.cpic.reporting.callbacklist.page.CallBackListPage;
import com.hy.cpic.reporting.callbackrate.page.CallBackRatePage;
import com.hy.cpic.reporting.calldetail.page.CallDetailPage;
import com.hy.cpic.reporting.ccmonitor.page.CCMonitorPage;
import com.hy.cpic.reporting.intentionStatistics.page.IntentionStatisticsPage;
import com.hy.cpic.reporting.intentionlist.page.IntentionListPage;
import com.hy.cpic.reporting.intentionrate.page.IntentionRatePage;
import com.hy.cpic.reporting.marketlist.page.MarketListPage;
import com.hy.cpic.reporting.marketrate.page.MarketRatePage;
import com.hy.cpic.reporting.tasklist.page.TaskListPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CallInfoStatisticsMapper extends IomMapper<CallInfoStatistics> {

    void save(CallInfoStatistics callInfoStatistics);

    List<CallDetailPage> query(CallDetailPage callDetailPage);

    List<TaskListPage> queryTask(TaskListPage taskListPage);

    List<CCMonitorPage> queryMonitor(CCMonitorPage page);

    List<CallBackRatePage> queryCallBackRate(CallBackRatePage page);

    List<CallBackListPage> queryCallBackList(CallBackListPage page);

    List<IntentionRatePage> queryIntentionRate(IntentionRatePage page);

    List<IntentionListPage> queryIntentionList(IntentionListPage page);

    List<Map<String, Object>> queryCallBackListMap(CallBackListPage page);

    List<Map<String, Object>> queryIntentionListMap(IntentionListPage page);

    List<MarketRatePage> queryMarketRate(MarketRatePage page);

    long queryMarketRateCount(MarketRatePage page);

    List<IntentionStatisticsPage> queryIntentionStatistics(IntentionStatisticsPage intentionStatisticsPage);

    int updateCallInfo(CallInfoStatistics callInfoStatistic);

    List<MarketListPage> queryMarketList(MarketListPage marketListPage);

    int queryCallCount(@Param("callNumber") String callNumber);

    long queryMarketListCount(MarketListPage marketListPage);
}
