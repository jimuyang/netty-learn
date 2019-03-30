package muyi.nonetty.nio;


import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: muyi-macpro
 * @Time: 2018/4/22 下午2:22
 * @Description:
 */
public class PlainOIOServer {

    public void server(int port) throws IOException {

        final ServerSocket socket = new ServerSocket(port);

        try {
            for (;;) {
                final Socket clientSocket = socket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OutputStream outputStream;
                        try {
                            outputStream = clientSocket.getOutputStream();
                            outputStream.write("Hi! \r\n".getBytes(Charset.forName("UTF-8")));
                            outputStream.flush();
                            clientSocket.close();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            try {
                                clientSocket.close();
                            }catch (IOException e) {
                                // ignore on close
                            }
                        }
                    }
                }).start();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        new PlainOIOServer().server(port);
    }





}
