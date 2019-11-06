package com.hy.reporting.callcyclemanage.dao;

import com.hy.reporting.callcyclemanage.entities.CallCycleEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2019/10/31
 * user: lxg
 * package_name: com.hy.reporting.callcyclemanage.dao
 */
@Slf4j
@Repository
public class CallCycleDao {
    private final JdbcTemplate template;

    public CallCycleDao(JdbcTemplate template) {
        this.template = template;
    }

    public int[] insertCallCycleList(List<CallCycleEntity> ls_recordInfo) {
        String sql_recordInfo = "INSERT INTO T_BUSINESS_CALLCYCLEMANAGE(ID,PUSHTASKID,RETURNTASKID,TASKTOTAL,TASKSTATE," +
            "            STARTDATE,ENDDATE,NEWSTARTDATE,NEWENDDATE,EDITOR,EDITDATE,CALLSTATE) values" +
            "            ( ?, ?, ?, ?, ?," +
            "            ?, ?, ?," +
            "            ?, ?," +
            "            ?, ?)";
        long startTime = System.currentTimeMillis();
        BatchPreparedStatementSetter batchPreparedStatementSetter = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CallCycleEntity callCycleEntity = ls_recordInfo.get(i);
                ps.setString(1, callCycleEntity.getId());
                ps.setString(2, callCycleEntity.getPushTaskId());
                ps.setString(3, callCycleEntity.getReturnTaskId());
                ps.setLong(4, callCycleEntity.getTaskTotal());
                ps.setInt(5, callCycleEntity.getTaskState());
                ps.setTimestamp(6, callCycleEntity.getStartDate());
                ps.setTimestamp(7, callCycleEntity.getEndDate());
                ps.setTimestamp(8, callCycleEntity.getNewStartDate());
                ps.setTimestamp(9, callCycleEntity.getNewEndDate());
                ps.setString(10, callCycleEntity.getEditor());
                ps.setTimestamp(11, callCycleEntity.getEndDate());
                ps.setInt(12, callCycleEntity.getCallState());
            }

            @Override
            public int getBatchSize() {
                return ls_recordInfo.size();
            }
        };
        long endTime = System.currentTimeMillis();
        log.debug("批处理数据生成时间 CallCycle time [{}],size [{}]",
            endTime - startTime,
            batchPreparedStatementSetter.getBatchSize());
        return template.batchUpdate(sql_recordInfo, batchPreparedStatementSetter);
    }
}
