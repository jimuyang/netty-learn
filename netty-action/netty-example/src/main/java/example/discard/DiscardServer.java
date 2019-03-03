package example.discard;

import example.NettyNioServer;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: muyi-macpro
 * @datetime: 2018/5/19 下午4:26
 * @description: Discards any incoming data.
 */
public class DiscardServer {

    public static void main(String[] args) throws Exception {

        new NettyNioServer(8080, new DiscardServerHandler()).run();
    }

}