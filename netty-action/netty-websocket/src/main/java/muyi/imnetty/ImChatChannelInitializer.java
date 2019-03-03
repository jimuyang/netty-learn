package muyi.imnetty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author: Jimu Yang
 * @date: 2019/3/3 6:59 PM
 * @descricption: want more.
 */
public class ImChatChannelInitializer extends ChannelInitializer<Channel> {

    private final ChannelGroup group;

    public ImChatChannelInitializer(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 将字节解码为HttpRequest HttpContent LastHttpContent
        // 将 HttpRequest HttpContent LastHttpContent 编码为字节
        pipeline.addLast(new HttpServerCodec());

        // adds support for writing a large data stream
        // 允许使用ChunkedInput
        pipeline.addLast(new ChunkedWriteHandler());

        // 将一个HttpMessage和跟随它的多个HttpContent聚合为单个FullHttpRequest或者FullHttpResponse
        // 下一个HttpHandler将只会收到完整的HTTP请求或响应
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));

        // 处理FullHttpRequest（不发送到/ws的请求
        pipeline.addLast(new HttpRequestHandler("/ws"));

        // 按照WebSocket规范 处理WebSocket 升级握手 Ping Pong Close 等WebSocketFrame
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        // 处理握手完成事件和文本消息
        pipeline.addLast(new TextWebSocketFrameHandler(group));
    }
}
