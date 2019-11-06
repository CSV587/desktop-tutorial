package com.hy.iom.dao;

import com.hy.iom.entities.CustomerInfo;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class CustomerInfoDao {
    private final JdbcTemplate template;

    public CustomerInfoDao(JdbcTemplate template) {
        this.template = template;
    }

    public int[] insertCustomerInfoList(List<CustomerInfo> ls_customerInfo) {
        String sql_customerInfo = "INSERT INTO T_IOM_CUSTOMERINFO_TMP(ID,UUID,NAME,VALUE) "
            + "values(?,?,?,?)";
        return template.batchUpdate(sql_customerInfo, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CustomerInfo customerInfo = ls_customerInfo.get(i);
                ps.setString(1, UUID.randomUUID().toString());
                ps.setString(2, customerInfo.getUuid());
                ps.setString(3, customerInfo.getName());
                ps.setString(4, customerInfo.getValue());
            }

            @Override
            public int getBatchSize() {
                return ls_customerInfo.size();
            }
        });
    }
}
