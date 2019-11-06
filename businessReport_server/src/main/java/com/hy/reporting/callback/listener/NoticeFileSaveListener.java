package com.hy.reporting.callback.listener;

import com.hy.config.NoticeEvent;
import com.hy.entity.filenotice.FileNotice;
import com.hy.entity.filenotice.FileNotices;
import com.hy.error.ErrorUtil;
import com.hy.reporting.callback.service.CallBackService;
import com.hy.util.CustomXmlUtil;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * .
 * Created by of liaoxg
 * date: 2019/10/31
 * user: lxg
 * package_name: com.hy.reporting.callback.listener
 */
@Component
@Slf4j
@PropertySource(value = "classpath:scheduler.properties", encoding = "UTF-8")
public class NoticeFileSaveListener implements ApplicationListener<NoticeEvent> {

    /**
     * .
     * ftp服务Ip
     */
    @Value("${ftpId}")
    private String ftpId;

    /**
     * .
     * ftp服务用户名
     */
    @Value("${ftpUser}")
    private String ftpUserName;

    /**
     * .
     * ftp服务密码
     */
    @Value("${ftpPwd}")
    private String ftpPwd;

    /**
     * .
     * ftp使用方式
     */
    @Value("${ftpFlag}")
    private String ftpFlag;

    /**
     * .
     * 秘钥文件路径
     */
    @Value("${pukPath:ccrobot_rsa}")
    private String pukPath;

    /**
     * .
     * 临时路径
     */
    @Value("${tmpPath:/tmp}")
    private String tmpPath;


    /**
     * .
     * callBackService
     */
    private final CallBackService callBackService;

    /**
     * .
     * 构造函数
     * 依赖注入
     *
     * @param service CallBackService
     */
    public NoticeFileSaveListener(final CallBackService service) {
        this.callBackService = service;
    }


    /**
     * .
     * 异步事件处理
     *
     * @param event event
     */
    @Async
    public void onApplicationEvent(final NoticeEvent event) {
        Map<String, FileNotices> fileNoticesMap;
        String xmlStr = event.getMessage();
        try {
            fileNoticesMap = CustomXmlUtil.unpack(xmlStr, FileNotices.class);
        } catch (Exception e) {
            log.error("解析异常:{},ErrorMsg:{}",
                xmlStr, ErrorUtil.getStackTrace(e));
            return;
        }
        FileNotices fileNotices = null;
        for (String key : fileNoticesMap.keySet()) {
            fileNotices = fileNoticesMap.get(key);
        }
        if (fileNotices != null) {
            try (SSHClient ssh = new SSHClient()) {
                ssh.addHostKeyVerifier(new PromiscuousVerifier());
                ssh.loadKnownHosts();
                ssh.connect(ftpId);
                if (ftpFlag.equals("psw")) {
                    ssh.authPassword(ftpUserName, ftpPwd);
                } else {
                    ssh.authPublickey(ftpUserName, pukPath);
                }
                try (SFTPClient sftp = ssh.newSFTPClient()) {
                    List<FileNotice> fileNoticeList
                        = fileNotices.getNoticeReq();
                    for (FileNotice fileNotice : fileNoticeList) {
                        callBackService.dealSingleFile(fileNotice, sftp);
                    }
                }
            } catch (IOException e) {
                log.error("ssh异常:{}", ErrorUtil.getStackTrace(e));
            }
        }
        log.debug("消息处理完成");
    }
}
