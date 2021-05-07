package com.ru.arsenal.grpc.routeguide;

import static java.lang.Math.max;
import static java.lang.Math.min;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class RouteGuideServer {

  private static final Logger logger = Logger.getLogger("RouteGuideServer");

  private int port = 50052;
  private Server server;



  private void start() throws IOException {
    List<Feature> features = RouteGuideUtil.parseFeatures(RouteGuideUtil.getDefaultFeaturesFile());
    logger.info(String.format("load %d features from databases", features.size() ));
    server = ServerBuilder.forPort(port)
        .addService(new RouteGuideService(features))
        .build()
        .start();

    logger.info("Server starter, listening on " + port);

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        System.err.println("*** shutting down gRPC server since JVM is shutting down");

        try {
          RouteGuideServer.this.stop();
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

  public static void main(String[] args) throws InterruptedException, IOException {
    final RouteGuideServer server = new RouteGuideServer();
    server.start();
    server.blockUntilShutdown();
  }

  private static class RouteGuideService extends RouteGuideGrpc.RouteGuideImplBase {

    private final Collection<Feature> features;

    RouteGuideService(Collection<Feature> features) {
      this.features = features;
    }

    @Override
    public void getFeature(Point request, StreamObserver<Feature> responseObserver) {
      responseObserver.onNext(checkFeature(request));
      responseObserver.onCompleted();
    }

    @Override
    public void listFeatures(Rectangle request, StreamObserver<Feature> responseObserver) {
      int left = min(request.getLo().getLongitude(), request.getHi().getLongitude());
      int right = max(request.getLo().getLongitude(), request.getHi().getLongitude());
      int top = max(request.getLo().getLatitude(), request.getHi().getLatitude());
      int bottom = min(request.getLo().getLatitude(), request.getHi().getLatitude());

      for (Feature feature : features) {
        if(!RouteGuideUtil.exists(feature)){
          continue;
        }

        int lat = feature.getLocation().getLatitude();
        int lon = feature.getLocation().getLongitude();
        if(lon >= left && lon <=right && lat >= bottom && lat <= top) {
          responseObserver.onNext(feature);
        }
      }

      responseObserver.onCompleted();
    }

    private Feature checkFeature(Point location) {
      for (Feature feature : features) {
        if (feature.getLocation().getLatitude() == location.getLatitude()
            && feature.getLocation().getLongitude() == location.getLongitude()) {
          return feature;
        }
      }
      return Feature.newBuilder().setName("").setLocation(location).build();
    }
  }

}
