package com.example.io.aio;

import com.example.io.aio.client.Client;
import com.example.io.aio.server.Server;

import java.util.Scanner;

/**
 * 测试方法
 *
 * @author yangtao__anxpp.com
 * @version 1.0
 */
public class Test {
    //测试主方法
    public static void main(String[] args) throws Exception {
        //运行服务器
        Server.start();
        //避免客户端先于服务器启动前执行代码
        Thread.sleep(100);
        //运行客户端
        Client.start();
        System.out.println("请输入请求消息：");
        Scanner scanner = new Scanner(System.in);
        while (Client.sendMsg(scanner.nextLine())) ;
    }
}

/****
 AIO是真正的异步非阻塞的，所以，在面对超级大量的客户端，更能得心应手。

 下面就比较一下，几种I/O编程的优缺点。


 **/