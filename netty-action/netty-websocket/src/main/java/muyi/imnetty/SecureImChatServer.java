package muyi.imnetty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.net.InetSocketAddress;

/**
 * @author: Jimu Yang
 * @date: 2019/3/5 9:56 PM
 * @descricption: want more.
 */
public class SecureImChatServer extends ImChatServer {

    private final SslContext sslContext;

    public SecureImChatServer(SslContext sslContext) {
        this.sslContext = sslContext;
    }

    @Override
    protected ChannelInitializer<Channel> buildChannelInitializer(ChannelGroup group) {
//        return super.buildChannelInitializer(group);
        return new SecureImChatChannelInitializer(group, sslContext);
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length < 1) {
            System.out.println("use 8080 as default port");
            port = 8080;
        } else {
            port = Integer.parseInt(args[0]);
        }

        SelfSignedCertificate cert = new SelfSignedCertificate();
//        SslContext sslContext = SslContext.newServerContext(cert.certificate(), cert.privateKey());
        SslContext sslContext = SslContextBuilder.forServer(cert.certificate(), cert.privateKey()).build();

        final SecureImChatServer server = new SecureImChatServer(sslContext);
        ChannelFuture future = server.start(new InetSocketAddress(port));
        Runtime.getRuntime().addShutdownHook(new Thread(server::destroy));
        future.channel().closeFuture().syncUninterruptibly();
    }
}
