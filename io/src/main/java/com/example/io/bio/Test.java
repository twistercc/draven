package com.example.io.bio;

import java.io.IOException;
import java.util.Random;

/**
 * 测试方法
 *
 */
public class Test {
    //测试主方法
    public static void main(String[] args) throws InterruptedException {
        //运行服务器
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 传统bio
                    ServerNormal.start();
                    // 伪异步io
//                    ServerBetter.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //避免客户端先于服务器启动前执行代码
        Thread.sleep(100);
        //运行客户端
        char operators[] = {'+', '-', '*', '/'};
        Random random = new Random(System.currentTimeMillis());
        new Thread(() -> {
            while (true) {
                //随机产生算术表达式
                String expression = random.nextInt(10) + "" + operators[random.nextInt(4)] + (random.nextInt(10) + 1);
                Client.send(expression);
                try {
                    Thread.currentThread().sleep(random.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

/***
 * 1.1、传统的BIO编程
 *     网络编程的基本模型是C/S模型，即两个进程间的通信。
 *     服务端提供IP和监听端口，客户端通过连接操作想服务端监听的地址发起连接请求，通过三次握手连接，如果连接成功建立，双方就可以通过套接字进行通信。
 *     传统的同步阻塞模型开发中，ServerSocket负责绑定IP地址，启动监听端口；Socket负责发起连接操作。连接成功后，双方通过输入和输出流进行同步阻塞式通信。 
 *     简单的描述一下BIO的服务端通信模型：采用BIO通信模型的服务端，通常由一个独立的Acceptor线程负责监听客户端的连接，它接收到客户端连接请求之后为每个客户端创建一个新的线程进行链路处理没处理完成后，通过输出流返回应答给客户端，线程销毁。即典型的一请求一应答通宵模型。
 *     传统BIO通信模型图：
 *     该模型最大的问题就是缺乏弹性伸缩能力，当客户端并发访问量增加后，服务端的线程个数和客户端并发访问数呈1:1的正比关系，Java中的线程也是比较宝贵的系统资源，线程数量快速膨胀后，系统的性能将急剧下降，随着访问量的继续增大，系统最终就死-掉-了。
 *
 * 一、normal：
 *服务器已启动，端口号：12345
 * 算术表达式为：4-2
 * 服务器收到消息：4-2
 * ___结果为：2
 * 算术表达式为：5-10
 * 服务器收到消息：5-10
 * ___结果为：-5
 * 算术表达式为：0-9
 * 服务器收到消息：0-9
 * ___结果为：-9
 * 算术表达式为：0+6
 * 服务器收到消息：0+6
 * ___结果为：6
 * 算术表达式为：1/6
 * 服务器收到消息：1/6
 * ___结果为：0.16666666666666666
 * 从以上代码，很容易看出，BIO主要的问题在于每当有一个新的客户端请求接入时，服务端必须创建一个新的线程来处理这条链路，在需要满足高性能、高并发的场景是没法应用的（大量创建新的线程会严重影响服务器性能，甚至罢工）。
 *为了改进这种一连接一线程的模型，我们可以使用线程池来管理这些线程（需要了解更多请参考前面提供的文章），实现1个或多个线程处理N个客户端的模型（但是底层还是使用的同步阻塞I/O），通常被称为“伪异步I/O模型“。
 *  实现很简单，我们只需要将新建线程的地方，交给线程池管理即可，只需要改动刚刚的Server代码即可。
 *
 * 二、better：
 * 测试运行结果是一样的。
 * 我们知道，如果使用CachedThreadPool线程池（不限制线程数量，如果不清楚请参考文首提供的文章），其实除了能自动帮我们管理线程（复用），看起来也就像是1:1的客户端：线程数模型，而使用FixedThreadPool我们就有效的控制了线程的最大数量，保证了系统有限的资源的控制，实现了N:M的伪异步I/O模型。
 * 但是，正因为限制了线程数量，如果发生大量并发请求，超过最大数量的线程就只能等待，直到线程池中的有空闲的线程可以被复用。而对Socket的输入流就行读取时，会一直阻塞，直到发生：
 * 1.有数据可读
 * 2.可用数据以及读取完毕
 * 3.发生空指针或I/O异常
 *所以在读取数据较慢时（比如数据量大、网络传输慢等），大量并发的情况下，其他接入的消息，只能一直等待，这就是最大的弊端。
 *
 *而后面即将介绍的NIO，就能解决这个难题。
 *
 ***/