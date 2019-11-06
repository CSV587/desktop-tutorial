package com.hy.cpic.base.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 21:54 2018/9/7
 * @ Description ：录音格式转换类
 * @ Modified By ：
 * @ Version     ：1.0
 */
@Slf4j
@Component
@Setter
@Getter
public class AudioUtils {

    private String codec = "libmp3lame";

    @Value("${voice.bitRate}")
    private Integer bitRate;

    @Value("${voice.channels}")
    private Integer channels;

    @Value("${voice.samplingRate}")
    private Integer samplingRate;

    @Value("${voice.ffmpegPath}")
    private String ffmpegPath;

    /**
     * 执行转化
     *
     * @param source 输入文件
     * @param target 目标文件
     * @return 转换之后文件
     */
    public boolean wav2Mp3(File source, File target)
        throws Exception {
        File file = new File(ffmpegPath);
        if (file.exists()) {
            long start = System.currentTimeMillis();
            log.debug("转码参数 比特率{} 声道数{} 采样率{}", bitRate, channels, samplingRate);
            String command = String.format("%s -i  %s -ar %s -ac %s -y -acodec %s %s ", ffmpegPath, source.getAbsolutePath(), samplingRate, channels, codec, target.getAbsolutePath());
            log.debug(command);
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            log.debug(sb.toString());
            long end = System.currentTimeMillis();
            log.debug("转码耗时{}ms", (end - start));
            return true;
        } else {
            return false;
        }
    }
}
