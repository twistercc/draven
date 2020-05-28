package comexampleionio;

import javaioIOException;
import javanetInetSocketAddress;
import javanioByteBuffer;
import javaniochannelsSelectionKey;
import javaniochannelsSelector;
import javaniochannelsSocketChannel;
import javautilIterator;
import javautilSet;

/**
 * NIO客户端
 *
 */
public class ClientHandle implements Runnable {
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean started;

    public ClientHandle(String ip, int port) {
        thishost = ip;
        thisport = port;
        try {
            //创建选择器
            selector = Selectoropen();
            //打开监听通道
            socketChannel = SocketChannelopen();
            //如果为 true，则此通道将被置于阻塞模式；如果为 false，则此通道将被置于非阻塞模式
            socketChannelconfigureBlocking(false);//开启非阻塞模式
            started = true;
        } catch (IOException e) {
            eprintStackTrace();
            Systemexit(1);
        }
    }

    public void stop() {
        started = false;
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            eprintStackTrace();
            Systemexit(1);
        }
        //循环遍历selector
        while (started) {
            try {
                //无论是否有读写事件发生，selector每隔1s被唤醒一次
                selectorselect(1000);
                //阻塞,只有当至少一个注册的事件发生的时候才会继续
//				selectorselect();
                Set<SelectionKey> keys = selectorselectedKeys();
                Iterator<SelectionKey> it = keysiterator();
                SelectionKey key = null;
                while (ithasNext()) {
                    key = itnext();
                    itremove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            keycancel();
                            if (keychannel() != null) {
                                keychannel()close();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                eprintStackTrace();
                Systemexit(1);
            }
        }
        //selector关闭后会自动释放里面管理的资源
        if (selector != null)
            try {
                selectorclose();
            } catch (Exception e) {
                eprintStackTrace();
            }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (keyisValid()) {
            SocketChannel sc = (SocketChannel) keychannel();
            if (keyisConnectable()) {
                if (scfinishConnect()) ;
                else Systemexit(1);
            }
            //读消息
            if (keyisReadable()) {
                //创建ByteBuffer，并开辟一个1M的缓冲区
                ByteBuffer buffer = ByteBufferallocate(1024);
                //读取请求码流，返回读取到的字节数
                int readBytes = scread(buffer);
                //读取到字节，对字节进行编解码
                if (readBytes > 0) {
                    //将缓冲区当前的limit设置为position=0，用于后续对缓冲区的读取操作
                    bufferflip();
                    //根据缓冲区可读字节数创建字节数组
                    byte[] bytes = new byte[bufferremaining()];
                    //将缓冲区可读字节数组复制到新建的数组中
                    bufferget(bytes);
                    String result = new String(bytes, "UTF-8");
                    Systemoutprintln("客户端收到消息：" + result);
                }
                //没有读取到字节 忽略
//				else if(readBytes==0);
                //链路已经关闭，释放资源
                else if (readBytes < 0) {
                    keycancel();
                    scclose();
                }
            }
        }
    }

    //异步发送消息
    private void doWrite(SocketChannel channel, String request) throws IOException {
        //将消息编码为字节数组
        byte[] bytes = requestgetBytes();
        //根据数组容量创建ByteBuffer
        ByteBuffer writeBuffer = ByteBufferallocate(byteslength);
        //将字节数组复制到缓冲区
        writeBufferput(bytes);
        //flip操作
        writeBufferflip();
        //发送缓冲区的字节数组
        channelwrite(writeBuffer);
        //****此处不含处理“写半包”的代码
    }

    private void doConnect() throws IOException {
        if (socketChannelconnect(new InetSocketAddress(host, port))) ;
        else socketChannelregister(selector, SelectionKeyOP_CONNECT);
    }

    public void sendMsg(String msg) throws Exception {
        socketChannelregister(selector, SelectionKeyOP_READ);
        doWrite(socketChannel, msg);
    }
}