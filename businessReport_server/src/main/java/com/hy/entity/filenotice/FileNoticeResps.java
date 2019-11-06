package com.hy.entity.filenotice;

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
 * package_name: com.hy.entity.filenotice
 */
@Setter
@Getter
@XStreamAlias("noticeRespList")
public class FileNoticeResps {
    /**
     * .
     * 响应文件列表
     */
    @XStreamImplicit
    private List<FileNoticeResp> fileNoticeRespList;
}
