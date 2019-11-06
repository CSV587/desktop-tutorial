package com.hy.entity.paper;

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
 * package_name: com.hy.entity.paper
 */
@Setter
@Getter
@XStreamAlias("paperList")
public class Papers {
    /**
     * .
     * 问卷列表
     */
    @XStreamImplicit
    private List<Paper> paperList;
}
