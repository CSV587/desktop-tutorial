package com.hy.entity.filenotice;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-11
 * user: lxg
 * package_name: com.hy.reporting.callback.entities
 */
@Setter
@Getter
@XStreamAlias("noticeReqList")
public class FileNotices {
    /**
     * .
     * 文件列表
     */
    @XStreamImplicit
    private List<FileNotice> noticeReq;
}
