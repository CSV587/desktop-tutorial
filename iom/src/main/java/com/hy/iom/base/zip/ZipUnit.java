package com.hy.iom.base.zip;

import java.io.File;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 16:58 2018/9/13
 * @ Description ：压缩单位类
 * @ Modified By ：
 * @ Version     ：1.0
 */
public class ZipUnit {

    private File file;

    private String fileName;

    public ZipUnit(File file, String fileName) {
        this.file = file;
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
