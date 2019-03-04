package muyi.imnetty;

import com.sun.deploy.net.HttpUtils;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author: Jimu Yang
 * @date: 2019/3/3 3:28 PM
 * @descricption: want more.
 * <p>
 * 处理FullHttpRequest
 */
public class HttpRequestHandler
        extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String wsUri;

    private static final File INDEX_FILE;

    static {
        URL location = HttpRequestHandler.class
                .getProtectionDomain().getCodeSource().getLocation();
        try {
            String path = location.toURI() + "index.html";
            System.out.println("index.html path: " + path);
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX_FILE = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("unable to locate index.html", e);
        }
    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (wsUri.equalsIgnoreCase(request.uri())) {
            // 如果请求了wsUri 需要将协议升级到WebSocket
            // 具体做法：增加引用计数（调用retain方法） 并将它传给下一个ChannelInboundHandler
            ctx.fireChannelRead(request.retain());
            return;
        }

        if (HttpUtil.is100ContinueExpected(request)) {
            // 处理100 Continue请求以符合 HTTP1.1规范
            send100Continue(ctx);
        }

        // 读取 index.html
        RandomAccessFile file = new RandomAccessFile(INDEX_FILE, "r");
        // 开始构建HttpResponse
        HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

        boolean keepAlive = HttpUtil.isKeepAlive(request);
        if (keepAlive) {
            // 如果是keep-alive 添加需要的httpHeaders
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.write(response);

        // 这里SslHandler为啥影响了FileChannel的write方式？
        if (ctx.pipeline().get(SslHandler.class) == null) {
            // 如果不需要加密和压缩 那么使用FileRegion的zero-copy特性来进行传输
            ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
        } else {
            ctx.write(new ChunkedNioFile(file.getChannel()));
        }

        // 写入LastHttpContent来标识响应结束
        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        // 不是keep-alive时 需要在写操作完成后关闭channel
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }
}
