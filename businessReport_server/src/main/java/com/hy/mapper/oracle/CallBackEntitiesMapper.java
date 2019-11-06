package com.hy.mapper.oracle;

import com.hy.entities.CallInfoStatistics;
import com.hy.entity.visithistory.VisitHistory;
import com.hy.mapper.BaseMapper;
import com.hy.reporting.callback.entities.CallBackEntities;
import com.hy.reporting.callcyclemanage.page.CallCyclePage;
import com.hy.reporting.reconciliation.page.ReconciliationPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-18
 * user: lxg
 * package_name: com.hy.mapper.oracle
 */
public interface CallBackEntitiesMapper
    extends BaseMapper<CallInfoStatistics> {

    /**
     * .
     * 查询所有可拨打数据
     *
     * @return 可拨打数据集合
     */
    List<CallBackEntities> queryAllPendingData();

    /**
     * .
     * 处理无效保单
     *
     * @return 返回无效记录个数
     */
    int dealInvalidData();

    /**
     * .
     * 将数据置为可分发的状态
     *
     * @param itemIds id集合
     * @return 受影响数
     */
    int distributeData(@Param("itemIds") List<String> itemIds);

    /**
     * .
     * 根据保单号查询回访业务数据
     *
     * @param id 唯一标识
     * @return 回访类
     */
    VisitHistory findByVisitValue(String id);

    /**
     * .
     * 关闭名单
     *
     * @param id   唯一标识
     * @param flag 标志
     */
    void closePolicyNo(String id, String flag);

    /**
     * .
     * 获取所有无效数据
     *
     * @param page 查询条件
     * @return 结束
     */
    List<CallBackEntities> queryInvalidData(CallCyclePage page);

    /**
     * .
     * 获取对账数据
     *
     * @param page ReconciliationPage
     * @return 任务对账集合
     */
    List<ReconciliationPage> queryReconciliation(ReconciliationPage page);

    /**
     * .
     * 终止数据
     *
     * @param pushTaskId 无效任务编号
     */
    void terminate(@Param("pushTaskId") String pushTaskId);

    /**
     * .
     * 根据Id终止数据
     *
     * @param id 无效主键
     */
    void terminateById(@Param("id") String id);

    /**
     * .
     * 根据主键返回实体
     *
     * @param id 主键
     * @return 实体类
     */
    CallBackEntities findById(@Param("id") String id);
}
