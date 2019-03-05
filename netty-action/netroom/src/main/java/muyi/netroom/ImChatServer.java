package muyi.netroom;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.net.InetSocketAddress;

/**
 * @author: Jimu Yang
 * @date: 2019/3/4 12:40 AM
 * @descricption: want more.
 */
public class ImChatServer {

    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    private final EventLoopGroup group = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture start(InetSocketAddress address) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(this.buildChannelInitializer(channelGroup));

        // 同步直到bind成功
        ChannelFuture future = serverBootstrap.bind(address);
        future.syncUninterruptibly();

        channel = future.channel();
        return future;
    }

    public void destroy() {
        if (channel != null) channel.close();
        channelGroup.close();
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length < 1) {
            System.out.println("use 8080 as default port");
            port = 8080;
        } else {
            port = Integer.parseInt(args[0]);
        }

        final ImChatServer chatServer = new ImChatServer();
        ChannelFuture future = chatServer.start(new InetSocketAddress(port));

        Runtime.getRuntime().addShutdownHook(new Thread(chatServer::destroy));
        future.channel().closeFuture().syncUninterruptibly();
    }

    protected ChannelInitializer<Channel> buildChannelInitializer(ChannelGroup group) {
        return new ImChatChannelInitializer(group);
    }


}
