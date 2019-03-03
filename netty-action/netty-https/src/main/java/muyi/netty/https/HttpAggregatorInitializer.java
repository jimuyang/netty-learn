package muyi.netty.https;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Jimu Yang.
 */
public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {
    private final boolean client;

    public HttpAggregatorInitializer(boolean client) {
        this.client = client;
    }


    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();

        if (client) {
            channelPipeline.addLast("codec", new HttpClientCodec());
        } else {
            channelPipeline.addLast("codec", new HttpServerCodec());
        }

        channelPipeline.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
    }
}

