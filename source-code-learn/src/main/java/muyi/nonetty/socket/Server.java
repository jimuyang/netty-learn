package muyi.nonetty.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: Jimu Yang
 * @date: 2019/1/13 10:14 PM
 * @descricption: want more.
 */
public class Server {

    private ServerSocket serverSocket;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("服务端启动成功，端口号：" + port);
        } catch (IOException ioe) {
            System.out.println("服务端启动失败");
        }
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                doStart();
            }
        }).start();
    }

    private void doStart() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                // new Thread去处理新接入的socket 防止阻塞新socket accept
                new SocketHandler(socket).start();
            }catch (IOException ioe) {
                System.out.println("服务端异常");
            }
        }
    }

}
