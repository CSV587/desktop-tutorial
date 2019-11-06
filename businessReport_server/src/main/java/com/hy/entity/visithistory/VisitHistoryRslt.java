package com.hy.entity.visithistory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-17
 * user: lxg
 * package_name: com.hy.entity.visithistory
 */
@Setter
@Getter
@XStreamAlias("visitHistoryRslt")
public class VisitHistoryRslt {
    /**
     * .
     * 保单号
     */
    private String contNo;
    /**
     * .
     * 序号
     */
    private String visitHistorySeqno;
    /**
     * .
     * 保存状态
     * 0-失败;1-成功
     */
    private String status;
    /**
     * .
     * 备注
     */
    private String notes;
}
