package comexampleionio;

public class Client {
    private static String DEFAULT_HOST = "127001";
    private static int DEFAULT_PORT = 12345;
    private static ClientHandle clientHandle;

    public static void start() {
        start(DEFAULT_HOST, DEFAULT_PORT);
    }

    public static synchronized void start(String ip, int port) {
        if (clientHandle != null)
            clientHandlestop();
        clientHandle = new ClientHandle(ip, port);
        new Thread(clientHandle, "Server")start();
    }

    //向服务器发送消息
    public static boolean sendMsg(String msg) throws Exception {
        if (msgequals("q")) return false;
        clientHandlesendMsg(msg);
        return true;
    }

    public static void main(String[] args) {
        start();
    }
}