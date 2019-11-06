package com.hy.entity.filenotice;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-17
 * user: lxg
 * package_name: com.hy.entity.filenotice
 */
@Setter
@Getter
@XStreamAlias("noticeResp")
public class FileNoticeResp {
    /**
     * .
     * 文件名
     */
    private String fileName;
    /**
     * .
     * 文件接收状态
     */
    private String fileReceiveState;
}
