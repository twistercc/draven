package com.example.rpc.grpc;


//import com.ymm.usercenter.proto.InvokeRequest;
//import com.ymm.usercenter.proto.InvokeResponse;
//import com.ymm.usercenter.proto.UserCenterServiceGrpc;
import com.example.rpc.grpc.proto.InvokeRequest;
import com.example.rpc.grpc.proto.InvokeResponse;
import com.example.rpc.grpc.proto.UserCenterServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;

/*
 * 客户端类
 */
public class Client {

    public static void main(String[] args) {
        //InvokeRequest request = InvokeRequest.newBuilder().setUserName("cclllday").build();
        InvokeRequest.Builder builder = InvokeRequest.newBuilder();
        builder.setUserName("cclllday");
        builder.setAge(22);
        builder.setSex(1);
        builder.setAddress("tiansu");
        builder.setId("123456789");
        builder.setTelephone("1394545646");
        InvokeRequest request = builder.build();
        Channel channel = ManagedChannelBuilder.forAddress("127.0.0.1", 50052).usePlaintext(true).build();
        //UserCenterServiceGrpc.UserCenterServiceBlockingStub blockingStub = UserCenterServiceGrpc.newBlockingStub(channel);
        UserCenterServiceGrpc.UserCenterServiceBlockingStub blockingStub = UserCenterServiceGrpc.newBlockingStub(channel);
        InvokeResponse response = blockingStub.userCenter(request);
        System.out.println(response.getMsg());
    }
}
