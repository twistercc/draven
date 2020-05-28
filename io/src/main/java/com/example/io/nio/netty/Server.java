package comexampleionionetty;

import ionettybootstrapServerBootstrap;
import ionettychannelChannelFuture;
import ionettychannelChannelInitializer;
import ionettychannelChannelOption;
import ionettychannelEventLoopGroup;
import ionettychannelnioNioEventLoopGroup;
import ionettychannelsocketSocketChannel;
import ionettychannelsocketnioNioServerSocketChannel;

public class Server {
    private int port;

    public Server(int port) {
        thisport = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            bgroup(bossGroup, workerGroup)
                    channel(NioServerSocketChannelclass)
                    option(ChannelOptionSO_BACKLOG, 1024)
                    childOption(ChannelOptionSO_KEEPALIVE, true)
                    childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannelpipeline()addLast(new ServerHandler());
                        }
                    });
            ChannelFuture f = bbind(port)sync();
            Systemoutprintln("服务器开启：" + port);
            fchannel()closeFuture()sync();
        } finally {
            workerGroupshutdownGracefully();
            bossGroupshutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (argslength > 0) {
            port = IntegerparseInt(args[0]);
        } else {
            port = 9090;
        }
        new Server(port)run();
    }
}