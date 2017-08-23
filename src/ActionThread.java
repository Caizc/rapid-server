/**
 * 动作处理线程
 */
public class ActionThread extends Thread {

    // 线程执行间隔（毫秒）
    static final int SLEEP = 5;

    boolean flag = true;

    public void run() {

        while (flag) {

            Action action;

            // 同步地从动作队列中取出一个动作指令
            synchronized (AgentThread.lock) {
                action = AgentThread.actionQueue.poll();
            }

            if (action != null) {

                // 执行动作
                action.doAction();

                // 向所有客户端广播数据状态
                AgentThread.broadcastState();
            }

            try {
                // 等待一段间隔，再继续本线程
                Thread.sleep(SLEEP);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
