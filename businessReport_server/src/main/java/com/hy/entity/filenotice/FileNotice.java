package com.hy.entity.filenotice;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-11
 * user: lxg
 * package_name: com.hy.reporting.callback.entities
 */
@Setter
@Getter
@XStreamAlias("noticeReq")
public class FileNotice {
    /**
     * .
     * 文件名
     */
    private String fileName;
    /**
     * .
     * 文件路径
     */
    private String filePath;
    /**
     * .
     * 文件大小
     */
    private String fileSize;
    /**
     * .
     * 文件的Md5值
     */
    private String fileMd5Code;
    /**
     * .
     * 文件上传时间
     */
    private String fileUploadTime;
    /**
     * .
     * 文件描述
     */
    private String fileDescription;
}
