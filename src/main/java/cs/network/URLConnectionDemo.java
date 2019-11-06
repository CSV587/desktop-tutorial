package cs.network;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/23.
 * @Description :
 */
public class URLConnectionDemo {

    public static void main(String[] args) throws IOException {

        URL url = new URL("https://www.baidu.com/");
        URLConnection urlConnection = url.openConnection();
        try(InputStream in = urlConnection.getInputStream()){
            InputStream buffer = new BufferedInputStream(in);
            Reader reader = new InputStreamReader(buffer);
            int c;
            while((c = reader.read()) != -1){
                System.out.print((char) c);
            }
        }
        System.out.println("内容长度为:" + urlConnection.getContentLength());

    }

}
