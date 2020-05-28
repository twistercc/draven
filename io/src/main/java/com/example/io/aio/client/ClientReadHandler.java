package comexampleioaioclient;

import javaioIOException;
import javaioUnsupportedEncodingException;
import javanioByteBuffer;
import javaniochannelsAsynchronousSocketChannel;
import javaniochannelsCompletionHandler;
import javautilconcurrentCountDownLatch;

public class ClientReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel clientChannel;
    private CountDownLatch latch;

    public ClientReadHandler(AsynchronousSocketChannel clientChannel, CountDownLatch latch) {
        thisclientChannel = clientChannel;
        thislatch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        bufferflip();
        byte[] bytes = new byte[bufferremaining()];
        bufferget(bytes);
        String body;
        try {
            body = new String(bytes, "UTF-8");
            Systemoutprintln("客户端收到结果:" + body);
        } catch (UnsupportedEncodingException e) {
            eprintStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        Systemerrprintln("数据读取失败");
        try {
            clientChannelclose();
            latchcountDown();
        } catch (IOException e) {
        }
    }
}