package com.hy.entity.visithistory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-17
 * user: lxg
 * package_name: com.hy.entity.visithistory
 */
@Setter
@Getter
@XStreamAlias("visitHistoryRsltList")
public class VisitHistoryRslts {
    /**
     * .
     * 回访历史保存结果列表
     */
    @XStreamImplicit
    private List<VisitHistoryRslt> visitHistoryRsltList;
}
