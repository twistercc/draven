package com.example.io.bio.local;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.util.internal.StringUtil;
import org.springframework.util.StringUtils;

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

                        // 服务端发送消息
                        if(TcpServer.isConnected()){
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
                        byte[] buffer = new byte[512];
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

//    public static void sendTcpMessage(final String msg){
//        if (isConnected()) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        socket.getOutputStream().write(msg.getBytes());
//                        socket.getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
//    }

    /**
     * 发送到芯片端消息
     * @param msg 长度不够512则补0到足长
     */
    public static void sendTcpMessage(final String msg){
        if (StringUtils.isEmpty(msg)) {
            return;
        }
        if (isConnected()) {
            new Thread(() -> {
                try {
                    StringBuilder sb = new StringBuilder().append(msg);
                    int leng = sb.toString().getBytes().length;
                    // 发送长度
                    socket.getOutputStream().write(intToBytesLittle(leng));
                    // 发送内容
                    socket.getOutputStream().write(sb.toString().getBytes());
                    socket.getOutputStream().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * 以小端方式写入前4个字节表示的内容长度
     * @param value
     * @return
     */
    public static byte[] intToBytesLittle(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }


    public static boolean isConnected(){
        return socket != null && socket.isConnected();
    }

    public static void main(String[] args) {
        TcpServer.startServer();
    }

}
