package com.example.rpc.grpc;

/*
 *   服务提供类
 */

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;


public class ServerProvider {
    public static void main(String[] args) throws IOException,InterruptedException {
        int port = 50052;
        Server server = ServerBuilder.forPort(port)
                .addService(new UserCenterService())
                .build();
        server.start();
        System.out.println("--------start--------");
        Thread.sleep(1000 * 60 * 5);
        server.shutdown();
        System.out.println("--------shutdown------");
    }
}

