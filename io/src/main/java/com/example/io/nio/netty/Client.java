package comexampleionionetty;

import ionettybootstrapBootstrap;
import ionettychannelChannelFuture;
import ionettychannelChannelInitializer;
import ionettychannelChannelOption;
import ionettychannelEventLoopGroup;
import ionettychannelnioNioEventLoopGroup;
import ionettychannelsocketSocketChannel;
import ionettychannelsocketnioNioSocketChannel;

import javautilScanner;

public class Client implements Runnable {
    static ClientHandler client = new ClientHandler();



    @Override
    public void run() {
        String host = "127001";
        int port = 9090;
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            bgroup(workerGroup);
            bchannel(NioSocketChannelclass);
            boption(ChannelOptionSO_KEEPALIVE, true);
            bhandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    chpipeline()addLast(client);
                }
            });
            ChannelFuture f = bconnect(host, port)sync();
            fchannel()closeFuture()sync();
        } catch (InterruptedException e) {
            eprintStackTrace();
        } finally {
            workerGroupshutdownGracefully();
        }
    }


    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new Thread(new Client())start();
        Scanner scanner = new Scanner(Systemin);
        while (clientsendMsg(scannernextLine())) ;
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