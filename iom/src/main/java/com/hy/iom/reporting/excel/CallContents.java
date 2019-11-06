package com.hy.iom.reporting.excel;

import com.hy.iom.utils.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 16:11 2018/9/5
 * @ Description ：录音对白Bean类
 * @ Modified By ：
 * @ Version     ：1.0
 */
public class CallContents {

    private String callNumber;

    private Date recordStartTime;

    private String uuid;

    private File excelFile;

    private String callContent;

    private String customInfo;

    private File voiceFile;

    public String formatterName() {
        return callNumber + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(recordStartTime) + "_" + UUID.randomUUID().toString();
    }

    public void writeExcel(Workbook workbook) throws IOException {
        String xlsxFilePath = FileUtils.randomTempPath(uuid + ".xlsx");
        File xlsxFile = new File(xlsxFilePath);
        if (!xlsxFile.exists()) xlsxFile.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(xlsxFile)) {
            workbook.write(fos);
        } catch (IOException e) {
            throw e;
        }
        this.setExcelFile(xlsxFile);
    }

    public CallContents(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public File getExcelFile() {
        return excelFile;
    }

    public void setExcelFile(File excelFile) {
        this.excelFile = excelFile;
    }

    public File getVoiceFile() {
        return voiceFile;
    }

    public void setVoiceFile(File voiceFile) {
        this.voiceFile = voiceFile;
    }

    public void delete() {
        if (excelFile != null && excelFile.exists()) {
            excelFile.delete();
            excelFile.getParentFile().delete();
        }
    }

    public String getCallContent() {
        return callContent;
    }

    public void setCallContent(String callContent) {
        this.callContent = callContent;
    }

    public String getCustomInfo() {
        return customInfo;
    }

    public void setCustomInfo(String customInfo) {
        this.customInfo = customInfo;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public Date getRecordStartTime() {
        return recordStartTime;
    }

    public void setRecordStartTime(Date recordStartTime) {
        this.recordStartTime = recordStartTime;
    }
}
