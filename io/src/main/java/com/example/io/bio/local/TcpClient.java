package com.example.io.bio.local;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by wentao.cui on 2020/3/20.
 */

public class TcpClient {
    public static Socket socket;
    private static final int port = 1234;

    public static void startClient(){
        if (socket == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("启动客户端");
                        socket = new Socket(InetAddress.getLocalHost(), port);
                        System.out.println("客户端连接成功");
                        PrintWriter pw = new PrintWriter(socket.getOutputStream());
                        InputStream inputStream = socket.getInputStream();
                        byte[] lengthBuf = new byte[4];
                        inputStream.read(lengthBuf, 0, 4);
                        int length = bytesToIntLittle(lengthBuf, 0);
                        byte[] buffer = new byte[length];
                        inputStream.read(buffer, 0, length);
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
                }
            }).start();
        }
    }

    public static void sendTcpMessage(final String msg){
        if (socket != null && socket.isConnected()) {
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

    /**
     * 以小端方式，读取前4个字节表示的长度
     * @param src
     * @param offset
     * @return
     */
    public static int bytesToIntLittle(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    public static void main(String[] args) {
        TcpClient.startClient();
    }

}
