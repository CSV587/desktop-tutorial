package cs.network;

import java.io.*;
import java.net.HttpURLConnection;
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
        try(InputStream in = urlConnection.getInputStream()) {
            InputStream buffer = new BufferedInputStream(in);
            Reader reader = new InputStreamReader(buffer);
            int c;
            while((c = reader.read()) != -1){
                System.out.print((char) c);
            }
        }
        System.out.println("内容长度为:" + urlConnection.getContentLength());
        System.out.println("内容编码为:" + urlConnection.getContentEncoding());
        System.out.println("发送时间为:" + urlConnection.getDate());
        System.out.println("过期时间为:" + urlConnection.getExpiration());//为0表示永不过期
        System.out.println("最后修改时间为:" + urlConnection.getLastModified());
        System.out.println("查看指定头信息:" + urlConnection.getHeaderField("content-type"));
        System.out.println("查看指定第n个头信息的key值:" + urlConnection.getHeaderFieldKey(6));
        System.out.println("查看指定第n个头信息的value值:" + urlConnection.getHeaderField(6));
        for(int i = 1; i < 12; i++){
            System.out.println(urlConnection.getHeaderFieldKey(i)+":"+urlConnection.getHeaderField(i));
        }

        //向服务器写回信息
        System.out.println(urlConnection.getDoOutput());
        if(true == urlConnection.getDoOutput()){
            OutputStream raw = urlConnection.getOutputStream();
            OutputStream buffer = new BufferedOutputStream(raw);
            OutputStreamWriter out = new OutputStreamWriter(buffer,"8859_1");
            out.write("first=Franke&middle=&last=James&work=String+Quartet\r\n");
            out.flush();
            out.close();
        }

        HttpURLConnection http = (HttpURLConnection) urlConnection;
        System.out.println("Permission:"+http.getPermission());

    }

}
