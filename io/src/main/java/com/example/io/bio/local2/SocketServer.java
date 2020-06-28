package com.example.io.bio.local2;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javafx.collections.ArrayChangeListener;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 本地socket通信服务端
 * Created by wentao.cui on 2020/3/20.
 */

public class SocketServer {
    private static ServerSocket serverSocket;
    private static Socket socket;
    private static final int port = 1234;
    public static final byte[] LOCK = new byte[0];
    public static volatile boolean CLIENLOCK = false;

    public static void startServer(){
        if (serverSocket == null){
            new Thread(() -> {
                try {
                    serverSocket = new ServerSocket(port);
                    System.out.println("服务器等待连接中");
                    socket = serverSocket.accept();
                    synchronized (LOCK) {
                        CLIENLOCK = true;
                        System.out.println("客户端连接上来了");
                        LOCK.notify();
                    }

                    // 服务端发送消息
                    if(SocketServer.isConnected()){
                        JSONObject msg = new JSONObject();
                        msg.put("cmd", "update");
                        JSONObject info = new JSONObject();
                        info.put("version", 1); // 版本号
                        info.put("path", "/&&&");
                        msg.put("info", info);
                        sendTcpMessage(JSON.toJSONString(msg));
                        System.out.println("服务端发送："+ JSON.toJSONString(msg));
                    }

                    // 服务端接收消息
                    InputStream inputStream = socket.getInputStream();
//                    byte[] buffer = new byte[512];
//                    int len = -1;
//                    while ((len = inputStream.read(buffer)) != -1) {
//                        String data = new String(buffer, 0, len);
//                        System.out.println("收到客户端的数据-----------------------------:" + data);
//                        JSONObject rec = JSONObject.parseObject(data);
//                    }
                    byte[] buffer = new byte[512];
                    inputStream.read(buffer, 0, 512);
                    String data = new String(buffer);
                    JSONObject rec = JSONObject.parseObject(data);
                    System.out.println("服务端收到:"+rec.toJSONString());
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
            }).start();
        }
    }


    /**
     * 发送到芯片端消息
     * @param msg
     */
    public static void sendTcpMessage(final String msg){
        if (StringUtils.isEmpty(msg)) {
            return;
        }
        if (isConnected()) {
            new Thread(() -> {
                try {
                    byte[] newbytes = transferStringToByte(msg, 512);
                    socket.getOutputStream().write(newbytes);
                    socket.getOutputStream().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * 将msg转为指定长度的byte数组，不足补0
     * @param msg
     * @return
     */
    public static byte[] transferStringToByte(String msg, int length) {
        byte[] newbytes = new byte[length];
        int oldLeth = msg.getBytes().length;
        byte[] oldbytes =  msg.getBytes();
        if (oldLeth < newbytes.length) {
            for (int len = 0; len < newbytes.length; len++) {
                if (len < oldLeth) {
                    newbytes[len] = oldbytes[len];
                } else {
                    newbytes[len] = 0;
                }
            }
        }
        return newbytes;
    }


    public static boolean isConnected(){
        return socket != null && socket.isConnected();
    }

    public static void main(String[] args) {
        SocketServer.startServer();
    }

}
