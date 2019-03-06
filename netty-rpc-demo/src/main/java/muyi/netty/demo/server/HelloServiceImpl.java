package muyi.netty.demo.server;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Jimu Yang.
 */
public class HelloServiceImpl implements HelloService {

    public String hello(String msg) {
        return "Hello, " + msg;
    }
}
