package com.ru.arsenal.blockchain;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;


public class HashCollision {

  public static void main(String[] args) {
    String data = "Lames";
    int nonce = 0;
    System.out.println("Start Hash Collision");
    long start = System.currentTimeMillis();
    while (nonce++ < Math.pow(2, 32)) {
      String potentialString = data.concat(nonce + "");
      String sha256Hex = Hashing.sha256().hashString(potentialString, StandardCharsets.UTF_8).toString();
      sha256Hex = Hashing.sha256().hashString(sha256Hex, StandardCharsets.UTF_8).toString();
      if(sha256Hex.startsWith("000000")){
        System.out.println(String.format("Found Hash %s from String %s with nonce %d", sha256Hex, potentialString, nonce));
        break;
      }
    }
    System.out.println("Time collapsing : "+ (System.currentTimeMillis() - start));
  }
}
