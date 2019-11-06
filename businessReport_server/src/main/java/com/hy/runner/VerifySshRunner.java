package com.hy.runner;

import com.hy.error.ErrorUtil;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * .
 * Created by of liaoxg
 * date: 2019/10/29
 * user: lxg
 * package_name: com.hy.runner
 */
@Slf4j
@Component
@PropertySource(value = "classpath:scheduler.properties", encoding = "UTF-8")
@Order(value = 0)
public class VerifySshRunner implements CommandLineRunner {

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

    @Override
    public void run(String... args) {
        try (SSHClient ssh = new SSHClient()) {
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.loadKnownHosts();
            ssh.connect(ftpId);
            if (ftpFlag.equals("psw")) {
                ssh.authPassword(ftpUserName, ftpPwd);
            } else {
                ssh.authPublickey(ftpUserName, pukPath);
            }
            if (!ssh.isConnected()) {
                throw new IllegalStateException("Not connected");
            }
            if (!ssh.isAuthenticated()) {
                throw new IllegalStateException("Not authenticated");
            }
        } catch (IOException e) {
            log.error("ssh异常:{}", ErrorUtil.getStackTrace(e));
            System.exit(1);
        }
        log.info("ssh校验正常");
    }
}
