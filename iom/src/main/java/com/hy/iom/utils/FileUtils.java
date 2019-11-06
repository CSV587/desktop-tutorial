package com.hy.iom.utils;

import com.hy.iom.base.config.BasicConfig;
import com.hy.iom.reporting.excel.CallContents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class FileUtils {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private List<String> allFiles = new ArrayList<String>();
    private List<File> ls_file = new ArrayList<File>();
    private static String tempDirPath = BasicConfig.getTempFileDirPath();
    /**
     * 获取所有需要解析的文件
     *
     * @param file
     * @return
     * @throws java.io.IOException
     */
    public List<String> findAllFiles(File file) throws IOException {
        if(file.exists()){
            if(file.isDirectory()){
                if(!new File(file.getCanonicalFile()+Constant.SUFFIX_SUCCESS).exists() && !new File(file.getCanonicalFile()+Constant.SUFFIX_ERROR).exists()){
                    File[] files = file.listFiles();
                    if(files != null){
                        for(File f : files){
                            findAllFiles(f);
                        }
                    }
                }
            }else{
                if(!file.getName().contains(Constant.SUFFIX_SUCCESS) || !file.getName().contains(Constant.SUFFIX_ERROR)){
                    File tmpFile = new File(file.getCanonicalFile().getParent()+Constant.SUFFIX_SUCCESS);
                    File tmpFileErr = new File(file.getCanonicalFile().getParent()+Constant.SUFFIX_ERROR);
                    if(!tmpFile.exists() && !tmpFileErr.exists() && Constant.TARGET_FILE_NAME.equals(file.getName())){
                        allFiles.add(file.getCanonicalPath());
                    }
                }

            }
        }else{
            log.debug("目录 "+file+" 不存在");
        }
        return allFiles;
    }

    public static String getTempDirPath() {
        return tempDirPath;
    }

    public List<File> findAllFiles(File path, FileFilter filter){

        if(!path.exists()){
            log.error("文件"+path+"不存在!");
            return null;
        }

        File[] files = path.listFiles(filter);

        if(files.length == 0){
            log.info("文件"+path+"下没有待抽取的目标的文件!");
            return null;
        }else{
            for(File file : files){
                if(file.isDirectory()){
                    findAllFiles(file,filter);
                }else{
                    ls_file.add(file);
                    System.out.println(file.getAbsolutePath());
                }
            }
        }
        return ls_file;
    }

    public FileFilter jsonFilter(){
        return new FileFilter() {
            public boolean accept(File path) {
                if(path.isDirectory() || path.getName().endsWith(".json")){
                    if(!path.isDirectory()){
                        File  fileFin = new File(path.getAbsolutePath()+Constant.SUFFIX_SUCCESS);
                        File  fileTmp = new File(path.getAbsolutePath()+ Constant.SUFFIX_TMP);
                        File  fileErr = new File(path.getAbsolutePath()+Constant.SUFFIX_ERROR);
                        if(fileFin.exists() || fileTmp.exists() || fileErr.exists()){
                            return false;
                        }
                    }else if(hasTagFile(path)){
                        return false;
                    }
                    return true;
                }
                return false;
            }
        };
    }

    private boolean hasTagFile(File path){
        File  fileFin = new File(path.getAbsolutePath()+Constant.SUFFIX_SUCCESS);
        File  fileTmp = new File(path.getAbsolutePath()+ Constant.SUFFIX_TMP);
        File  fileErr = new File(path.getAbsolutePath()+Constant.SUFFIX_ERROR);
        if(fileFin.exists() || fileTmp.exists() || fileErr.exists()){
            return true;
        }
        return false;
    }

    public FileFilter dirFilter(){
        return new FileFilter() {
            public boolean accept(File path) {
                if(path.isDirectory() || path.getName().endsWith(".json")) {
                    return true;
                }else {
                    return false;
                }
            }
        };
    }

    public static void deleteTempFile(File file){
        if(file!=null && file.exists()){
            file.delete();
            file.getParentFile().delete();//删除文件夹
        }
    }

    public static void deleteTempFile(List<CallContents> callContents) {
        if(CollectionsUtil.isNotEmpty(callContents)){
            for (CallContents cc : callContents){
                cc.delete();
            }
        }
    }

    public static void deleteTempFile2(String fileName) {
        File file=new File(FileUtils.tempDirPath);
        File[] fi=file.listFiles();
        for(File f : fi){
            if(f.getName().substring(0, 4).equals(fileName)){
                f.delete();
            }
        }
    }

    public static String randomTempPath(String fileName){
        String dirPath = tempDirPath + File.separator + UUID.randomUUID().toString().replaceAll("-","");
        File dir = new File(dirPath);
        if(!dir.exists()) dir.mkdir();
        return dirPath + File.separator + fileName;
    }

}
