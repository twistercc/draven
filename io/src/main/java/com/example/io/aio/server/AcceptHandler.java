package comexampleioaioserver;

import javanioByteBuffer;
import javaniochannelsAsynchronousSocketChannel;
import javaniochannelsCompletionHandler;

//作为handler接收客户端连接
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncServerHandler> {
    @Override
    public void completed(AsynchronousSocketChannel channel, AsyncServerHandler serverHandler) {
        //继续接受其他客户端的请求
        ServerclientCount++;
        Systemoutprintln("连接的客户端数：" + ServerclientCount);
        serverHandlerchannelaccept(serverHandler, this);
        //创建新的Buffer
        ByteBuffer buffer = ByteBufferallocate(1024);
        //异步读  第三个参数为接收消息回调的业务Handler
        channelread(buffer, buffer, new ServerReadHandler(channel));
    }

    @Override
    public void failed(Throwable exc, AsyncServerHandler serverHandler) {
        excprintStackTrace();
        serverHandlerlatchcountDown();
    }
}