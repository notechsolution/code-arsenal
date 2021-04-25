package com.ru.arsenal.grpc;

import com.ru.arsenal.grpc.helloworld.GreeterGrpc;
import com.ru.arsenal.grpc.helloworld.HelloReply;
import com.ru.arsenal.grpc.helloworld.HelloRequest;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.RandomUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldClient {

  private static final Logger logger = LoggerFactory.getLogger("HelloWorldClient");

  private final GreeterGrpc.GreeterBlockingStub blockingStub;

  public HelloWorldClient(Channel channel) {
    blockingStub = GreeterGrpc.newBlockingStub(channel);
  }

//  public void shutdown() throws InterruptedException {
//    channel.shutdown().awaitTermination(30, TimeUnit.SECONDS);
//  }

  public void greet(String name, long age) {
    HelloRequest request = HelloRequest.newBuilder().setName(name).setAge(age).build();
    HelloReply response = null;
    try {
      // 0a 04 4f4f434c 10 58 (hex)
      // 0a: Tag, 00001 010 (field sequence, name==1, 00001) + (field type - string, 2, 010)
      // 04: value length, 4
      // 4f 4f 43 4c:  OOCL
      // 10: Tag, 00010 000 (field sequence, name==2, 00010) + (field type - int64, 0, 000)
      // 58: 88, 0 1011000
      response = blockingStub.sayHello(request);
    }catch (StatusRuntimeException e){
      logger.error("RPC failed", e.getStatus().asException());
      return;
    }
    logger.debug("Data encoded in protobuf with length {} : {}", request.getSerializedSize(), HexUtils.toHexString(request.toByteArray()));
    logger.info("Greeting:" + response.getMessage());
  }

  public static void main(String[] args) throws InterruptedException {
    String user = "world";
    // Access a service running on the local machine on port 50051
    String target = "localhost:50051";
    // Allow passing in the user and target strings as command line arguments
    if (args.length > 0) {
      if ("--help".equals(args[0])) {
        System.err.println("Usage: [name [target]]");
        System.err.println("");
        System.err.println("  name    The name you wish to be greeted by. Defaults to " + user);
        System.err.println("  target  The server to connect to. Defaults to " + target);
        System.exit(1);
      }
      user = args[0];
    }
    if (args.length > 1) {
      target = args[1];
    }

    ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
        .usePlaintext()
        .build();
    HelloWorldClient client = new HelloWorldClient(channel);
    List<String> companies = Arrays.asList("Google", "OOCL", "COSCO", "HUAWEI","TENCENT","ALI" ,"Microsoft", "BYTEDANCE");
    try {
      client.greet("OOCL", 88);
      client.greet("COSCO_aaaaaaaaaaaaaaaaaaaaaaaaaverylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring===", 100);
//      client.greet("COSCO_verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====verylongstring=====", 100);
//      for (int i = 0; i < 8; i++) {
//        client.greet(companies.get(RandomUtils.nextInt(0, 8)));
//        Thread.sleep(1000);
//      }
    }finally {
      channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }
  }
}
