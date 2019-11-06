package cs.加密;

import java.io.*;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/10.
 * @Description :
 */
public class test {

    public static void main(String[] args) throws IOException {

//        File file= new File("C:/Users/cs/Desktop/Let It Go.mp3");
//        Reader r = new FileReader(file);
//        char[] chars = new char[(int) file.length()];
//        int len,temp,i = 0;
//        while((temp = r.read()) != -1) {//每次只能读取一个字符，通过循环的方式放入到字符数组中
//            chars[i++] = (char)temp;
//        }
//        len = r.read(chars);
//        FileOutputStream fos = new FileOutputStream("C:/Users/cs/Desktop/zzz/hhh.txt");
//        fos.write(bytes2);
//        fos.close();

        //加密
        File file= new File("C:/Users/cs/Desktop/小情歌.mp3");    //filename为 文件目录，请自行设置
        FileInputStream in= new FileInputStream(file);  //真正要用到的是FileInputStream类的read()方法
        byte[] bytes= new byte[in.available()];    //in.available()是得到文件的字节数
        in.read(bytes);    //把文件的字节一个一个地填到bytes数组中
        in.close();    //记得要关闭in
        byte[] bytes2 = AESUtil.encrypt2(bytes,"0510");
        FileOutputStream fos = new FileOutputStream("C:/Users/cs/Desktop/hhh.txt");
        fos.write(bytes2);
        fos.close();

        //解密
//        File file= new File("C:/Users/cs/Desktop/zzz/hhh.txt");    //filename为 文件目录，请自行设置
//        InputStream in= new FileInputStream(file);  //真正要用到的是FileInputStream类的read()方法
//        byte[] bytes= new byte[in.available()];    //in.available()是得到文件的字节数
//        in.read(bytes);    //把文件的字节一个一个地填到bytes数组中
//        in.close();    //记得要关闭in
//        byte[] bytes2 = AESUtil.decrypt2(bytes,"0510");
//        FileOutputStream fos = new FileOutputStream("C:/Users/cs/Desktop/zzz/xxx.mp3");
//        fos.write(bytes2);
//        fos.close();

    }

}