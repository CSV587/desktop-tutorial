package com.hy.iom.dao;

import com.hy.iom.entities.ResInfo;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class ResInfoDao {

    private final JdbcTemplate template;

    public ResInfoDao(JdbcTemplate template) {
        this.template = template;
    }

    public int[] insertResInfoList(List<ResInfo> ls_resInfo) {
        String sql_resInfo = "INSERT INTO T_IOM_RESINFO_TMP(ID,UUID,NAME,VALUE) "
            + "values(?,?,?,?)";
        return template.batchUpdate(sql_resInfo, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ResInfo resInfo = ls_resInfo.get(i);
                ps.setString(1, UUID.randomUUID().toString());
                ps.setString(2, resInfo.getUuid());
                ps.setString(3, resInfo.getName());
                ps.setString(4, resInfo.getValue());
            }

            @Override
            public int getBatchSize() {
                return ls_resInfo.size();
            }
        });
    }
}
