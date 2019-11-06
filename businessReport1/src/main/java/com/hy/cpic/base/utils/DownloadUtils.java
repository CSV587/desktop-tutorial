package com.hy.cpic.base.utils;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 14:23 2018/9/5
 * @ Description ：web实现断点续传下载
 * @ Modified By ：
 * @ Version     ：1.0
 */
public class DownloadUtils {

    private static Logger log = LoggerFactory.getLogger(DownloadUtils.class);

    public static void downloadFile(File file, HttpServletResponse response, HttpServletRequest request) {
        if (!file.exists()) {
            log.error("文件不存在:{}", file.getAbsolutePath());
        }

        try {
            downloadFileRanges(file, request, response);
        } catch (Exception e) {
            //ignore
        }

    }

    private static void downloadFileRanges(File downloadFile, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            byte[] aBytes;
            try (FileInputStream fis = new FileInputStream(downloadFile);
                 ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
                byte[] b = new byte[1024];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
                aBytes = bos.toByteArray();
            }

            String range = request.getHeader("Range");
            response.reset();
            long startByte = 0;
            long endByte = aBytes.length - 1;
            if (range != null && range.contains("bytes=") && range.contains("-")) {
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                range = range.substring(range.lastIndexOf("=") + 1).trim();
                String ranges[] = range.split("-");
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

            String contentType = request.getServletContext().getMimeType(fileName);
            response.setHeader("Accept-Ranges", "bytes");
            response.setContentType(contentType);
            if (downloadFile.getName().endsWith(".mp3")) {
                response.setContentType("audio/mpeg");
            }
            response.setHeader("Content-Type", contentType);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Content-Length", String.valueOf(aBytes.length));
            response.setHeader("Content-Length", String.valueOf(aBytes.length));
            response.setHeader("Content-Disposition",
                "attachment;filename=" + fileName + "");
            response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + aBytes.length);


            long transmitted = 0;
            try (BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                 InputStream sbs = new ByteArrayInputStream(aBytes);) {
                startByte = sbs.skip(startByte);
                byte[] buff = new byte[4096];
                int len;
                while ((transmitted <= contentLength) && (len = sbs.read(buff)) != -1) {
                    outputStream.write(buff, 0, len);
                    transmitted += len;
                }
                outputStream.flush();
                response.flushBuffer();
                sbs.close();
                log.debug("下载完毕：" + startByte + "-" + endByte + "：" + transmitted);
            } catch (ClientAbortException e) {
                log.debug("用户停止下载：" + startByte + "-" + endByte + "：" + transmitted);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String getDownloadChineseFileName(String paramName) {
        String downloadChineseFileName = "";
        try {
            downloadChineseFileName = new String(paramName.getBytes("GBK"),
                "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return downloadChineseFileName;
    }


    /**
     * 从网络Url中下载文件
     *
     * @param urlStr   url地址
     * @param fileName 文件名
     * @param savePath 存储路径
     * @throws IOException 异常
     */
    public static void downLoadFromUrl(String urlStr, String fileName, String savePath, String toekn) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
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
        try (FileOutputStream fos = new FileOutputStream(file);) {
            fos.write(getData);
        }
        inputStream.close();
        log.info("info: {} download success to {}", url, file.getAbsolutePath());
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream 输入流
     * @return 输出结果
     * @throws IOException 异常
     */
    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
