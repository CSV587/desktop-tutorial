package cs.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/13.
 * @Description :
 */
public class SimpleServerSocketDemo {

    public static void main(String[] args) throws IOException {

        //创建一个监听80端口的服务器Socket
        ServerSocket server = new ServerSocket(80);
        //同步阻塞接收socket连接
        while(true) {
            try(Socket connection = server.accept()) {
                Writer writer = new OutputStreamWriter(connection.getOutputStream());
                Date now = new Date();
                writer.write(now.toString()+"\r\n");
                writer.flush();
                writer.close();
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }



    }

}
