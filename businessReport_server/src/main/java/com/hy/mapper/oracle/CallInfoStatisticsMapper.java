package com.hy.mapper.oracle;

import com.hy.entities.CallInfoStatistics;
import com.hy.entity.record.RecordEntity;
import com.hy.mapper.BaseMapper;
import com.hy.reporting.callback.page.CallBackPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * .
 * 数据库操作类
 */
public interface CallInfoStatisticsMapper
    extends BaseMapper<CallInfoStatistics> {

    /**
     * .
     * 保存数据
     *
     * @param callInfoStatistics CallInfoStatistics列表
     */
    void save(List<CallInfoStatistics> callInfoStatistics);

    /**
     * .
     * 获取回访明细数据
     *
     * @param callBackPage CallBackPage
     * @return 明细数据
     */
    List<CallBackPage> queryCallBack(CallBackPage callBackPage);

    /**
     * .
     * 查询所有可回写录音文件
     *
     * @return 录音实体集合
     */
    List<RecordEntity> queryAllCallBackRecord();

    /**
     * .
     * 更新回访记录已回传状态
     *
     * @param items 保单号集合
     * @return 受影响数
     */
    int updateBatchCallBackPaper(@Param("itemIds") List<String> items);

    /**
     * .
     * 查询所有待回传数据
     *
     * @return CallInfoStatistics集合
     */
    List<CallInfoStatistics> queryAllCallBack();

    /**
     * .
     * 更新回访记录已回写录音状态
     *
     * @param items id 集合
     * @return 受影响数
     */
    int updateBatchCallBackRecord(@Param("itemIds") List<String> items);

    /**
     * .
     * 回写录音文件无效
     *
     * @param id id
     * @return 受影响数
     */
    int updateInValidCallBackRecord(@Param("id") String id);

    /**
     * .
     * 处理回访业务表中不存在的 数据记录
     *
     * @param callInfoStatistics CallInfoStatistics
     */
    void invalidRecordList(CallInfoStatistics callInfoStatistics);

    /**
     * .
     * 统计单条记录打通次数
     *
     * @param recordId 记录Id
     * @return 次数
     */
    int maxCountByRecordId(@Param("recordId") String recordId);
}
