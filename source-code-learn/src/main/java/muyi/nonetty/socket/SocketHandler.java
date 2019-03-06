package muyi.nonetty.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @author: Jimu Yang
 * @date: 2019/1/13 10:48 PM
 * @descricption: want more.
 */
public class SocketHandler {
    public static final int MAX_DATA_LENGTH = 1024;

    private final Socket socket;

    public SocketHandler(Socket socket) {
        this.socket = socket;
    }

    public void start() {
        System.out.println("新SOCKET接入");
        new Thread(new Runnable() {
            @Override
            public void run() {
                doStart();
            }
        }).start();
    }

    private void doStart() {
        try {
            InputStream inputStream = socket.getInputStream();
//            socket.setSoTimeout(100);
            while (true) {
                byte[] data = new byte[MAX_DATA_LENGTH];
                int len;
                while ((len = inputStream.read(data)) != -1) {
                    String message = new String(data, 0, len);
                    System.out.println("收到客户端消息：" + message);
                    socket.getOutputStream().write(data);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
