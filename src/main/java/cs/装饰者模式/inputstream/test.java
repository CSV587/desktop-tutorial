package cs.装饰者模式.inputstream;

import java.io.*;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/26.
 * @Description :
 */
public class test {

    public static void main(String[] arg) throws IOException {
        int c;
        try{
            InputStream in =
                    new LowerCaseInputStream(
                        new BufferedInputStream(
                                new FileInputStream("C:/Users/cs/Desktop/test.txt")
                        )
            );
            while((c = in.read()) >= 0){
                System.out.print((char)c);
            }
            System.out.println();
            in.close();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
              System.out.print("All Done!");
        }
    }

}
