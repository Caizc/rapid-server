package com.caizicong.network;

import java.util.*;
import java.io.*;
import java.net.*;

/**
 * 服务端代理线程
 */
public class AgentThread extends Thread {

    // 客户端连接数
    private static int clientCount = 0;

    // 0: 红色；1: 蓝色
    int redOrBlue = 0;

    static float rd;
    static float rs;
    static float bd;
    static float bs;
    static float rx;
    static float ry;
    static float rz;
    static float rRotx;
    static float rRoty;
    static float rRotz;
    static float bx;
    static float by;
    static float bz;
    static float bRotx;
    static float bRoty;
    static float bRotz;
    static int rjump;
    static int bjump;

    // 同步锁使用的对象
    static Object lock = new Object();
    // 客户端连接列表
    static List<AgentThread> clientList = new ArrayList<>();
    // 动作队列
    static Queue<Action> actionQueue = new LinkedList<Action>();

    // 传入的客户端 Socket 连接
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    boolean flag = true;

    //static int iii=0;

    /**
     * 构造方法
     *
     * @param socket 客户端 Socket 连接
     */
    public AgentThread(Socket socket) {

        this.socket = socket;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 向所有客户端广播状态数据
     */
    public static void broadcastState() {

        // 遍历客户端连接列表，向所有客户端发送状态数据
        for (AgentThread agentThread : clientList) {

            try {

                byte[] brd = ByteUtils.float2ByteArray(rd);
                byte[] brs = ByteUtils.float2ByteArray(rs);
                byte[] bbd = ByteUtils.float2ByteArray(bd);
                byte[] bbs = ByteUtils.float2ByteArray(bs);
                byte[] brx = ByteUtils.float2ByteArray(rx);
                byte[] bry = ByteUtils.float2ByteArray(ry);
                byte[] brz = ByteUtils.float2ByteArray(rz);
                byte[] brrx = ByteUtils.float2ByteArray(rRotx);
                byte[] brry = ByteUtils.float2ByteArray(rRoty);
                byte[] brrz = ByteUtils.float2ByteArray(rRotz);
                byte[] bbx = ByteUtils.float2ByteArray(bx);
                byte[] bby = ByteUtils.float2ByteArray(by);
                byte[] bbz = ByteUtils.float2ByteArray(bz);
                byte[] bbrx = ByteUtils.float2ByteArray(bRotx);
                byte[] bbry = ByteUtils.float2ByteArray(bRoty);
                byte[] bbrz = ByteUtils.float2ByteArray(bRotz);
                byte[] brjump = ByteUtils.int2ByteArray(rjump);
                byte[] bbjump = ByteUtils.int2ByteArray(bjump);

                byte[] bytes = {
                        brd[0], brd[1], brd[2], brd[3],
                        brs[0], brs[1], brs[2], brs[3],
                        bbd[0], bbd[1], bbd[2], bbd[3],
                        bbs[0], bbs[1], bbs[2], bbs[3],
                        brx[0], brx[1], brx[2], brx[3],
                        bry[0], bry[1], bry[2], bry[3],
                        brz[0], brz[1], brz[2], brz[3],
                        brrx[0], brrx[1], brrx[2], brrx[3],
                        brry[0], brry[1], brry[2], brry[3],
                        brrz[0], brrz[1], brrz[2], brrz[3],
                        bbx[0], bbx[1], bbx[2], bbx[3],
                        bby[0], bby[1], bby[2], bby[3],
                        bbz[0], bbz[1], bbz[2], bbz[3],
                        bbrx[0], bbrx[1], bbrx[2], bbrx[3],
                        bbry[0], bbry[1], bbry[2], bbry[3],
                        bbrz[0], bbrz[1], bbrz[2], bbrz[3],
                        brjump[0], brjump[1], brjump[2], brjump[3],
                        bbjump[0], bbjump[1], bbjump[2], bbjump[3]
                };

                synchronized (lock) {

                    byte[] byetArrayLength = ByteUtils.int2ByteArray(bytes.length);

                    // 先发送字节数据的长度
                    agentThread.dataOutputStream.write(byetArrayLength);
                    // 再发送字节数据内容
                    agentThread.dataOutputStream.write(bytes, 0, bytes.length);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void run() {

        while (flag) {

            try {

                // TODO: 与客户端连接断开后的异常处理还没做，思路是通过发送心跳包来检测连接的存活状态
                if (socket.isClosed()) {
                    System.out.println("One of the connections has been closed!");
                    break;
                }

                if (dataInputStream.available() == 0) {
                    continue;
                }

                // 前 4 个字节定义了数据包的长度
                byte[] byteslen = new byte[4];
                dataInputStream.read(byteslen, 0, byteslen.length);
                int length = ByteUtils.byteArray2Int(byteslen);

                // 根据数据包的长度读取输入流
                byte[] bytes = new byte[length];
                int count = 0;
                while (count < length) {
                    int templen = dataInputStream.read(bytes);
                    count += templen;
                }

                // 拆分数据包
                splitPackage(bytes);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {

            // 关闭数据流和 Socket 连接
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();

            clientCount--;
            clientList.remove(this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 拆分数据包
     *
     * @param bytes 字节数组
     */
    public void splitPackage(byte[] bytes) {

        // 获取数据包长度
        int packageLength = bytes.length;

        if (packageLength == 0) {
            return;
        }

        // 如果数据包长度为 11，表示是连接请求消息
        if (packageLength == 11) {

            if (clientCount == 0) {

                // 分配当前客户端操控红色单位
                redOrBlue = 0;

            } else if (clientCount == 1) {

                // 分配当前客户端操控蓝色单位
                redOrBlue = 1;

            } else {

                System.out.println("The room is full!");

                return;
            }

            clientList.add(this);

            // 分配玩家单位指令的长度为 4 个字节
            byte[] assignCommandLength = ByteUtils.int2ByteArray(4);
            // 分配玩家单位指令
            byte[] assignCommand = ByteUtils.int2ByteArray(redOrBlue);
            // 指令包
            byte[] commandPackage = {assignCommandLength[0], assignCommandLength[1], assignCommandLength[2], assignCommandLength[3],
                    assignCommand[0], assignCommand[1], assignCommand[2], assignCommand[3]};

            try {
                // 向客户端发送分配玩家单位指令包
                this.dataOutputStream.write(commandPackage, 0, commandPackage.length);

                clientCount++;

                System.out.println(clientCount + " player online.");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (packageLength == 72 && clientCount >= 2) {

            // 如果数据包长度为 72，表示是操控和状态数据指令

            byte[] brx = {bytes[0], bytes[1], bytes[2], bytes[3]};
            byte[] bry = {bytes[4], bytes[5], bytes[6], bytes[7]};
            byte[] bbx = {bytes[8], bytes[9], bytes[10], bytes[11]};
            byte[] bby = {bytes[12], bytes[13], bytes[14], bytes[15]};
            byte[] rx = {bytes[16], bytes[17], bytes[18], bytes[19]};
            byte[] ry = {bytes[20], bytes[21], bytes[22], bytes[23]};
            byte[] rz = {bytes[24], bytes[25], bytes[26], bytes[27]};
            byte[] rRotx = {bytes[28], bytes[29], bytes[30], bytes[31]};
            byte[] rRoty = {bytes[32], bytes[33], bytes[34], bytes[35]};
            byte[] rRotz = {bytes[36], bytes[37], bytes[38], bytes[39]};
            byte[] bx = {bytes[40], bytes[41], bytes[42], bytes[43]};
            byte[] by = {bytes[44], bytes[45], bytes[46], bytes[47]};
            byte[] bz = {bytes[48], bytes[49], bytes[50], bytes[51]};
            byte[] bRotx = {bytes[52], bytes[53], bytes[54], bytes[55]};
            byte[] bRoty = {bytes[56], bytes[57], bytes[58], bytes[59]};
            byte[] bRotz = {bytes[60], bytes[61], bytes[62], bytes[63]};
            byte[] brjump = {bytes[64], bytes[65], bytes[66], bytes[67]};
            byte[] bbjump = {bytes[68], bytes[69], bytes[70], bytes[71]};

            Action action = new Action(
                    this.redOrBlue,
                    ByteUtils.byteArray2Float(brx),
                    ByteUtils.byteArray2Float(bry),
                    ByteUtils.byteArray2Float(bbx),
                    ByteUtils.byteArray2Float(bby),
                    ByteUtils.byteArray2Float(rx),
                    ByteUtils.byteArray2Float(ry),
                    ByteUtils.byteArray2Float(rz),
                    ByteUtils.byteArray2Float(rRotx),
                    ByteUtils.byteArray2Float(rRoty),
                    ByteUtils.byteArray2Float(rRotz),
                    ByteUtils.byteArray2Float(bx),
                    ByteUtils.byteArray2Float(by),
                    ByteUtils.byteArray2Float(bz),
                    ByteUtils.byteArray2Float(bRotx),
                    ByteUtils.byteArray2Float(bRoty),
                    ByteUtils.byteArray2Float(bRotz),
                    ByteUtils.byteArray2Int(brjump),
                    ByteUtils.byteArray2Int(bbjump)
            );

            //System.out.println(ByteUtils.byteArray2Float(rx)+" "+ByteUtils.byteArray2Float(ry)+" "+ByteUtils.byteArray2Float(rz));

            // 同步地往动作队列中添加新的动作
            synchronized (lock) {
                actionQueue.offer(action);
            }

        } else {

            byte[] sidBytes = {bytes[0]};
            byte[] pidBytes = {bytes[1]};
            byte[] sqidBytes = {bytes[2]};

            int serviceId = ByteUtils.byteArray2Int(sidBytes);
            int protocolId = ByteUtils.byteArray2Int(pidBytes);
            int sequenceId = ByteUtils.byteArray2Int(sqidBytes);

            System.out.println(serviceId + " : " + protocolId + " : " + sequenceId);
        }

    }
}