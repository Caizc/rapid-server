public class ActionThread extends Thread {
    static final int SLEEP = 5;
    boolean flag = true;

    public void run() {
        while (flag) {
            Action a = null;
            synchronized (ServerAgentThread.lock) {
                a = ServerAgentThread.aq.poll();
            }
            if (a != null) {
                a.doAction();
                ServerAgentThread.broadcastState();
            }
            try {
                Thread.sleep(SLEEP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
