package com.hy.iom.dao;

import com.hy.iom.entities.SceneThroughDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * .
 * Created by of liaoxg
 * date: 2018-12-10
 * user: lxg
 * package_name: com.hy.iom.dao
 */
@Repository
public class SceneThroughDetailDao {
    private final JdbcTemplate template;

    @Autowired
    public SceneThroughDetailDao(JdbcTemplate template) {
        this.template = template;
    }

    public int[] insertThroughDetailList(List<SceneThroughDetail> throughDetails) {
        String sql_callContentDetails = "INSERT INTO T_IOM_SCENETHROUGHDETAIL_TMP(ID,UUID,NODENAME,SEQ,THROUGHFLAG) "
            + "values(?,?,?,?,?)";
        return template.batchUpdate(sql_callContentDetails, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                SceneThroughDetail throughDetail = throughDetails.get(i);
                ps.setString(1, UUID.randomUUID().toString());
                ps.setString(2, throughDetail.getUuid());
                ps.setString(3, throughDetail.getNodeName());
                ps.setInt(4, throughDetail.getSeq());
                ps.setString(5, throughDetail.getThroughFlag().toString());
            }

            @Override
            public int getBatchSize() {
                return throughDetails.size();
            }
        });
    }
}
