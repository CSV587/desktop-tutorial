package com.hy.entity.paper;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-17
 * user: lxg
 * package_name: com.hy.entity.paper
 */
@Setter
@Getter
@XStreamAlias("save")
public class Save {
    /**
     * .
     * 保单号
     */
    private String contNo;
    /**
     * .
     * 问卷Id
     */
    private String questionnaireID;
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
