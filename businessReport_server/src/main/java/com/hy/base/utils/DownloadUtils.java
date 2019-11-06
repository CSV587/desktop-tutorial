package com.hy.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * .
 *
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 14:23 2018/9/5
 * @ Description ：web实现断点续传下载
 * @ Modified By ：
 * @ Version     ：1.0
 */
@Slf4j
public final class DownloadUtils {

    /**
     * .
     * 读流byte数组长度
     */
    private static final int BYTES_LENGTH = 1024;


    /**
     * .
     * 下载byte数组长度
     */
    private static final int DOWNLOAD_BYTES_LENGTH = 4016;

    /**
     * .
     * 1秒
     */
    private static final int ONE_SECOND = 1000;

    /**
     * .
     * 3
     */
    private static final int THREE = 3;

    /**
     * .
     * 工具类隐藏构造函数
     */
    private DownloadUtils() {

    }

    /**
     * .
     * 下载文件
     *
     * @param file     文件对象
     * @param response HttpServletResponse
     * @param request  HttpServletRequest
     */
    public static void downloadFile(final File file,
                                    final HttpServletResponse response,
                                    final HttpServletRequest request) {
        if (!file.exists()) {
            log.error("文件不存在:{}", file.getAbsolutePath());
            return;
        }
        downloadFileRanges(file, request, response);
    }

    /**
     * .
     * 流文件形式下载文件
     *
     * @param downloadFile 下载文件对象
     * @param request      HttpServletRequest
     * @param response     HttpServletResponse
     */
    private static void downloadFileRanges(final File downloadFile,
                                           final HttpServletRequest request,
                                           final HttpServletResponse response) {
        try {
            FileInputStream fis = new FileInputStream(downloadFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[BYTES_LENGTH];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            byte[] aBytes = bos.toByteArray();

            String range = request.getHeader("Range");
            response.reset();
            long startByte = 0;
            long endByte = aBytes.length - 1;
            if (range != null
                && range.contains("bytes=")
                && range.contains("-")) {
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                range = range.substring(range.lastIndexOf("=") + 1).trim();
                String[] ranges = range.split("-");
                try {
                    if (ranges.length == 1) {
                        if (range.startsWith("-")) {
                            endByte = Long.parseLong(ranges[0]);
                        } else if (range.endsWith("-")) {
                            startByte = Long.parseLong(ranges[0]);
                        }
                    } else if (ranges.length == 2) {
                        startByte = Long.parseLong(ranges[0]);
                        endByte = Long.parseLong(ranges[1]);
                    }

                } catch (NumberFormatException e) {
                    startByte = 0;
                    endByte = aBytes.length - 1;
                }
            }

            long contentLength = endByte - startByte + 1;

            String fileName = downloadFile.getName();

            String contentType = request
                .getServletContext()
                .getMimeType(fileName);
            response.setHeader("Accept-Ranges", "bytes");
            response.setContentType(contentType);
            response.setHeader("Content-Type", contentType);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Content-Length", String.valueOf(aBytes.length));
            response.setHeader("Content-Length", String.valueOf(aBytes.length));
            response.setHeader("Content-Disposition",
                "attachment;filename=" + fileName + "");
            response.setHeader("Content-Range",
                "bytes "
                    + startByte
                    + "-"
                    + endByte
                    + "/"
                    + aBytes.length);
            BufferedOutputStream outputStream;
            InputStream sbs = new ByteArrayInputStream(aBytes);

            startByte = sbs.skip(startByte);

            long transmitted = 0;
            try {
                outputStream =
                    new BufferedOutputStream(response.getOutputStream());
                byte[] buff = new byte[DOWNLOAD_BYTES_LENGTH];
                int len;
                while ((transmitted <= contentLength)
                    && ((len = sbs.read(buff)) != -1)) {
                    outputStream.write(buff, 0, len);
                    transmitted += len;
                }
                outputStream.flush();
                response.flushBuffer();
                sbs.close();
                log.debug("下载完毕："
                    + startByte
                    + "-"
                    + endByte
                    + "："
                    + transmitted);
            } catch (ClientAbortException e) {
                log.debug("用户停止下载："
                    + startByte + "-"
                    + endByte + "：" + transmitted);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    sbs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * .
     * 从网络Url中下载文件
     *
     * @param urlStr   url地址
     * @param fileName 文件名
     * @param savePath 存储路径
     * @param toekn    token地址
     * @throws IOException 异常
     */
    public static void downLoadFromUrl(final String urlStr,
                                       final String fileName,
                                       final String savePath,
                                       final String toekn)
        throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(THREE * ONE_SECOND);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent",
            "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.setRequestProperty("lfwywxqyh_token", toekn);

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            if (saveDir.mkdir()) {
                log.info("创建文件夹{}成功", saveDir);
            }
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        fos.close();
        inputStream.close();
        log.info("info: {} download success to {}",
            url,
            file.getAbsolutePath());
    }


    /**
     * .
     * 从输入流中获取字节数组
     *
     * @param inputStream 输入流
     * @return 输出结果
     * @throws IOException 异常
     */
    private static byte[] readInputStream(final InputStream inputStream)
        throws IOException {
        byte[] buffer = new byte[BYTES_LENGTH];
        int len;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
