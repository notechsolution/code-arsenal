package com.ru.arsenal.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.tomcat.util.buf.HexUtils;

public class HmacUtil {

  public static String ALGORITHM_HmacMD5 = "HmacMD5"; // 128 bits
  public static String ALGORITHM_HmacSHA1 = "HmacSHA1"; // 160 bits
  public static String ALGORITHM_HmacSHA256 = "HmacSHA256"; // 256 bits
  public static String ALGORITHM_HmacSHA384 = "HmacSHA384"; // 384 bits
  public static String ALGORITHM_HmacSHA512 = "HmacSHA512"; // 512 bits

  public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException {
    String content = "this is origin content";
    String secret = "rootPassword";
    String hmac = generateHmac(ALGORITHM_HmacSHA1, content, secret);
    System.out.println("hmac -> "+ hmac);
  }

  public static String generateHmac(String algorithm, String content, String key)
      throws NoSuchAlgorithmException, InvalidKeyException {
    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), algorithm);
    Mac mac = Mac.getInstance(algorithm);
    mac.init(secretKey);
    byte[] hashByte = mac.doFinal(content.getBytes());
    return HexUtils.toHexString(hashByte);
  }

}
