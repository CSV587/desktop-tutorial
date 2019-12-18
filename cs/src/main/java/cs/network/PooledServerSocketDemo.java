package cs.network;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/13.
 * @Description :
 */
//线程池服务器，相较于多线程服务器，这种方式可以限制资源使用，避免同时生成大量线程
public class PooledServerSocketDemo {

    public final static int PORT = 80;

    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(50);

        try(ServerSocket server = new ServerSocket(PORT)) {
            while(true) {
                try {
                    Socket connection = server.accept();
                    Callable<Void> task = new PTask(connection);
                    pool.submit(task);
                } catch (IOException ex){}
            }
        } catch (IOException ex) {
            System.err.println("Couldn't start server.");
        }

    }

    private static class PTask implements Callable<Void> {
        private Socket connection;
        PTask(Socket connection) {
            this.connection = connection;
        }

        @Override
        public Void call() {
            try{
                Writer out = new OutputStreamWriter(connection.getOutputStream());
                Date now = new Date();
                out.write(now.toString()+"----"+"\r\n");
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
            return null;
        }
    }

}
