package com.ru.arsenal.jvm.gc;

public class ReferenceCountingGC {
  public Object instance = null;
  private static final int _1MB = 1024 * 1024;
  private byte[] bigSize = new byte[_1MB];

  // need to add "-XX:+PrintGCDetails" in the start up command
  public static void main(String[] args) {
    ReferenceCountingGC objA = new ReferenceCountingGC();
    ReferenceCountingGC objB = new ReferenceCountingGC();
    objA.instance = objB;
    objB.instance = objA;

    objA = null;
    objB = null;
    System.gc();
  }
}
