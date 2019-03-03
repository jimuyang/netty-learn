package example.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: muyi-macpro
 * @datetime: 2018/5/23 上午12:30
 * @description:
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private ByteBuf buf;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        buf = ctx.alloc().buffer(4);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        buf.release();
        buf = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf m = (ByteBuf) msg; // (1)
        buf.writeBytes(m);
        m.release();

        if (buf.readableBytes() >= 4) { // (3)
            long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        }

//        try {
//            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
//            System.out.println("receive time:" + new Date(currentTimeMillis));
//            ctx.close();
//        } finally {
//            m.release();
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}