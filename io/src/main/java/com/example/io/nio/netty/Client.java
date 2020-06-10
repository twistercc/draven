package com.example.io.nio.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

public class Client implements Runnable {
    static ClientHandler client = new ClientHandler();



    @Override
    public void run() {
        String host = "127001";
        int port = 9090;
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(client);
                }
            });
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }


    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new Thread(new Client()).start();
        Scanner scanner = new Scanner(System.in);
        while (client.sendMsg(scanner.nextLine())) ;
    }

    /***
     * 4、测试
     *分别启动服务端和客户端，然后再客户端控制台输入表达式：
     *
     * 1+5+5+5+5+5
     * 客户端发送消息：1+5+5+5+5+5
     * 服务器消息：26
     * 156158*458918+125615
     * 客户端发送消息：156158*458918+125615
     * 服务器消息：71663842659E10
     * 1895612+555+5+5+5+5+5+5+5-5*4/4
     * 客户端发送消息：1895612+555+5+5+5+5+5+5+5-5*4/4
     * 服务器消息：1896197
     *    
     * 可以看到服务端返回的结果。
     * 查看服务端控制台：
     *
     * 服务器开启：9090
     * 收到客户端消息:1+5+5+5+5+5
     * 收到客户端消息:156158*458918+125615
     * 收到客户端消息:1895612+555+5+5+5+5+5+5+5-5*4/4
     */
}