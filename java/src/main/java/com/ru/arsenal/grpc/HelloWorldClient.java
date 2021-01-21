package com.ru.arsenal.grpc;

import com.ru.arsenal.grpc.helloworld.GreeterGrpc;
import com.ru.arsenal.grpc.helloworld.HelloReply;
import com.ru.arsenal.grpc.helloworld.HelloRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldClient {

  private static final Logger logger = LoggerFactory.getLogger("HelloWorldClient");

  private final ManagedChannel channel;
  private final GreeterGrpc.GreeterBlockingStub blockingStub;

  public HelloWorldClient(String target) {
    channel = ManagedChannelBuilder.forTarget(target)
        .usePlaintext(false)
        .build();
    blockingStub = GreeterGrpc.newBlockingStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(30, TimeUnit.SECONDS);
  }

  public void greet(String name) {
    HelloRequest request = HelloRequest.newBuilder().setName(name).build();
    HelloReply response = null;
    try {
      response = blockingStub.sayHello(request);
    }catch (StatusRuntimeException e){
      logger.error("RPC failed", e.getStatus().asException());
      return;
    }
    logger.info("Greeting:" + response.getMessage());
  }

  public static void main(String[] args) throws InterruptedException {
    HelloWorldClient client = new HelloWorldClient("localhost:50051");
    List<String> companies = Arrays.asList("Google", "OOCL", "COSCO", "HUAWEI","TENCENT","ALI" ,"Microsoft", "BYTEDANCE");
    try {
      client.greet("OOCL");
//      for (int i = 0; i < 8; i++) {
//        client.greet(companies.get(RandomUtils.nextInt(0, 8)));
//        Thread.sleep(1000);
//      }
    }finally {
      client.shutdown();
    }
  }
}
