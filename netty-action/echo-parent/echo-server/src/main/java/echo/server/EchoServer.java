package echo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: muyi-macpro
 * @Time: 2018/4/14 下午4:14
 * @Description:
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println(
                    "Usage: " + EchoServer.class.getSimpleName() +
                            "<port>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    public void start() throws Exception {

        final EchoServerHandler serverHandler = new EchoServerHandler();

        // 创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            // 创建一个ServerBootstrap
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 指定所使用的NIO传输channel
            // 使用指定的端口创建套接字
            // 添加一个EchoServerHandler到channel的pipeline
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);
                        }
                    });

            // 异步的绑定服务器，但调用sync()阻塞直到绑定完成
            ChannelFuture channelFuture = bootstrap.bind().sync();

            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }



    }







}
