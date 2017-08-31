package com.caizicong.network;

import java.net.*;

/**
 * 服务端连接监听线程
 */
public class ServerThread extends Thread {

    ServerSocket serverSocket = null;

    // 服务端 Socket 是否就绪
    boolean isServerSocketReady = false;

    // 监听端口号
    int port = 2001;

    /**
     * 服务端程序入口
     * @param args
     */
    public static void startup() {

        // 启动服务端连接监听线程
        new ServerThread().start();

    }

    public void run() {

        try {

            // 创建服务端 Socket 并监听 port 端口
            serverSocket = new ServerSocket(port);

            System.out.println("Server Listening on " + port + "...");

            isServerSocketReady = true;

            new ActionThread().start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 循环接收客户端发来的请求，并移交给 AgentThread 处理
        while (isServerSocketReady) {
            try {

                // 等待新的 Socket 连接
                Socket connection = serverSocket.accept();

                // 将新的 Socket 连接移交给 AgentThread 处理
//                new AgentThread(connection).start();

                new ProtobufThread(connection).start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
