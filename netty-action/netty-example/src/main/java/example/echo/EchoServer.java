package example.echo;

import example.NettyNioServer;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: muyi-macpro
 * @datetime: 2018/5/19 下午5:55
 * @description:
 */
public class EchoServer {

    public static void main(String[] args) throws Exception {
        new NettyNioServer(8080, new EchoServerHandler()).run();
    }

}



