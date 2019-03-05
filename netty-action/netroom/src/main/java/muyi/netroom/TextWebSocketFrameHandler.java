package muyi.netroom;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @author: Jimu Yang
 * @date: 2019/3/3 4:08 PM
 * @descricption: want more.
 * <p>
 * 处理WebSocket TextFrame
 */
public class TextWebSocketFrameHandler
        extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 增加消息的引用计数 并发送至group内
        /**
         * 为什么要调用retain方法？
         * channelRead0 和 writeAndFlush 都是异步的
         * 因此：可能channelRead0先返回了 msg引用计数减少 之后writeAndFlush不能访问一个已经失效的引用
         */
        group.writeAndFlush(msg.retain());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        super.userEventTriggered(ctx, evt);
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // 握手成功事件
            System.out.println("handshake complete.");
            // 握手成功后 协议已升级 不会再收到HTTP消息了
            ctx.pipeline().remove(HttpRequestHandler.class);

            // 通知group内所有的clientChannel
            group.writeAndFlush(new TextWebSocketFrame("client:" + ctx.channel() + " joined"));
            group.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
