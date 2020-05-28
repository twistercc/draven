package comexampleioaioclient;



import javaioIOException;
import javanioByteBuffer;
import javaniochannelsAsynchronousSocketChannel;
import javaniochannelsCompletionHandler;
import javautilconcurrentCountDownLatch;

public class ClientWriteHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel clientChannel;
    private CountDownLatch latch;

    public ClientWriteHandler(AsynchronousSocketChannel clientChannel, CountDownLatch latch) {
        thisclientChannel = clientChannel;
        thislatch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        //完成全部数据的写入
        if (bufferhasRemaining()) {
            clientChannelwrite(buffer, buffer, this);
        } else {
            //读取数据
            ByteBuffer readBuffer = ByteBufferallocate(1024);
            clientChannelread(readBuffer, readBuffer, new ClientReadHandler(clientChannel, latch));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        Systemerrprintln("数据发送失败");
        try {
            clientChannelclose();
            latchcountDown();
        } catch (IOException e) {
        }
    }
}