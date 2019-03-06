package muyi.netty.demo.echo;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: Jimu Yang
 * @date: 2019/1/12 11:04 PM
 * @descricption: want more.
 * @see io.netty.example.echo.EchoServerHandler
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {


    public EchoServerHandler() {
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.write(msg);
    }

    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
