package com.hy.iom.dao;

import com.hy.iom.entities.CallContentDetail;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class CallContentDetailDao {

    private final JdbcTemplate template;

    public CallContentDetailDao(JdbcTemplate template) {
        this.template = template;
    }

    public int[] insertCallContentDetailList(List<CallContentDetail> callContentDetails) {
        String sql_callContentDetails = "INSERT INTO T_IOM_CALLCONTENTDETAIL_TMP(ID,UUID,CALLCONTENT,CUSTOMINFO) "
            + "values(?,?,?,?)";
        return template.batchUpdate(sql_callContentDetails, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CallContentDetail callContentDetail = callContentDetails.get(i);
                ps.setString(1, UUID.randomUUID().toString());
                ps.setString(2, callContentDetail.getUuid());
                ps.setString(3, callContentDetail.getCallContent());
                ps.setString(4, callContentDetail.getCustomInfo());
            }

            @Override
            public int getBatchSize() {
                return callContentDetails.size();
            }
        });
    }
}
