package com.example.rpc.grpc;

import com.example.rpc.grpc.proto.InvokeRequest;
import com.example.rpc.grpc.proto.InvokeResponse;
import com.example.rpc.grpc.proto.UserCenterServiceGrpc;
import io.grpc.stub.StreamObserver;

/*
服务实现类
 */
public class UserCenterService extends UserCenterServiceGrpc.UserCenterServiceImplBase {

    public void userCenter(InvokeRequest request, StreamObserver responseObserver){
        System.out.println("-------------request->--"+ request);
        String name = request.getUserName();
        Integer age = request.getAge();
        String address = request.getAddress();
        String id = request.getId();
        InvokeResponse response = InvokeResponse.newBuilder()
                .setMsg(name + "cs" + " age " + String.valueOf(age)
                        + " from " +  address + " id " + id)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}


