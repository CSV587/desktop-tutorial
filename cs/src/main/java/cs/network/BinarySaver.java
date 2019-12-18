package cs.network;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.MalformedInputException;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/23.
 * @Description :
 */

//从web网站下载二进制文件并保存至硬盘
public class BinarySaver {

    public static void main(String[] args){
        String[] urls = {"https://www.baidu.com/"};
        for(int i = 0;i < urls.length;i++){
            try{
                URL root = new URL(urls[i]);
                saveBinaryFile(root);
            } catch (MalformedInputException e){
                System.out.println(urls[i]+" is not URL I understand.");
            } catch (IOException ex){
                System.err.println(ex);
            }
        }
    }

    private static void saveBinaryFile(URL u) throws IOException {

        URLConnection urlConnection = u.openConnection();
        String contentType = urlConnection.getContentType();
        int contentLength = urlConnection.getContentLength();
//        if(contentType.startsWith("text/") || contentLength == -1){
//            throw new IOException("This is not a binary file.");
//        }

        try(InputStream raw = urlConnection.getInputStream()){
            InputStream in = new BufferedInputStream(raw);
            byte[] data = new byte[contentLength];
            int off = 0;
            while(off < contentLength){
                int bytesRead = in.read(data,off,data.length - off);
                if(bytesRead == -1) break;
                off += bytesRead;
            }
            if(off != contentLength){
                throw new IOException("Only read "+off+" bytes;Expected "+contentType+" bytes");
            }
            String fileName = "Baidu.txt";
//            String fileName = u.getFile();
//            fileName = fileName.substring(fileName.lastIndexOf("/")+1);
            try(FileOutputStream out = new FileOutputStream("C:/Users/cs/Desktop/"+fileName)){
                out.write(data);
                out.flush();
            }
        }

    }

}
