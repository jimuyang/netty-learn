package muyi.netty.demo.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Jimu Yang.
 */
public class HelloServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 如果符合约定，则调用本地方法，返回数据
        if (msg.toString().startsWith("HelloService#")) {
            String result = new HelloServiceImpl().hello(msg.toString().replace("HelloService#", ""));
            ctx.writeAndFlush(result);
        }
    }
}
