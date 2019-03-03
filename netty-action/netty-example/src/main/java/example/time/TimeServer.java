package example.time;

import example.NettyNioServer;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: muyi-macpro
 * @datetime: 2018/5/23 上午12:17
 * @description:
 */
public class TimeServer {

    public static void main(String[] args) throws Exception{
        new NettyNioServer(8080, new TimeServerHandler()).run();
    }
}
