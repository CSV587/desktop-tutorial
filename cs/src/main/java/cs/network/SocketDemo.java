package cs.network;

import java.io.*;
import java.net.*;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/12.
 * @Description :
 */
public class SocketDemo {

    public static void main(String[] args) throws IOException {

        //socket写入
        Socket socket = new Socket("www.baidu.com",80);
        socket.setSoTimeout(15000);
        OutputStream out = socket.getOutputStream();
        Writer writer = new OutputStreamWriter(out,"UTF-8");
        writer.write("DEFINE eng-lat gold\r\n");
        writer.flush();
        socket.shutdownOutput();

        //socket读出
        InputStream in = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        for(String line = reader.readLine();!line.equals(".");line = reader.readLine()){
            System.out.println(line);
        }
        socket.shutdownInput();

        //查看指定主机上前1024个端口中哪些安装有tcp服务器
        String host = "172.16.18.215";
        for(int i = 1; i < 1024; i++){
            try{
                Socket s = new Socket(host,i);
                System.out.println("There is a server on port "+i+" of "+host);
                s.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                break;
            } catch (IOException ex) {
                System.out.println("This is not a server.");
            }
        }


        //以192.168.144.128为代理服务器，代理访问www.baidu.com
        SocketAddress address = new InetSocketAddress("192.168.144.128",80);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, address);
        Socket s = new Socket(proxy);
        SocketAddress remote = new InetSocketAddress("www.baidu.com",80);
        s.connect(remote);

    }

}
