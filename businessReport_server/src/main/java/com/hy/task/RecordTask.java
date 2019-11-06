package com.hy.task;

import com.hy.connector.constant.CallBackTaskConstant;
import com.hy.entity.filenotice.FileNotice;
import com.hy.entity.filenotice.FileNoticeResp;
import com.hy.entity.filenotice.FileNoticeResps;
import com.hy.entity.filenotice.FileNotices;
import com.hy.entity.record.RecordEntity;
import com.hy.error.ErrorUtil;
import com.hy.reporting.callback.service.CallBackService;
import com.hy.util.BusinessType;
import com.hy.util.CompactAlgorithm;
import com.hy.util.CustomXmlUtil;
import com.hy.util.HttpRequestUtils;
import com.hy.util.MessageType;
import com.hy.util.MyFileUtil;
import com.hy.util.ObjToStrUtil;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-19
 * user: lxg
 * package_name: com.hy.task
 */
@Slf4j
@Service
@PropertySource(value = "classpath:scheduler.properties", encoding = "UTF-8")
public class RecordTask {

    /**
     * .
     * 录音文件加一级目录字符长度
     */
    private static final int WAV_DIR_LENGTH = 43;

    /**
     * .
     * 回访数据service 服务
     */
    private final CallBackService callBackService;

    /**
     * .
     *
     * @param service CallBackService
     */
    public RecordTask(final CallBackService service) {
        this.callBackService = service;
    }

    /**
     * .
     * 临时路径
     */
    @Value("${tmpPath:/tmp}")
    private String tmpPath;

    /**
     * .
     * 远端上传路径
     */
    @Value("${remoteUploadPath}")
    private String remoteUploadPath;

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
    @Value("${ftpPwd:123456}")
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

    /**
     * .
     * 单个录音文件大小
     */
    @Value("${oneFileRecordNumber:100}")
    private int recordFileNumber;

    /**
     * .
     * 回传录音文件生成
     */
    @Scheduled(cron = "${exportRecord.cron}")
    public void exportRecord() {
        long startTime = System.currentTimeMillis();
        log.debug("开始导出录音");
        List<RecordEntity> recordEntities
            = callBackService.queryAllCallBackRecordData();
        String prefixStr = "Record-" + UUID.randomUUID().toString();
        int end;
        for (int start = 0; start < recordEntities.size(); start = end) {
            end = start + recordFileNumber;
            exportFile(recordEntities, start, end, prefixStr);
        }
        long endTime = System.currentTimeMillis();
        log.debug("结束导出录音,共{}条数据，共耗时{}ms",
            recordEntities.size(), endTime - startTime);
    }

    /**
     * .
     * 导出文件
     *
     * @param recordEntities 实体列表
     * @param start          开始区间
     * @param end            结束区间
     * @param prefixStr      前缀key
     */
    private void exportFile(final List<RecordEntity> recordEntities,
                            final int start,
                            final int end,
                            final String prefixStr) {
        String sourceFilePath = String.format("%s%s%s-%s-%s",
            tmpPath, File.separator,
            prefixStr, start, end);
        String recordFileName = String.format("%s%scc_recorddetail.txt",
            sourceFilePath, File.separator);
        log.debug("录音数据文件:{}", recordFileName);
        File recordFile = new File(recordFileName);
        List<String> items = new ArrayList<>();
        int recordCount = 0;
        for (int i = start; i < end && i < recordEntities.size(); i++) {
            RecordEntity recordEntity = recordEntities.get(i);
            String recordPath = recordEntity.getRecordPath();
            if (recordPath == null) {
                continue;
            }

            File sourceFile = new File(recordPath);
            if (!sourceFile.exists()) {
                log.error("录音文件{}不存在", recordPath);
                callBackService.updateInValidCallBackRecord(recordEntity.getKeyStr());
                continue;
            }

            String targetPath = String.format("%s%srecords%s%s",
                sourceFilePath, File.separator, File.separator,
                recordPath.substring(recordPath.length() - WAV_DIR_LENGTH));
            log.debug("目标文件:{}", targetPath);

            File targetFile = new File(targetPath);
            if (!targetFile.getParentFile().exists()) {
                log.debug("文件夹不存在:{}",
                    targetFile.getParentFile().getAbsolutePath());
                if (targetFile.getParentFile().mkdirs()) {
                    log.debug("文件夹{}创建成功",
                        targetFile.getParentFile().getAbsolutePath());
                } else {
                    log.debug("文件夹{}创建失败",
                        targetFile.getParentFile().getAbsolutePath());
                    continue;
                }
            }
            try {
                Files.copy(sourceFile.toPath(),
                    targetFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                log.error("文件复制失败:{}", ErrorUtil.getStackTrace(e));
                continue;
            }
            if (!targetFile.exists()) {
                log.warn("文件复制失败:{} ====> {}", recordPath, targetPath);
                continue;
            }

            recordEntity.setRecordPath(
                targetPath.replace(
                    sourceFilePath + File.separator,
                    ""));
            String recordStr = ObjToStrUtil.objToStr(recordEntity, "|@|");
            log.debug("对应录音数据:{}", recordStr);

            try {
                FileUtils.write(recordFile, recordStr + "\r\n", true);
                items.add(recordEntity.getKeyStr());
            } catch (IOException e) {
                log.error("写入文件失败:{}", ErrorUtil.getStackTrace(e));
            }
            recordCount++;
        }
        if (recordCount > 0) {
            File sourceDir = new File(sourceFilePath);
            if (!sourceDir.exists()) {
                log.warn("目录不存在:{}", sourceFilePath);
            }
            String tarZipPath = String.format("%s.zip", sourceFilePath);
            log.debug("zipFile:{}", tarZipPath);

            File tarZip = new File(tarZipPath);

            CompactAlgorithm.zipFiles(sourceDir, tarZip, true);
            if (FileUtils.deleteQuietly(sourceDir)) {
                log.info("文件删除:{}", sourceFilePath);
            } else {
                log.info("文件删除失败:{}", sourceFilePath);
            }
            int count = callBackService.updateBatchCallBackRecord(items);
            log.debug("实际更新回写录音文件数据{}", count);
        }
        log.debug("更新回写录音文件数据{}", recordCount);
    }

    /**
     * .
     * 录音文件回传
     */
    @Scheduled(cron = "${uploadRecord.cron}")
    public void uploadRecord() {
        long startTime = System.currentTimeMillis();
        log.debug("开始上传录音");
        List<FileNotice> fileNoticeList = new ArrayList<>();
        List<File> fileList = scanFile();
        if (!fileList.isEmpty()) {
            try (SSHClient ssh = new SSHClient()) {
                ssh.addHostKeyVerifier(new PromiscuousVerifier());
                ssh.loadKnownHosts();
                ssh.connect(ftpId);
                if (ftpFlag.equals("psw")) {
                    ssh.authPassword(ftpUserName, ftpPwd);
                } else {
                    ssh.authPublickey(ftpUserName, pukPath);
                }
                try (SFTPClient sftp = ssh.newSFTPClient()) {
                    for (File file : fileList) {
                        FileNotice notice = uploadSingeFile(file, sftp);
                        fileNoticeList.add(notice);
                    }
                }
            } catch (IOException e) {
                log.error("上传录音文件异常：{}", ErrorUtil.getStackTrace(e));
            }
            log.debug("录音上传成功");
            try {
                sendRecord(fileNoticeList);
            } catch (Exception e) {
                log.error("发送上传录音报文异常：{}", ErrorUtil.getStackTrace(e));
            }
        } else {
            log.debug("无可上传录音文件");
        }

        long endTime = System.currentTimeMillis();
        log.debug("结束上传录音,共{}条数据，共耗时{}ms",
            fileList.size(), endTime - startTime);
    }

    /**
     * .
     * 发送报文
     *
     * @param fileNoticeList 上传文件报文集合
     * @throws Exception Exception
     */
    private void sendRecord(
        final List<FileNotice> fileNoticeList)
        throws Exception {
        if (fileNoticeList == null
            || fileNoticeList.isEmpty()) {
            log.info("上传文件数据为空");
            return;
        }
        log.debug("发送报文开始");
        FileNotices fileNotices = new FileNotices();
        fileNotices.setNoticeReq(fileNoticeList);
        long startTime = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();
        String xmlStr = CustomXmlUtil.pack(fileNotices,
            BusinessType.FileNotices,
            MessageType.request,
            uuid);
        log.debug("目标地址:{}", CallBackTaskConstant.getNoticeFileUrl());
        String result = HttpRequestUtils.httpPost(
            CallBackTaskConstant.getNoticeFileUrl(), xmlStr);
        log.debug("回执报文:\n{}", result);
        Map<String, FileNoticeResps> fileNoticeRespsMap
            = CustomXmlUtil.unpack(result, FileNoticeResps.class);
        FileNoticeResps resps = null;
        String traceId = null;
        for (String key : fileNoticeRespsMap.keySet()) {
            traceId = key;
            resps = fileNoticeRespsMap.get(key);
        }
        if (uuid.equals(traceId) && resps != null) {
            List<FileNoticeResp> fileNoticeRespList
                = resps.getFileNoticeRespList();
            for (FileNoticeResp resp : fileNoticeRespList) {
                String name = resp.getFileName();
                if ("0".equals(resp.getFileReceiveState())) {
                    String filePath
                        = String.format("%s%s%s", tmpPath,
                        File.separator, name);
                    File file = new File(filePath);
                    Files.delete(file.toPath());
                } else {
                    log.info("处理录音文件{}失败", name);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        log.debug("发送报文结束,共耗时{}ms", endTime - startTime);
    }

    /**
     * .
     * 扫描未传录音文件
     *
     * @return 录音文件集合
     */
    private List<File> scanFile() {
        IOFileFilter fileFilter =
            FileFilterUtils
                .and(FileFilterUtils.prefixFileFilter("Record-"),
                    FileFilterUtils.suffixFileFilter(".zip"));
        return new ArrayList<>(FileUtils.listFiles(new File(tmpPath),
            fileFilter,
            null));
    }

    /**
     * .
     * 上传文件
     *
     * @param file file
     * @param sftp SFTPClient
     * @return FileNotice
     * @throws IOException IOException
     */
    private FileNotice uploadSingeFile(final File file,
                                       final SFTPClient sftp)
        throws IOException {
        SimpleDateFormat format =
            new SimpleDateFormat("yyyyMMddHHmmss");
        FileNotice fileNotice = new FileNotice();
        fileNotice.setFileName(file.getName());
        fileNotice.setFilePath(remoteUploadPath);
        fileNotice.setFileSize(String.valueOf(MyFileUtil.getFileSize(file)));
        fileNotice.setFileUploadTime(format.format(new Date()));
        fileNotice.setFileDescription("录音文件");
        sftp.put(new FileSystemFile(file),
            remoteUploadPath + File.separator + file.getName());
        String md5Str = MyFileUtil.getFileMd5(file);
        fileNotice.setFileMd5Code(md5Str);
        return fileNotice;
    }
}
