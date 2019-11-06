package com.hy.mapper.oracle;

import com.hy.reporting.callcyclemanage.entities.CallCycleEntity;
import com.hy.reporting.callcyclemanage.page.CallCyclePage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/4.
 * @Description :
 */
public interface CallCycleMapper {

    List<Map<String, String>> queryPushTask();

    List<Map<String, String>> queryReturnTask();

    List<CallCyclePage> queryList(CallCyclePage callCyclePage);

    CallCyclePage queryById(@Param("pushTaskId") String pushTaskId);

    void save(CallCyclePage callCyclePage);

    void terminate(@Param("pushTaskId") String pushTaskId);

    void updateEditorAndDate(@Param("pushTaskId") String pushTaskId, @Param("editorName") String editorName, @Param("date") String date);

    int invalidCallCycle(@Param("id") String id);

}
