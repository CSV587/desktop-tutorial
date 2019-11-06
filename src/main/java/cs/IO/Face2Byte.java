package cs.IO;

import java.io.*;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/22.
 * @Description :
 */
public class Face2Byte {

    public static void main(String[] args) throws IOException {
        useInputStream();
    }

    /**
     *  使用 InputStream 读取数据
     *  好处：节省空间
     * @throws IOException
     */
    public static void useInputStream() throws IOException {
        File readFile = new File("C:/Users/cs/Desktop/设计模式笔记.txt");
        InputStream inputStream = new FileInputStream(readFile);
        byte[] b = new byte[(int)readFile.length()];
        System.err.println("文档内容长度为："+readFile.length());

        // 1.1 全字节读取
        inputStream.read(b);
        inputStream.close();
        System.err.println(new String(b));

        // 1.2 逐字节读取
//        int count = 0;
//        int temp = 0;
//        // 注意：当读到文件末尾的时候会返回-1.正常情况下是不会返回-1的。
//        while((temp = inputStream.read()) != -1){
//            b[count++] = (byte)temp;
//        }
//        inputStream.close();
//        System.err.println(new String(b));
    }

    /**
     * 使用 OutputStream 写入数据
     * @throws IOException
     */
    public static void useOutputStream() throws IOException{

        File writeFile = new File("/Users/panzhangbao/Desktop/Java/JavaIO/src/pan/WriteMe.txt");
        OutputStream outputStream = new FileOutputStream(writeFile);
        // 开启添加模式
//        OutputStream outputStream = new FileOutputStream(writeFile, true);
        byte[] b2 = "DAXIE,xiaoxie大哥，恭喜你，终于把我给打印出来啦，我是神屌丝，继续努力哦\n".getBytes();
        outputStream.write(b2);
        int temp = 0;
        // 下面是逐字节写入
        for (int i = 0; i < b2.length; i++) {
            outputStream.write(b2[i]);
            // 大写转换为小写
            outputStream.write(Character.toLowerCase(b2[i]));
        }
        outputStream.close();
    }

}
