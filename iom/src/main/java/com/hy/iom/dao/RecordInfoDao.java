package com.hy.iom.dao;

import com.hy.iom.entities.RecordInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Repository
public class RecordInfoDao {
    private final JdbcTemplate template;

    public RecordInfoDao(JdbcTemplate template) {
        this.template = template;
    }

    public int[] insertRecordInfoList(List<RecordInfo> ls_recordInfo) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql_recordInfo = "INSERT INTO T_IOM_RECORDINFO_TMP(ID,UUID,RECORDENDTIME,CHANNELENDTIME,CHANNELSTARTTIME,RECORDSTARTTIME,CALLNUMBER,RECORDPATH"
            + ",ONSTATE,PROJECTID,FLOWID,RULEID,CALLCOUNT,DURATION,TURNCOUNT,ENDNODENAME,CALLSTATE,FINALNODENAME,TRADESTATE,CALLRESULT,TASKID,BLACKLISTFLAG) "
            + "values(? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return template.batchUpdate(sql_recordInfo, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                RecordInfo recordInfo = ls_recordInfo.get(i);
                ps.setString(1, UUID.randomUUID().toString());
                ps.setString(2, recordInfo.getUuid());
                long duration = 0L;
                try {
                    String recordStartTime = StringUtils.isNotBlank(recordInfo.getRecordStartTime()) ? recordInfo.getRecordStartTime() : recordInfo.getChannelStartTime();
                    String recordEndTime = StringUtils.isNotBlank(recordInfo.getRecordEndTime()) ? recordInfo.getRecordEndTime() : recordInfo.getChannelEndTime();
                    ps.setTimestamp(3, new Timestamp(simpleDateFormat.parse(recordEndTime).getTime()));
                    ps.setTimestamp(4, new Timestamp(simpleDateFormat.parse(recordInfo.getChannelEndTime()).getTime()));
                    ps.setTimestamp(5, new Timestamp(simpleDateFormat.parse(recordInfo.getChannelStartTime()).getTime()));
                    ps.setTimestamp(6, new Timestamp(simpleDateFormat.parse(recordStartTime).getTime()));
                    if ("connect".equals(recordInfo.getOnState())) {
                        duration = (simpleDateFormat.parse(recordEndTime).getTime() - simpleDateFormat.parse(recordStartTime).getTime()) / 1000;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ps.setString(7, recordInfo.getCallNumber());
                ps.setString(8, recordInfo.getRecordPath());
                ps.setString(9, recordInfo.getOnState());
                ps.setString(10, recordInfo.getProjectId());
                ps.setString(11, recordInfo.getFlowId());
                ps.setString(12, recordInfo.getRuleId());
                ps.setInt(13, recordInfo.getCallCount());
                ps.setInt(14, (int) duration);
                ps.setInt(15, recordInfo.getTurnCount());
                ps.setString(16, recordInfo.getEndNodeName());
                ps.setString(17, recordInfo.getCallState());
                ps.setString(18, StringUtils.isNotBlank(recordInfo.getCallResult()) ? recordInfo.getCallResult() : recordInfo.getEndNodeName());
                ps.setString(19, recordInfo.getTradeState());
                ps.setString(20, recordInfo.getCallResult());
                ps.setString(21, recordInfo.getTaskId());
                ps.setString(22, recordInfo.getBlackListFlag());
            }

            @Override
            public int getBatchSize() {
                return ls_recordInfo.size();
            }
        });
    }


}
