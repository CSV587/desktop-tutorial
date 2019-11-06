package com.hy.iom.dao;

import com.hy.iom.entities.RecordInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Repository
public class StatisticsDao {
    @Autowired
    private JdbcTemplate template;

    public int[] insertRecordInfoList(List<RecordInfo> ls_recordInfo) throws SQLException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql_recordInfo = "INSERT INTO T_IOM_RECORDINFO_TMP(ID,UUID,RECORDENDTIME,CHANNELENDTIME,CHANNELSTARTTIME,RECORDSTARTTIME,CALLNUMBER,RECORDPATH" +
                ",ONSTATE,PROJECTNAME,FLOWNAME,RULENAME,CALLCOUNT) " +
                "values(SEQ_IOM_RECORDINFO_TMP_ID.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?)";
        return template.batchUpdate(sql_recordInfo, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                RecordInfo recordInfo = ls_recordInfo.get(i);
                ps.setString(1, recordInfo.getUuid());

                try {
                    ps.setTimestamp(2, new Timestamp(simpleDateFormat.parse(recordInfo.getRecordEndTime()).getTime()));
                    ps.setTimestamp(3, new Timestamp(simpleDateFormat.parse(recordInfo.getChannelEndTime()).getTime()));
                    ps.setTimestamp(4, new Timestamp(simpleDateFormat.parse(recordInfo.getChannelStartTime()).getTime()));
                    ps.setTimestamp(5, new Timestamp(simpleDateFormat.parse(recordInfo.getRecordStartTime()).getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ps.setString(6, recordInfo.getCallNumber());
                ps.setString(7, recordInfo.getRecordPath());
                ps.setString(8, recordInfo.getOnState());
                ps.setString(9, recordInfo.getProjectId());
                ps.setString(10, recordInfo.getFlowId());
                ps.setString(11, recordInfo.getRuleId());
                ps.setInt(12, recordInfo.getCallCount());
            }

            @Override
            public int getBatchSize() {
                return ls_recordInfo.size();
            }
        });
    }


}
