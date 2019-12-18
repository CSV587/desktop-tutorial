package cs.network;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/13.
 * @Description :
 */
//多线程服务器，可实现异步非阻塞接收请求
public class MultiThreadServerSocketDemo {

    public final static int PORT = 80;

    public static void main(String[] args) {

        try(ServerSocket server = new ServerSocket(PORT)) {
            while(true) {
                try {
                    Socket connection = server.accept();
                    Thread task = new MThread(connection);
                    task.start();
                } catch (IOException ex){}
            }
        } catch (IOException ex) {
            System.err.println("Couldn't start server.");
        }

    }

    private static class MThread extends Thread {
        private Socket connection;
        MThread(Socket connection) {
            this.connection = connection;
        }
        @Override
        public void run() {
            try{
                Writer out = new OutputStreamWriter(connection.getOutputStream());
                Date now = new Date();
                out.write(now.toString()+"\r\n");
                out.flush();
            } catch (IOException ex){
                System.err.println(ex.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }

}
