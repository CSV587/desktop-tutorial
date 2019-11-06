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
@XStreamAlias("visitHistoryList")
public class VisitHistorys {
    /**
     * .
     * 回访历史列表
     */
    @XStreamImplicit
    private List<VisitHistory> visitHistoryList;
}
