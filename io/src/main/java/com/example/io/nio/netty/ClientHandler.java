package comexampleionionetty;

import ionettybufferByteBuf;
import ionettybufferUnpooled;
import ionettychannelChannelHandlerContext;
import ionettychannelChannelInboundHandlerAdapter;

import javaioUnsupportedEncodingException;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    ChannelHandlerContext ctx;

    /**
     * tcp链路简历成功后调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        thisctx = ctx;
    }

    public boolean sendMsg(String msg) {
        Systemoutprintln("客户端发送消息：" + msg);
        byte[] req = msggetBytes();
        ByteBuf m = Unpooledbuffer(reqlength);
        mwriteBytes(req);
        ctxwriteAndFlush(m);
        return msgequals("q") ? false : true;
    }

    /**
     * 收到服务器消息后调用
     *
     * @throws UnsupportedEncodingException
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[bufreadableBytes()];
        bufreadBytes(req);
        String body = new String(req, "utf-8");
        Systemoutprintln("收到服务器消息：" + body);
    }

    /**
     * 发生异常时调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        causeprintStackTrace();
        ctxclose();
    }
}