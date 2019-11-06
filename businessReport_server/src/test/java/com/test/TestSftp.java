package com.test;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;

import java.io.IOException;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-24
 * user: lxg
 * package_name: com.test
 */
public class TestSftp {
    public static void main(String[] args) throws IOException {
        final SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.loadKnownHosts();
        ssh.connect("172.16.18.215");
        try {
            ssh.authPublickey("ccrobot", "/Users/lxg/Downloads/ccrobot_rsa");
            try (SFTPClient sftp = ssh.newSFTPClient()) {
                sftp.get("/home/ccrobot/testSsh",
                    new FileSystemFile("/Users/lxg/Downloads"));
            }
        } finally {
            ssh.disconnect();
        }
    }
}
