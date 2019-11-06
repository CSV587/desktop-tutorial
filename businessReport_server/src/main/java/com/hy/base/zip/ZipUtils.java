package com.hy.base.zip;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import java.io.ByteArrayOutputStream;

/**
 * .
 *
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 16:19 2018/8/31
 * @ Description ：字符串压缩类
 * @ Modified By ：
 * @ Version     ：
 */

public final class ZipUtils {


    /**
     * .
     * 默认byte数组大小
     */
    private static final int DEF_BYTE_ARRAY_LENGTH = 1024;

    /**
     * .
     * 工具类隐藏构造函数
     */
    private ZipUtils() {

    }

    /**
     * .
     * 使用gzip进行压缩
     *
     * @param primStr 压缩文本
     * @return BASE64Encoder编码后压缩流
     */
    public static String zip(final String primStr) {
        if (primStr == null || primStr.length() == 0) {
            return primStr;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(primStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new sun.misc.BASE64Encoder().encode(out.toByteArray());
    }

    /**
     * .
     * Description:使用gzip进行解压缩
     *
     * @param compressedStr 压缩文本
     * @return 解压文本
     */
    public static String unzip(final String compressedStr) {
        if (compressedStr == null) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream ginzip = null;
        byte[] compressed;
        String decompressed = null;
        try {
            compressed =
                new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
            in = new ByteArrayInputStream(compressed);
            ginzip = new GZIPInputStream(in);

            byte[] buffer = new byte[DEF_BYTE_ARRAY_LENGTH];
            int offset;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ginzip != null) {
                try {
                    ginzip.close();
                } catch (IOException ignored) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
            try {
                out.close();
            } catch (IOException ignored) {
            }
        }
        return decompressed;
    }

}
