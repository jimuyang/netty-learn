package use.jdk;

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
 * Created with IntelliJ IDEA.
 *
 * @Author: muyi-macpro
 * @Time: 2018/4/22 下午2:41
 * @Description:
 */
public class PlainNIOServer {

    public void server(int port) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //配置服务器 serverSocketChannel不阻塞
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        //将服务器绑定到指定的端口
        InetSocketAddress address = new InetSocketAddress(port);
        serverSocket.bind(address);

        //打开selector来处理Channel
        Selector selector = Selector.open();
        //将serverSocketChannel注册到selector以接受连接
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        final ByteBuffer msg = ByteBuffer.wrap("Hi! \r\n".getBytes());

        for (; ; ) {
            try {
                //阻塞直到下一个传入事件
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }

            //获取所有接收事件的SelectionKey实例
            Set<SelectionKey> readyKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                try {
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();

                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate());
                        System.out.println("Accepted connection from " + client);
                    }

                    if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        while (buffer.hasRemaining()) {
                            if (client.write(buffer) == 0) {
                                break;
                            }
                        }
                        client.close();
                    }
                } catch (IOException e) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException ex) {
                        //ignore on close
                    }
                }
            }

        }
    }
}
