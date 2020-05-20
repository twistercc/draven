package com.example.io.bio.me;


import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 本地socket通信服务端
 * Created by wentao.cui on 2020/3/20.
 */

public class TcpServer {
    private static ServerSocket serverSocket;
    private static Socket socket;
    private static final int port = 1234;
    public static final byte[] LOCK = new byte[0];
    public static volatile boolean CLIENLOCK = false;

    public static void startServer(){
        if (serverSocket == null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        serverSocket = new ServerSocket(port);
                        System.out.println("服务器等待连接中");
                        socket = serverSocket.accept();
                        synchronized (LOCK) {
                            CLIENLOCK = true;
                            System.out.println("客户端连接上来了");
                            LOCK.notify();
                        }
                        InputStream inputStream = socket.getInputStream();
                        byte[] buffer = new byte[1024];
                        int len = -1;
                        while ((len = inputStream.read(buffer)) != -1) {
                            String data = new String(buffer, 0, len);
                            System.out.println("收到客户端的数据-----------------------------:" + data);
                            JSONObject rec = JSONObject.parseObject(data);
                        }
                    } catch (IOException e) {
                        System.out.println("failed:"+ e.getMessage());
                        e.printStackTrace();

                    }finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            serverSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        socket = null;
                        serverSocket = null;
                    }
                }
            }).start();
        }
    }

    public static void sendTcpMessage(final String msg){
        if (isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket.getOutputStream().write(msg.getBytes());
                        socket.getOutputStream().flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static boolean isConnected(){
        return socket != null && socket.isConnected();
    }

    public static void main(String[] args) {
        TcpServer.startServer();
    }

}
