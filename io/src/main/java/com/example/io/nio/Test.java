package com.example.io.nio;

import java.util.Scanner;

/**
 * 测试方法
 *
 * @author yangtao__anxpp.com
 * @version 1.0
 */
public class Test {
    //测试主方法
    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {
        //运行服务器
        Server.start();
        //避免客户端先于服务器启动前执行代码
        Thread.sleep(100);
        //运行客户端
        Client.start();
        while (Client.sendMsg(new Scanner(System.in).nextLine())) ;
    }
}

/****
     2.1、简介
    对于低负载、低并发的应用程序，可以使用同步阻塞I/O来提升开发速率和更好的维护性；对于高负载、高并发的（网络）应用，应使用NIO的非阻塞模式来开发。
    2.2、缓冲区 Buffer
    Buffer是一个对象，包含一些要写入或者读出的数据。
    在NIO库中，所有数据都是用缓冲区处理的。在读取数据时，它是直接读到缓冲区中的；在写入数据时，也是写入到缓冲区中。任何时候访问NIO中的数据，都是通过缓冲区进行操作。
    缓冲区实际上是一个数组，并提供了对数据结构化访问以及维护读写位置等信息。
    具体的缓存区有这些：ByteBuffe、CharBuffer、 ShortBuffer、IntBuffer、LongBuffer、FloatBuffer、DoubleBuffer。他们实现了相同的接口：Buffer。
    2.3、通道 Channel
    我们对数据的读取和写入要通过Channel，它就像水管一样，是一个通道。通道不同于流的地方就是通道是双向的，可以用于读、写和同时读写操作。
    底层的操作系统的通道一般都是全双工的，所以全双工的Channel比流能更好的映射底层操作系统的API。
    Channel主要分两大类：
    SelectableChannel：用户网络读写
    FileChannel：用于文件操作
    后面代码会涉及的ServerSocketChannel和SocketChannel都是SelectableChannel的子类。
    2.4、多路复用器 Selector
    Selector是Java  NIO 编程的基础。
    Selector提供选择已经就绪的任务的能力：Selector会不断轮询注册在其上的Channel，如果某个Channel上面发生读或者写事件，这个Channel就处于就绪状态，会被Selector轮询出来，然后通过SelectionKey可以获取就绪Channel的集合，进行后续的I/O操作。
    一个Selector可以同时轮询多个Channel，因为JDK使用了epoll()代替传统的select实现，所以没有最大连接句柄1024/2048的限制。所以，只需要一个线程负责Selector的轮询，就可以接入成千上万的客户端。

 可以看到，创建NIO服务端的主要步骤如下：
     打开ServerSocketChannel，监听客户端连接
     绑定监听端口，设置连接为非阻塞模式
     创建Reactor线程，创建多路复用器并启动线程
     将ServerSocketChannel注册到Reactor线程中的Selector上，监听ACCEPT事件
     Selector轮询准备就绪的key
     Selector监听到新的客户端接入，处理新的接入请求，完成TCP三次握手，简历物理链路
     设置客户端链路为非阻塞模式
     将新接入的客户端连接注册到Reactor线程的Selector上，监听读操作，读取客户端发送的网络消息
     异步读取客户端消息到缓冲区
     对Buffer编解码，处理半包消息，将解码成功的消息封装成Task
     将应答消息编码为Buffer，调用SocketChannel的write将消息异步发送给客户端

 因为应答消息的发送，SocketChannel也是异步非阻塞的，所以不能保证一次能吧需要发送的数据发送完，此时就会出现写半包的问题。我们需要注册写操作，不断轮询Selector将没有发送完的消息发送完毕，然后通过Buffer的hasRemain()方法判断消息是否发送完成。

 测试结果：
 服务器已启动，端口号：12345
 1+2+3+4+5+6
 服务器收到消息：1+2+3+4+5+6
 客户端收到消息：21
 1*2/3-4+5*6/7-8
 服务器收到消息：1*2/3-4+5*6/7-8
 客户端收到消息：-7.0476190476190474
 运行多个客户端，都是没有问题的。


 **/