package comexampleionionetty;

import comexampleioCalculator;
import ionettybufferByteBuf;
import ionettybufferUnpooled;
import ionettychannelChannelHandlerContext;
import ionettychannelChannelInboundHandlerAdapter;

import javaioUnsupportedEncodingException;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        ByteBuf in = (ByteBuf) msg;
        byte[] req = new byte[inreadableBytes()];
        inreadBytes(req);
        String body = new String(req, "utf-8");
        Systemoutprintln("收到客户端消息:" + body);
        String calrResult = null;
        try {
            calrResult = CalculatorInstancecal(body)toString();
        } catch (Exception e) {
            calrResult = "错误的表达式：" + egetMessage();
        }
        ctxwrite(UnpooledcopiedBuffer(calrResultgetBytes()));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctxflush();
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        causeprintStackTrace();
        ctxclose();
    }
}