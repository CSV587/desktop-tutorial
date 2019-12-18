package cs.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/13.
 * @Description :
 */
//非阻塞nio
public class EchoServer {

    public static int PORT = 80;

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverChannel;
        Selector selector;
        byte[] rotation = new byte[95*2];
        for(byte i = ' '; i <= '~'; i++){ // ' '是ASCII码表的第一个(0)，'~'是倒数第二个(126)
            rotation[i-' '] = i;
            rotation[i+95-' '] = i;
        }
        try{
            //获取通道
            serverChannel = ServerSocketChannel.open();
            ServerSocket ss = serverChannel.socket();
            InetSocketAddress address = new InetSocketAddress(PORT);
            ss.bind(address);
            //设置为非阻塞
            serverChannel.configureBlocking(false);
            //获取Selector选择器
            selector = Selector.open();
            //将通道注册到选择器上,并注册的操作为：“接收”操作
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        //采用轮询的方式，查询获取“准备就绪”的注册过的操作
        while(true){
            try{
                selector.select();
            } catch (IOException ex){
                ex.printStackTrace();
                break;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                //移除选择键,从而不会处理两次
                iterator.remove();
                try{
                    if(key.isAcceptable()){
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        System.out.println("Accepted connection from "+client);
                        client.configureBlocking(false);
                        SelectionKey clientKey = client.register(selector,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        buffer.put(Byte.parseByte("cs loves jy"));
                        //写完后进行flip()操作，将写指针置回0，从而使缓冲区排空后可以继续从开头写入数据
                        buffer.flip();
                        //将缓冲区附加到通道上
                        clientKey.attach(buffer);
                    }
                    else if(key.isReadable()){
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer output = (ByteBuffer) key.attachment();
                        client.read(output);
                    }
                    else if(key.isWritable()){
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer output = (ByteBuffer) key.attachment();
//                        if(!output.hasRemaining()){
//                            //用下一行重新填充缓冲区
//                            output.rewind();
//                            //得到上一次的首字符
//                            int first = output.get();
//                            //准备改变缓冲区中的数据
//                            output.rewind();
//                            //寻找rotation中新的首字符的位置
//                            int position = first - ' ' + 1;
//                            //将数据从rotation复制到缓冲区
//                            output.put(rotation,position,72);
//                            //在缓冲区末尾存储一个行分隔符
//                            output.put((byte)'\r');
//                            output.put((byte)'\n');
//                            //准备缓冲区进行写入
//                            output.flip();
//                        }
                        output.flip();
                        client.write(output);
                        output.compact();
                    }
                }catch (IOException ex){
                    key.cancel();
                    try{
                        key.channel().close();
                    } catch (IOException e){}
                }
            }
        }

//        // 1、获取Selector选择器
//        Selector selector = Selector.open();
//        // 2、获取通道
//        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//        // 3.设置为非阻塞
//        serverSocketChannel.configureBlocking(false);
//        // 4、绑定连接
//        serverSocketChannel.bind(new InetSocketAddress(PORT));
//        // 5、将通道注册到选择器上,并注册的操作为：“接收”操作
//        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
//        // 6、采用轮询的方式，查询获取“准备就绪”的注册过的操作
//        while (selector.select() > 0)
//        {
//            // 7、获取当前选择器中所有注册的选择键（“已经准备就绪的操作”）
//            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
//            while (selectedKeys.hasNext())
//            {
//                // 8、获取“准备就绪”的时间
//                SelectionKey selectedKey = selectedKeys.next();
//                // 9、判断key是具体的什么事件
//                if (selectedKey.isAcceptable())
//                {
//                    // 10、若接受的事件是“接收就绪” 操作,就获取客户端连接
//                    SocketChannel socketChannel = serverSocketChannel.accept();
//                    // 11、切换为非阻塞模式
//                    socketChannel.configureBlocking(false);
//                    // 12、将该通道注册到selector选择器上
//                    socketChannel.register(selector, SelectionKey.OP_READ);
//                }
//                else if (selectedKey.isReadable())
//                {
//                    // 13、获取该选择器上的“读就绪”状态的通道
//                    SocketChannel socketChannel = (SocketChannel) selectedKey.channel();
//                    // 14、读取数据
//                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//                    int length = 0;
//                    while ((length = socketChannel.read(byteBuffer)) != -1)
//                    {
//                        byteBuffer.flip();
//                        System.out.println(new String(byteBuffer.array(), 0, length));
//                        byteBuffer.clear();
//                    }
//                    socketChannel.close();
//                }
//                // 15、移除选择键
//                selectedKeys.remove();
//            }
//        }
//
//        // 7、关闭连接
//        serverSocketChannel.close();
    }

}
