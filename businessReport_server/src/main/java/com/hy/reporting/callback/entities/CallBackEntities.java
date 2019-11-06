package com.hy.reporting.callback.entities;

import com.hy.base.excel.ExcelAnnotation;
import com.hy.entity.PolicyState;
import com.hy.entity.SexType;
import com.hy.util.DateFormat;
import com.hy.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-09
 * user: lxg
 * package_name: com.hy.reporting.callback
 */
@Slf4j
public class CallBackEntities {

    /**
     * .
     * id
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 0, name = "唯一标识")
    private String id = UUID.randomUUID().toString();

    /**
     * .
     * 回访任务编号
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 1, name = "回访任务编号")
    private String callTask_id;

    /**
     * .
     * 推送任务编号
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 2, name = "推送任务编号")
    private String callTask_fisc_id;

    /**
     * .
     * 一级业务类型
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 3, name = "一级业务类型")
    private String callOutType1;

    /**
     * .
     * 二级业务类型
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 4, name = "二级业务类型")
    private String callOutType2;

    /**
     * .
     * 保单号
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 5, name = "保单号")
    private String policyNo;

    /**
     * .
     * 投保人姓名
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 6, name = "投保人姓名")
    private String applicantName;

    /**
     * .
     * 投保人性别
     */
    @Getter
    private String applicantSex;

    /**
     * .
     * 设置投保人性别
     *
     * @param sexType sex
     */
    public void setApplicantSex(final String sexType) {
        this.applicantSex = sexType;
        this.sex = SexType.getValue(sexType);
    }

    /**
     * .
     * 投保人性别
     */
    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    @ExcelAnnotation(id = 7, name = "投保人性别")
    private SexType sex;

    /**
     * .
     * 主约中文名
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 8, name = "主约中文名")
    private String riskName;

    /**
     * .
     * 保单生效日
     */
    @Getter
    @DateFormat(source = "yyyy-MM-dd", target = "yyyy年MM月dd日")
    @ExcelAnnotation(id = 9, name = "保单生效日")
    private String effectDt;

    /**
     * .
     * 设置保单生效日
     *
     * @param date 保单生效日字符串
     * @throws ParseException ParseException
     */
    public void setEffectDt(final String date)
        throws ParseException {
        this.effectDt = date;
        String source = "yyyy-MM-dd";
        String target = "yyyy年MM月份";
        this.policyAcquisitionDate = DateUtil.conversion(
            date,
            source,
            target)
            .replace("年0", "年");
    }

    /**
     * .
     * 下次扣款日
     */
    @Getter
    @Setter
    @DateFormat(source = "yyyy-MM-dd", target = "yyyy年MM月dd日")
    @ExcelAnnotation(id = 10, name = "下次扣款日")
    private String nextDeductDt;

    /**
     * .
     * 应缴日
     */
    @Getter
    @Setter
    @DateFormat(source = "yyyy-MM-dd", target = "yyyy年MM月dd日")
    @ExcelAnnotation(id = 11, name = "应缴日")
    private String payableDt;

    /**
     * .
     * 本次应缴保费
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 12, name = "本次应缴保费")
    private String renewalPayablePrem;

    /**
     * .
     * 最后扣款日
     */
    @Getter
    @Setter
    @DateFormat(source = "yyyy-MM-dd", target = "yyyy年MM月dd日")
    @ExcelAnnotation(id = 13, name = "最后扣款日")
    private String lastDeductDt;

    /**
     * .
     * 是否有账号
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 14, name = "是否有账号")
    private String deductAccount;

    /**
     * .
     * 扣款账号后四位
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 15, name = "扣款账号后四位")
    private String deductAccountCode;

    /**
     * .
     * 宽限期期满日
     */
    @Getter
    @Setter
    @DateFormat(source = "yyyy-MM-dd", target = "yyyy年MM月dd日")
    @ExcelAnnotation(id = 16, name = "宽限期期满日")
    private String gracePeriodExpiryDt;

    /**
     * .
     * 投保人生日
     */
    @Getter
    @Setter
    @DateFormat(source = "yyyy-MM-dd", target = "yyyy年MM月dd日")
    @ExcelAnnotation(id = 17, name = "投保人生日")
    private String applicantBirthday;
    
    /**
     * .
     * 投保人证件类型
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 18, name = "证件类型")
    private String applicantCdTp;

    /**
     * .
     * 投保人身份证号后四位
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 19, name = "证件号后四位")
    private String applicantCdNo;

    /**
     * .
     * 缴费年限
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 20, name = "缴费年限")
    private String payPeriond;

    /**
     * .
     * 扣款失败原因
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 21, name = "扣款失败原因")
    private String deductErrNote;

    /**
     * .
     * 扣款银行
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 22, name = "扣款银行")
    private String deductBankname;

    /**
     * .
     * 手机号码
     */
    @Getter
    private String[] phones;

    /**
     * .
     * set phones 字段值
     *
     * @param values 数组
     */
    public void setPhones(final String[] values) {
        this.phones = values;
        StringBuilder itemStr = new StringBuilder();
        for (String item : values) {
            if (itemStr.length() != 0) {
                itemStr.append(";");
            }
            itemStr.append(item);
        }
        this.allPhones = itemStr.toString();
        this.callCount = values.length * 2;
    }

    /**
     * .
     * 手机号码
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 23, name = "手机号码")
    private String allPhones;

    /**
     * .
     * 保单状态
     */
    @Getter
    @ExcelAnnotation(id = 24, name = "保单状态")
    private String policyState;

    /**
     * .
     * 设置状态
     *
     * @param state 状态
     */
    public void setPolicyState(final String state) {
        this.policyState = state;
        this.broadcastPolicyState = PolicyState.getValue(state);
    }

    /**
     * .
     * 投被保人是否同一人
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 25, name = "投被保人是否同一人")
    private String custSameFlag;

    /**
     * .
     * 失效情况
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 26, name = "失效情况")
    private String policyOverDueState;

    /**
     * .
     * 被保险人姓名
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 27, name = "被保险人姓名")
    private String insuredName;

    /**
     * .
     * 复效最后受理时间
     */
    @Getter
    @Setter
    @DateFormat(source = "yyyy-MM-dd", target = "yyyy年MM月dd日")
    @ExcelAnnotation(id = 28, name = "复效最后受理时间")
    private String lastComplexApplyDt;

    /**
     * .
     * 被保险人出生日期
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 29, name = "被保险人出生日期")
    private String insuredBirthday;

    /**
     * .
     * 实际保单状态
     */
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ExcelAnnotation(id = 30, name = "实际保单状态")
    private PolicyState broadcastPolicyState;

    /**
     * .
     * 保单购买日
     */
    @Getter
    @Setter
    @ExcelAnnotation(id = 31, name = "保单购买日")
    private String policyAcquisitionDate;

    /**
     * .
     * 导入时间
     */
    @Getter
    @Setter
    private Timestamp importDate = new Timestamp(new Date().getTime());

    /**
     * .
     * 名单分发时间
     */
    @Getter
    @Setter
    private Timestamp distributeDate;


    /**
     * .
     * 名单分发标记
     * 0-未分发 1-已分发 2-重复保单不分发
     */
    @Getter
    @Setter
    private int distributeFlag = 0;


    /**
     * .
     * 名单截止时间
     */
    @Getter
    @Setter
    private Timestamp closeDate;


    /**
     * .
     * 名单截止标记
     * 0-未截止 1-成功 2-失败
     */
    @Getter
    @Setter
    private int closeFlag = 0;

    /**
     * .
     * 应拨打次数
     */
    @Getter
    @ExcelAnnotation(id = 32, name = "拨打次数")
    private int callCount;

    /**
     * .
     * 应拨打次数
     */
    @Setter
    @Getter
    @ExcelAnnotation(id = 33, name = "年份")
    private String year;
}
