package cs.IO;

import java.io.*;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/22.
 * @Description :
 */
public class Face2Char {

    public static void main(String[] args) throws IOException {
//        useFileReaderReadFile();
        useBufferedReaderReadFile();
    }


    /**
     * FileReader 类读取文件
     */
    public static void useFileReaderReadFile() throws IOException {
        FileReader b = new FileReader("C:/Users/cs/Desktop/设计模式笔记.txt");
        char a[]=new char[1000]; //创建可容纳 1000 个字符的数组
        int num=b.read(a); //将数据读入到数组 a 中，并返回字符数
        String str=new String(a,0,num); //将字符串数组转换成字符串
        System.out.println("读取的字符个数为："+num+"\n内容为：\n" + str);
    }

    /**
     * 使用 FileWrite写入文件
     * @throws IOException
     */
    public static void useFileWriterWriteFile() throws IOException{
        FileWriter fileWriter =new FileWriter("C:/Users/cs/Desktop/设计模式笔记.txt");
        for(int i=0;i<10;i++){
            fileWriter.write("用 FileWriter 写入文字内容\n");
        }
        System.out.println("FileWriter 写入文件成功");
        fileWriter.close();
    }

    /**
     * 使用 BufferedReader 类读取文件
     */
    public static void useBufferedReaderReadFile() throws IOException{
        String OneLine;
        int count = 0;
        try{
//            FileReader fileReader =new FileReader("C:/Users/cs/Desktop/设计模式笔记.txt");
//            BufferedReader bufferedReader =new BufferedReader(fileReader);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("C:/Users/cs/Desktop/设计模式笔记.txt"),"GBK")
            );
            while((OneLine = bufferedReader.readLine())!=null){  //每次读取 1 行
                count++;  //计算读取的行数
                System.out.println(OneLine + "\n");
            }
            System.out.println("\n 共读取了 "+count+" 行");
            bufferedReader.close();
        }
        catch(IOException io){
            System.out.println("出错了!"+io);
        }
    }

    /**
     * 使用 BufferedWriter 写入文件
     * @throws IOException
     */
    public static void useBufferedWriterWriteFile() throws IOException{
        String str;
        BufferedReader in = new
                BufferedReader(new FileReader("/Users/panzhangbao/Desktop/Java/JavaIO/src/pan/ReadMe.txt"));
        BufferedWriter out = new
                BufferedWriter(new FileWriter("/Users/panzhangbao/Desktop/Java/JavaIO/src/pan/WriteMe.txt"));

        while((str=in.readLine())!=null){
            out.write(str);  //将读取到的 1 行数据写入输出流
            System.out.println(str);
        }
        out.flush();
        in.close();
        out.close();
    }


}
