package com.example.io.bio.local2;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 设置发送指定长度的字节数据
 * Created by wentao.cui on 2020/3/20.
 */

public class SocketClient {
    public static Socket socket;
    private static final int port = 1234;

    public static void startClient(){
        if (socket == null) {
            new Thread(() -> {
                try {
                    System.out.println("启动客户端");
                    socket = new Socket(InetAddress.getLocalHost(), port);
                    System.out.println("客户端连接成功");
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());
                    InputStream inputStream = socket.getInputStream();
                    byte[] buffer = new byte[512];
                    inputStream.read(buffer, 0, 512);
                    String data = new String(buffer);
                    System.out.println("收到服务器的数据------------:" + data);
                    System.out.println("客户端断开连接");
                    pw.close();
                } catch (Exception EE) {
                    EE.printStackTrace();
                    System.out.println("客户端无法连接服务器");
                }finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    socket = null;
                }
            }).start();
        }
    }

    public static void sendTcpMessage(final String msg){
        if (socket != null && socket.isConnected()) {
            new Thread(() -> {
                try {
                    socket.getOutputStream().write(msg.getBytes());
                    socket.getOutputStream().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        SocketClient.startClient();
    }

}
