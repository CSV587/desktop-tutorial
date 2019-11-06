package com.hy.iom.utils;

import com.hy.iom.base.zip.ZipUnit;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
            return;
        }
        downloadFileRanges(file, request, response);
    }

    private static void downloadFileRanges(File downloadFile, HttpServletRequest request, HttpServletResponse response) {
        try {
            FileInputStream fis = new FileInputStream(downloadFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
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
            response.setHeader("Content-Type", contentType);
            response.setHeader("Content-Length", String.valueOf(aBytes.length));
            response.setHeader("Content-Disposition",
                "attachment;filename=" + fileName + "");
            response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + aBytes.length);
            BufferedOutputStream outputStream;
            InputStream sbs = new ByteArrayInputStream(aBytes);

            startByte = sbs.skip(startByte);

            long transmitted = 0;
            try {
                outputStream = new BufferedOutputStream(response.getOutputStream());
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

//    public static void downloadZipFiles(List<ZipUnit> files, String fileName, HttpServletResponse response, HttpServletRequest request) {
//        //响应头的设置
//        response.reset();
//        response.setCharacterEncoding("utf-8");
//        response.setContentType("multipart/form-data");
//
//        //设置压缩包的名字
//        //解决不同浏览器压缩包名字含有中文时乱码的问题
//        String downloadName = fileName;
//        String agent = request.getHeader("USER-AGENT");
//
//        DataOutputStream os = null;
//        ZipOutputStream zipos = null;
//        try {
//            if (agent.contains("MSIE") || agent.contains("Trident")) {
//                downloadName = java.net.URLEncoder.encode(downloadName, "UTF-8");
//            } else {
//                downloadName = new String(downloadName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
//            }
//            response.setHeader("Content-Disposition", "attachment;fileName=\"" + downloadName + "\"");
//            //设置压缩流：直接写入response，实现边压缩边下载
//            zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
//            zipos.setMethod(ZipOutputStream.DEFLATED); //设置压缩方法
//            if (files != null && files.size() > 0) {
//                for (ZipUnit unit : files) {
//                    if (unit == null) {
//                        continue;
//                    }
//                    File file = unit.getFile();
//                    if (!file.exists()) {
//                        log.error("文件不存在 跳过,{}", file.getAbsolutePath());
//                    }
//                    //添加ZipEntry，并ZipEntry中写入文件流
//                    //这里，加上i是防止要下载的文件有重名的导致下载失败
//                    if (zipos != null) {
//                        zipos.putNextEntry(new ZipEntry(unit.getFileName()));
//                        os = new DataOutputStream(zipos);
//                        try (InputStream is = new FileInputStream(file)) {
//                            byte[] b = new byte[64 * 1024];
//                            int length;
//                            while ((length = is.read(b)) != -1) {
//                                os.write(b, 0, length);
//                            }
//                            zipos.closeEntry();
//                        } catch (Exception e) {
//                            log.error("单个文件压缩出错,{}", file.getAbsolutePath());
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (os != null) {
//                try {
//                    os.flush();
//                } catch (Exception e) {
//                    log.error("刷新流出错！,{}", e);
//                }
//                try {
//                    os.close();
//                } catch (Exception e) {
//                    log.error("关闭流出错！,{}", e);
//                }
//            }
//            if (zipos != null) {
//                try {
//                    zipos.close();
//                } catch (Exception e) {
//                    log.error("关闭流出错！,{}", e);
//                }
//            }
//        }
//    }
}
