package com.ru.arsenal.grpc.routeguide;

import com.ru.arsenal.grpc.routeguide.RouteGuideGrpc.RouteGuideBlockingStub;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RouteGuideClient {

  private static final Logger logger = LoggerFactory.getLogger(RouteGuideClient.class);

  private final RouteGuideBlockingStub blockingStub;

  /**
   * Construct client for accessing RouteGuide server using the existing channel.
   */
  public RouteGuideClient(Channel channel) {
    blockingStub = RouteGuideGrpc.newBlockingStub(channel);
  }

  /**
   * Blocking unary call example.  Calls getFeature and prints the response.
   */
  public void getFeature(int lat, int lon) {
    logger.info("*** GetFeature: lat={} lon={}", lat, lon);

    Point request = Point.newBuilder().setLatitude(lat).setLongitude(lon).build();

    Feature feature;
    try {
      feature = blockingStub.getFeature(request);
      logger.info("received feature {}", feature);
    } catch (StatusRuntimeException e) {
      logger.warn("RPC failed: {}", e.getStatus());
      return;
    }
    if (RouteGuideUtil.exists(feature)) {
      logger.info("Found feature called \"{}\" at {}, {}",
          feature.getName(),
          RouteGuideUtil.getLatitude(feature.getLocation()),
          RouteGuideUtil.getLongitude(feature.getLocation()));
    } else {
      logger.info("Found no feature at {}, {}",
          RouteGuideUtil.getLatitude(feature.getLocation()),
          RouteGuideUtil.getLongitude(feature.getLocation()));
    }
  }

  public void listFeatures(int lowLat, int lowLon, int hiLat, int hiLon) {
    logger.info("*** ListFeatures: lowLat={} lowLon={} hiLat={} hiLon={}", lowLat, lowLon, hiLat, hiLon);

    Rectangle request = Rectangle.newBuilder()
        .setLo(Point.newBuilder().setLatitude(lowLat).setLongitude(lowLon).build())
        .setHi(Point.newBuilder().setLatitude(hiLat).setLongitude(hiLon).build())
        .build();

    Iterator<Feature> features = blockingStub.listFeatures(request);
    for (int i = 1; features.hasNext(); i++) {
      logger.info("Result #{}: {}", i, features.next());
    }
  }

  /**
   * Issues several different requests and then exits.
   */
  public static void main(String[] args) throws InterruptedException {
    String target = "localhost:50052";
    if (args.length > 0) {
      if ("--help".equals(args[0])) {
        System.err.println("Usage: [target]");
        System.err.println("");
        System.err.println("  target  The server to connect to. Defaults to " + target);
        System.exit(1);
      }
      target = args[0];
    }

    List<Feature> features;
    try {
      features = RouteGuideUtil.parseFeatures(RouteGuideUtil.getDefaultFeaturesFile());
    } catch (IOException ex) {
      ex.printStackTrace();
      return;
    }

    ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
    try {
      RouteGuideClient client = new RouteGuideClient(channel);
      // Looking for a valid feature
      client.getFeature(409146138, -746188906);

      // Feature missing.
      client.getFeature(0, 0);

//      // Looking for features between 40, -75 and 42, -73.
      client.listFeatures(400000000, -750000000, 420000000, -730000000);
//
//      // Record a few randomly selected points from the features file.
//      client.recordRoute(features, 10);
//
//      // Send and receive some notes.
//      CountDownLatch finishLatch = client.routeChat();
//
//      if (!finishLatch.await(1, TimeUnit.MINUTES)) {
//        client.warning("routeChat can not finish within 1 minutes");
//      }
    } finally {
      channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }
  }
}
