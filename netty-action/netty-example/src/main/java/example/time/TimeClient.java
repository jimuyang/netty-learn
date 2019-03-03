package example.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: muyi-macpro
 * @datetime: 2018/5/23 上午12:21
 * @description:
 */
public class TimeClient {

    public static void main(String[] args) throws Exception{
        String host = "localhost";
        int port = 8080;

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            // start the client
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            // wait close
            channelFuture.channel().closeFuture().sync();
        } finally {
           workerGroup.shutdownGracefully();
        }
    }
}
