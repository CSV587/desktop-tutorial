package com.hy.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import security.crypt.EnvelopeUtil;
import security.crypt.SignUtil;

import java.io.File;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-23
 * user: lxg
 * package_name: com.hy.util
 */
@Component
@Slf4j
@PropertySource(value = "classpath:scheduler.properties", encoding = "UTF-8")
public final class CharactersEncryptionUtil {

    /**
     * .
     * 工具类隐藏构造函数
     */
    private CharactersEncryptionUtil() {

    }

    /**
     * .
     * 加密工具类
     */
    private static EnvelopeUtil envelopeUtil;
    /**
     * .
     * 签名工具类
     */
    private static SignUtil signUtil;
    /**
     * .
     * cert文件路径
     */
    private static String encryptCertFile;
    /**
     * .
     * 签名文件路径
     */
    private static String signP12File;
    /**
     * .
     * 签名秘钥
     */
    private static String signP12FilePassword = "ccbivr_cccp";

    /**
     * .
     * 密码
     *
     * @param password 密码
     */
    @Value("${p12Password:ccbivr_cccp}")
    public void setSignP12FilePassword(final String password) {
        log.debug("password:{}", password);
        CharactersEncryptionUtil.signP12FilePassword = password;
    }

    static {
        log.debug("StringEncript init");
        String cryptAlgrorithm = "AES128_CBC";
        envelopeUtil = new EnvelopeUtil(cryptAlgrorithm);
        String signAlgrorithm = "SHA1withRSA";
        signUtil = new SignUtil(signAlgrorithm);
        log.debug("cryptAlgrorithm: {}\n", cryptAlgrorithm);
        log.debug("signAlgrorithm: {}\n", signAlgrorithm);
        String path = new Object() {
            String getPath() {
                return this.getClass().getResource("/").getPath();
            }
        }.getPath();
        encryptCertFile =
            String.format("%s%scert%s%s", path, File.separator,
                File.separatorChar, "ccbivrsend.cer");
        signP12File =
            String.format("%s%scert%s%s", path, File.separator,
                File.separatorChar, "ccbivrsend.p12");
        log.debug(signP12FilePassword);
    }

    /**
     * .
     * 加密
     *
     * @param sourceData 原始数据
     * @return 加密数据
     * @throws Exception Exception
     */
    public static String encryption(final String sourceData)
        throws Exception {
        return envelopeUtil.envelopeMessage(sourceData, encryptCertFile);

    }

    /**
     * .
     * 解密
     *
     * @param sourceData 原始数据
     * @return 解密数据
     * @throws Exception Exception
     */
    public static String decryption(final String sourceData)
        throws Exception {
        return envelopeUtil.openEnvelope(sourceData,
            signP12File, signP12FilePassword);
    }

    /**
     * .
     * 数据签名
     *
     * @param sourceData 原始数据
     * @return 签名数据
     * @throws Exception Exception
     */
    public static String signature(final String sourceData)
        throws Exception {
        return signUtil.signData(sourceData,
            signP12File, signP12FilePassword);
    }

    /**
     * .
     * 验证签名
     *
     * @param sourceData 原始数据
     * @param signedData 签名数据
     * @return 是否通过
     * @throws Exception Exception
     */
    public static boolean checkSignature(final String sourceData,
                                         final String signedData)
        throws Exception {
        return signUtil.verifySignedData(sourceData,
            signedData, encryptCertFile);
    }
}
