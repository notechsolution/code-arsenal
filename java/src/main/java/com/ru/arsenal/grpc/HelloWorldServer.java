package com.ru.arsenal.grpc;

import com.ru.arsenal.grpc.helloworld.GreeterGrpc;
import com.ru.arsenal.grpc.helloworld.HelloReply;
import com.ru.arsenal.grpc.helloworld.HelloRequest;
import com.ru.arsenal.grpc.helloworld.ReplicatorGrpc;
import com.ru.arsenal.grpc.helloworld.ReplicatorRequest;
import com.ru.arsenal.grpc.helloworld.ReplicatorResponse;
import com.ru.arsenal.grpc.helloworld.ReplicatorResponse.Builder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class HelloWorldServer {

  private static final Logger logger = Logger.getLogger("HelloWorldServer");

  private int port = 50051;
  private Server server;

  private void start() throws IOException {
    server = ServerBuilder.forPort(port)
        .addService(new GreeterImpl())
        .addService(new ReplicatorImpl())
        .build()
        .start();

    logger.info("Server starter, listening on " + port);

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        System.err.println("*** shutting down gRPC server since JVM is shutting down");

        try {
          HelloWorldServer.this.stop();
        } catch (InterruptedException e) {
          e.printStackTrace(System.err);
        }

        System.err.println("*** server shut down");
      }
    });
  }

  private void stop() throws InterruptedException {
    if (server != null) {
      server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
    }
  }

  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  static class GreeterImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
      HelloReply helloReply = HelloReply.newBuilder()
          .setMessage("Hello My Dear " + request.getName() + "with age " + request.getAge())
          .build();
      responseObserver.onNext(helloReply);
      responseObserver.onCompleted();
    }
  }

  static class ReplicatorImpl extends ReplicatorGrpc.ReplicatorImplBase {

    @Override
    public void doReplicate(ReplicatorRequest request,
        StreamObserver<ReplicatorResponse> responseObserver) {
      String message = String
          .format("replicator type %s request by %s with count %d isValid %s, targets: ",
              request.getReplicatorType(), request.getRequestor(), request.getCount(),
              request.getValid());
      for (int i = 0; i < request.getTargetsCount(); i++) {
        message += request.getTargets(i);
      }
      Builder responseBuilder = ReplicatorResponse.newBuilder()
          .setResult(message);
      for (int i = 0; i < request.getTargetsCount(); i++) {
        responseBuilder.addTargets(request.getTargets(i));
      }
      responseObserver.onNext(responseBuilder.build());
      responseObserver.onCompleted();
    }
  }

  public static void main(String[] args) throws InterruptedException, IOException {
    final HelloWorldServer server = new HelloWorldServer();
    server.start();
    server.blockUntilShutdown();
  }
}
