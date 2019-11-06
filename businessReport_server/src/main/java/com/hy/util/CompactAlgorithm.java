package com.hy.util;

import com.hy.error.ErrorUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-22
 * user: lxg
 * package_name: com.hy.util
 */
@Slf4j
public final class CompactAlgorithm {

    /**
     * .
     * bytes数组大小
     */
    private static final int BYTES_SIZE = 1024;

    /**
     * .
     * 工具类隐藏私有构造函数
     */
    private CompactAlgorithm() {

    }

    /**
     * .
     * 压缩文件
     *
     * @param srcFile    原始文件
     * @param targetFile 目的文件
     * @param includeDir 是否包含目录
     */
    public static void zipFiles(final File srcFile,
                                final File targetFile,
                                final boolean includeDir) {
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(targetFile));
            String baseDir = "";
            if (includeDir) {
                baseDir = srcFile.getName() + File.separator;
            }
            if (srcFile.isFile()) {
                zipFile(srcFile, out, baseDir);
            } else {
                File[] list = srcFile.listFiles();
                if (list != null) {
                    for (File file : list) {
                        compress(file, out, baseDir);
                    }
                }
            }
            log.info("压缩完毕");
        } catch (Exception e) {
            log.error("{}", ErrorUtil.getStackTrace(e));
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * .
     * 统一调用该方法
     *
     * @param file    原始文件
     * @param out     压缩流
     * @param basedir 目录
     */
    private static void compress(final File file,
                                 final ZipOutputStream out,
                                 final String basedir) {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            zipDirectory(file, out, basedir);
        } else {
            zipFile(file, out, basedir);
        }
    }

    /**
     * .
     * 压缩单个文件
     *
     * @param srcFile 原始文件
     * @param out     ZipOutputStream
     * @param basedir 根路径
     */
    private static void zipFile(final File srcFile,
                                final ZipOutputStream out,
                                final String basedir) {
        if (!srcFile.exists()) {
            return;
        }

        byte[] buf = new byte[BYTES_SIZE];
        FileInputStream in = null;

        try {
            int len;
            in = new FileInputStream(srcFile);
            out.putNextEntry(new ZipEntry(basedir + srcFile.getName()));
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.closeEntry();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("{}", ErrorUtil.getStackTrace(e));
            }
        }
    }

    /**
     * .
     * 压缩文件夹
     *
     * @param dir     目录
     * @param out     zip流
     * @param basedir 基础地址
     */
    private static void zipDirectory(final File dir,
                                     final ZipOutputStream out,
                                     final String basedir) {
        if (!dir.exists()) {
            return;
        }

        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                compress(file, out, basedir + dir.getName() + File.separator);
            }
        }
    }
}
