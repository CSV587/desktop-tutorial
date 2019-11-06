package com.hy.iom.dao;

import com.hy.iom.entities.CallContent;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class CallContentDao {
    private final JdbcTemplate template;

    public CallContentDao(JdbcTemplate template) {
        this.template = template;
    }

    public int[] insertCallContentList(List<CallContent> ls_callContent) {
        String sql_callContent = "INSERT INTO T_IOM_CALLCONTENT_TMP(ID,UUID,NODENAME,TYPE,SEQ,CONTENT,INTERVALSECONDS) "
            + "values(?,?,?,?,?,?,?)";
        return template.batchUpdate(sql_callContent, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CallContent callContent = ls_callContent.get(i);
                ps.setString(1, UUID.randomUUID().toString());
                ps.setString(2, callContent.getUuid());
                ps.setString(3, callContent.getNodeName());
                ps.setString(4, callContent.getType());
                ps.setInt(5, callContent.getSeq());
                ps.setString(6, callContent.getContent());
                ps.setInt(7, callContent.getIntervalSeconds());
            }
            @Override
            public int getBatchSize() {
                return ls_callContent.size();
            }
        });
    }
}
