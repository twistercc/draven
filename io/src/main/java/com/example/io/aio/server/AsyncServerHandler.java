package comexampleioaioserver;

import javaioIOException;
import javanetInetSocketAddress;
import javaniochannelsAsynchronousServerSocketChannel;
import javautilconcurrentCountDownLatch;

public class AsyncServerHandler implements Runnable {
    public CountDownLatch latch;
    public AsynchronousServerSocketChannel channel;

    public AsyncServerHandler(int port) {
        try {
            //创建服务端通道
            channel = AsynchronousServerSocketChannelopen();
            //绑定端口
            channelbind(new InetSocketAddress(port));
            Systemoutprintln("服务器已启动，端口号：" + port);
        } catch (IOException e) {
            eprintStackTrace();
        }
    }

    @Override
    public void run() {
        //CountDownLatch初始化
        //它的作用：在完成一组正在执行的操作之前，允许当前的现场一直阻塞
        //此处，让现场在此阻塞，防止服务端执行完成后退出
        //也可以使用while(true)+sleep
        //生成环境就不需要担心这个问题，以为服务端是不会退出的
        latch = new CountDownLatch(1);
        //用于接收客户端的连接
        channelaccept(this, new AcceptHandler());
        try {
            latchawait();
        } catch (InterruptedException e) {
            eprintStackTrace();
        }
    }
}