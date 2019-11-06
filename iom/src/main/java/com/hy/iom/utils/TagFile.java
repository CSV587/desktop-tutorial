package com.hy.iom.utils;

import java.io.File;
import java.io.IOException;

public class TagFile {
    public static final String SUFFIX_SUCCESS = Constant.SUFFIX_SUCCESS;
    public static final String SUFFIX_ERROR = Constant.SUFFIX_ERROR;
    /**
     * 创建已解析文件的标识文件
     * @param file
     * @param toParent
     *  是否是在父级目录创建
     * @throws java.io.IOException
     */
    public static void createEndFlag(String file,boolean toParent) {
        createEndFlag(file, toParent, false);
    }

    public static void createEndFlag(String file,boolean toParent,boolean success) {
        String suffix = Constant.SUFFIX_SUCCESS;
        if(!success){
            suffix = Constant.SUFFIX_ERROR;
        }
        createFlagFile(file,toParent,suffix);
    }

    public static void createEndFlag(String file) {
        createEndFlag(file, false);
    }

    public static void createFlagToParent(String file) {
        createEndFlag(file, true);
    }

    public static String createStartFlag(String file,boolean toParent){
       return createFlagFile(file,toParent,Constant.SUFFIX_TMP);
    }

    public static void renameToEnd(String sourceFile,String suffix){
        if(sourceFile != null && !"".equals(sourceFile)){
            String targetFile = sourceFile.substring(0,sourceFile.lastIndexOf(Constant.SUFFIX_TMP))+suffix;
            new File(sourceFile).renameTo(new File(targetFile));
        }

    }

    public static void renameToEnd(String sourceFile,boolean success){
        if(success){
            renameToEnd(sourceFile,Constant.SUFFIX_SUCCESS);
        }else{
            renameToEnd(sourceFile,Constant.SUFFIX_ERROR);
        }
    }

    protected static String createFlagFile(String file,boolean toParent,String suffix){
        String filePath = getFilePath(file,toParent)+suffix;
        File flagFile = new File(filePath);
        if(!flagFile.exists()){
            try {
                flagFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            return null;
        }
        return filePath;
    }

    private static String getFilePath(String file,boolean toParent){
        if(toParent){
            return new File(file).getParent();
        }else{
            return new File(file).getAbsolutePath();
        }
    }

}
