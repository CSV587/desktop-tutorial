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
@XStreamAlias("callOutResult")
public class CallOutResult {
    /**
     * .
     * 呼叫结果Id
     * 1073(智能外呼-继续回访)
     * 1072(智能外呼-回访成功)
     * 1071(智能外呼-转人工)
     */
    private String callResultId;
    /**
     * .
     * 呼叫结果
     * 智能外呼-继续回访(1073)
     * 智能外呼-回访成功(1072)
     * 智能外呼-转人工(1071)
     */
    private String calloutRslt;
}
