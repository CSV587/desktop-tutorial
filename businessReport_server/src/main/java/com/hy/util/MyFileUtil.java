package com.hy.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-23
 * user: lxg
 * package_name: com.hy.util
 */
public final class MyFileUtil {

    /**
     * .
     * 工具类隐藏构造函数
     */
    private MyFileUtil() {

    }

    /**
     * .
     * 获取文件的md5值
     *
     * @param file 文件对象
     * @return 字符串
     * @throws IOException IOException
     */
    public static String getFileMd5(final File file)
        throws IOException {
        return DigestUtils.md5Hex(new FileInputStream(file));
    }

    /**
     * .
     * 获取文件长度
     *
     * @param file 文件对象
     * @return 文件大小
     */
    public static long getFileSize(final File file) {
        if (file.exists() && file.isFile()) {
            return file.length();
        }
        return 0;
    }
}
