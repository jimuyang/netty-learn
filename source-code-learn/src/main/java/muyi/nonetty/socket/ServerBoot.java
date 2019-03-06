package muyi.nonetty.socket;

/**
 * @author: Jimu Yang
 * @date: 2019/1/13 10:55 PM
 * @descricption: want more.
 */
public class ServerBoot {

    private static final int PORT = 8000;

    public static void main(String[] args) {
        Server server = new Server(PORT);
        server.start();
    }


}
