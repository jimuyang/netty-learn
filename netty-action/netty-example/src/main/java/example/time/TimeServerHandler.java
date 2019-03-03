package example.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: muyi-macpro
 * @datetime: 2018/5/22 下午11:16
 * @description:
 */
@ChannelHandler.Sharable
public class TimeServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * the channelActive() method will be invoked
     * when a connection is established and ready to generate traffic.
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //Get the current ByteBufAllocator
        //via ChannelHandlerContext.alloc() and allocate a new buffer.
        System.out.println("received a new time request:");
        final ByteBuf time = ctx.alloc().buffer(4);
        int nowInt = (int) (System.currentTimeMillis() / 1000L + 2208988800L);
        time.writeInt(nowInt);
        System.out.println("return back time: " + nowInt);

        final ChannelFuture channelFuture = ctx.writeAndFlush(time);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                assert channelFuture == future;
                ctx.close();
            }
        });
        //也可以简单的写成：
        //channelFuture.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
