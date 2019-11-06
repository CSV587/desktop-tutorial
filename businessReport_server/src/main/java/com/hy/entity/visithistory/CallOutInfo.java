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
@XStreamAlias("callOutInfo")
public class CallOutInfo {
    /**
     * .
     * 外拨号码
     */
    private String callOutNo;
    /**
     * .
     * 关联录音Id
     */
    private String recordId;
}
