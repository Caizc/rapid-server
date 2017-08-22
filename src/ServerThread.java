import java.net.*;

public class ServerThread extends Thread {
    boolean flag = false;
    ServerSocket ss;

    public static void main(String args[]) {
        new ServerThread().start();
    }

    public void run() {
        try {
            ss = new ServerSocket(2001);
            System.out.println("Server Listening on 2001...");
            flag = true;
            new ActionThread().start();

        } catch (Exception e) {
            e.printStackTrace();
        }
        while (flag) {
            try {
                Socket sc = ss.accept();
                new ServerAgentThread(sc).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
