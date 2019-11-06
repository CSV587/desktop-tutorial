package cs.IO;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/22.
 * @Description :
 */
public class ZipAndUnzip {

    //需要压缩的文件夹完整路径
    static String filePath = "C:/Users/cs/Desktop/unzip/";
    //需要压缩的文件夹名
    static String fileName = "unzip";
    //压缩完成后保存为小笨蛋.zip文件，名字随意
    static String outPath = "C:/Users/cs/Desktop/小笨蛋.zip";
    // 需要解压的文件（夹）完整路径
    static String zipPath = "C:/Users/cs/Desktop/大数据.zip";
    // 解压后的文件（夹）完整路径
    static String unzipedFilePath = "C:/Users/cs/Desktop/unzip/";

    public static void main(String[] args) throws Exception {

//        zipFileMethod();
        unzipFileMethod();

    }


    /**
     * 压缩文件或者文件夹
     *
     * @throws Exception
     */
    public static void zipFileMethod() throws Exception {
        OutputStream outputStream = new FileOutputStream(outPath);//创建 压缩文件名.zip文件
        CheckedOutputStream checkedOutputStream = new CheckedOutputStream(outputStream, new CRC32());//检查输出流,采用CRC32算法，保证文件的一致性
        ZipOutputStream zipOutputStream = new ZipOutputStream(checkedOutputStream);//创建zip文件的输出流
//        zipOutputStream.setEncoding("GBK");//设置编码，防止中文乱码
        File file = new File(filePath);//需要压缩的文件或文件夹对象
        ZipFile(zipOutputStream, file);//压缩文件的具体实现函数
        zipOutputStream.close();
        checkedOutputStream.close();
        outputStream.close();
        System.out.println("压缩完成");
    }

    //递归，获取需要压缩的文件夹下面的所有子文件,然后创建对应目录与文件,对文件进行压缩
    public static void ZipFile(ZipOutputStream zipOutputStream, File file) throws Exception {
        if (file.isDirectory()) {
            //创建压缩文件的目录结构
            zipOutputStream.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(fileName)) + File.separator));
            System.out.println(file.getPath().substring(file.getPath().indexOf(fileName))+File.separator);
            for (File f : file.listFiles()) {
                ZipFile(zipOutputStream, f);
            }
        } else {
            //打印输出正在压缩的文件
            System.out.println("正在压缩文件:" + file.getName());
            //创建压缩文件
            zipOutputStream.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(fileName))));
            //用字节方式读取源文件
            InputStream inputStream = new FileInputStream(file.getPath());
            //创建一个缓存区
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            //字节数组,每次读取1024个字节
            byte[] bytes = new byte[1024];
            //循环读取，边读边写
            while (bufferedInputStream.read(bytes) != -1) {
                zipOutputStream.write(bytes); //写入压缩文件
            }
            //关闭流
            bufferedInputStream.close();
            inputStream.close();
        }
    }


    /**
     * 解压缩文件或者文件夹
     *
     * @throws Exception
     */
    public static void unzipFileMethod() throws IOException {
//        ZipFile zipFile = new ZipFile(zipPath, Charset.forName("GBK"));//压缩文件的实列,并设置编码
        ZipFile zipFile = new ZipFile(zipPath);
        //获取压缩文中的所有项
        for (Enumeration<ZipEntry> enumeration = (Enumeration<ZipEntry>) zipFile.entries(); enumeration.hasMoreElements(); ) {
            ZipEntry zipEntry = enumeration.nextElement();//获取元素
            if (zipEntry.isDirectory()) {
                new File(unzipedFilePath + zipEntry.getName()).mkdirs();
            } else {
                System.out.println("正在解压文件:"+zipEntry.getName());//打印输出信息
                OutputStream outputStream = new FileOutputStream(unzipedFilePath+zipEntry.getName());//创建解压后的文件
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);//带缓的写出流
                InputStream inputStream = zipFile.getInputStream(zipEntry);//读取元素
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);//读取流的缓存流
                CheckedInputStream checkedInputStream = new CheckedInputStream(bufferedInputStream, new CRC32());//检查读取流，采用CRC32算法，保证文件的一致性
                byte [] bytes = new byte[1024];//字节数组，每次读取1024个字节
                //循环读取压缩文件的值
                while(checkedInputStream.read(bytes)!=-1)
                {
                    bufferedOutputStream.write(bytes);//写入到新文件
                }
                checkedInputStream.close();
                bufferedInputStream.close();
                inputStream.close();
                bufferedOutputStream.close();
                outputStream.close();
            }
        }
            System.out.println("解压完成");
            zipFile.close();
    }
}

