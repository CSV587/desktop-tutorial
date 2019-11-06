package com.hy.reporting.callback.dao;

import com.hy.reporting.callback.entities.CallBackEntities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2019/10/31
 * user: lxg
 * package_name: com.hy.reporting.callback.dao
 */
@Slf4j
@Repository
public class CallBackDao {
    private final JdbcTemplate template;

    public CallBackDao(JdbcTemplate template) {
        this.template = template;
    }

    public int[] insertRecordInfoList(List<CallBackEntities> ls_recordInfo) {
        String sql_recordInfo = "INSERT INTO T_BUSINESS_CALLBACK(ID,CALLTASKID," +
            "            CALLTASKFISCID,CALLOUTTYPE1,CALLOUTTYPE2,POLICYNO,APPLICANTNAME," +
            "            APPLICANTSEX,RISKNAME,EFFECTDT," +
            "            NEXTDEDUCTDT,PAYABLEDT,RENEWALPAYABLEPREM,LASTDEDUCTDT,DEDUCTACCOUNT," +
            "            DEDUCTACCOUNTCODE," +
            "            GRACEPERIODEXPIRYDT,APPLICANTBIRTHDAY,APPLICANTCDTP,APPLICANTCDNO," +
            "            PAYPERIOND,DEDUCTERRNOTE,DEDUCTBANKNAME,PHONES,POLICYSTATE,CUSTSAMEFLAG," +
            "POLICYOVERDUESTATE,INSUREDNAME," +
            "            LASTCOMPLEXAPPLYDT,INSUREDBIRTHDAY," +
            "            BROADCASTPOLICYSTATE,POLICYACQUISITIONDATE,IMPORTDATE,DISTRIBUTEDATE,DISTRIBUTEFLAG," +
            "            CLOSEDATE,CLOSEFLAG,CALLCOUNT,YEAR,CREATETIME) "
            + "values(? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
            " ?, ?, ?, ? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        long startTime = System.currentTimeMillis();
        BatchPreparedStatementSetter batchPreparedStatementSetter = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CallBackEntities callBackEntities = ls_recordInfo.get(i);
                ps.setString(1, callBackEntities.getId());
                ps.setString(2, callBackEntities.getCallTask_id());
                ps.setString(3, callBackEntities.getCallTask_fisc_id());
                ps.setString(4, callBackEntities.getCallOutType1());
                ps.setString(5, callBackEntities.getCallOutType2());
                ps.setString(6, callBackEntities.getPolicyNo());
                ps.setString(7, callBackEntities.getApplicantName());
                ps.setString(8, callBackEntities.getApplicantSex());
                ps.setString(9, callBackEntities.getRiskName());
                ps.setString(10, callBackEntities.getEffectDt());
                ps.setString(11, callBackEntities.getNextDeductDt());
                ps.setString(12, callBackEntities.getPayableDt());
                ps.setString(13, callBackEntities.getRenewalPayablePrem());
                ps.setString(14, callBackEntities.getLastDeductDt());
                ps.setString(15, callBackEntities.getDeductAccount());
                ps.setString(16, callBackEntities.getDeductAccountCode());
                ps.setString(17, callBackEntities.getGracePeriodExpiryDt());
                ps.setString(18, callBackEntities.getApplicantBirthday());
                ps.setString(19, callBackEntities.getApplicantCdTp());
                ps.setString(20, callBackEntities.getApplicantCdNo());
                ps.setString(21, callBackEntities.getPayPeriond());
                ps.setString(22, callBackEntities.getDeductErrNote());
                ps.setString(23, callBackEntities.getDeductBankname());
                ps.setString(24, callBackEntities.getAllPhones());
                ps.setString(25, callBackEntities.getPolicyState());
                ps.setString(26, callBackEntities.getCustSameFlag());
                ps.setString(27, callBackEntities.getPolicyOverDueState());
                ps.setString(28, callBackEntities.getInsuredName());
                ps.setString(29, callBackEntities.getLastComplexApplyDt());
                ps.setString(30, callBackEntities.getInsuredBirthday());
                if (callBackEntities.getBroadcastPolicyState() != null) {
                    ps.setString(31, callBackEntities.getBroadcastPolicyState().toString());
                } else {
                    ps.setString(31, "");
                }
                ps.setString(32, callBackEntities.getPolicyAcquisitionDate());
                ps.setTimestamp(33, callBackEntities.getImportDate());
                ps.setTimestamp(34, callBackEntities.getDistributeDate());
                ps.setInt(35, callBackEntities.getDistributeFlag());
                ps.setTimestamp(36, callBackEntities.getCloseDate());
                ps.setInt(37, callBackEntities.getCloseFlag());
                ps.setInt(38, callBackEntities.getCallCount());
                ps.setString(39, callBackEntities.getYear());
                ps.setTimestamp(40, new Timestamp(System.currentTimeMillis()));
            }

            @Override
            public int getBatchSize() {
                return ls_recordInfo.size();
            }
        };
        long endTime = System.currentTimeMillis();
        log.debug("批处理数据生成时间 CallBackEntities time [{}],size [{}]",
            endTime - startTime,
            batchPreparedStatementSetter.getBatchSize());
        return template.batchUpdate(sql_recordInfo, batchPreparedStatementSetter);
    }
}
