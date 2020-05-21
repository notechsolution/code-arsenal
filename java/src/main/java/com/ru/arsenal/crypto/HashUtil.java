package com.ru.arsenal.crypto;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.tomcat.util.buf.HexUtils;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi.SHA3_256;
import org.bouncycastle.jcajce.provider.digest.SHA3;


public class HashUtil {

  public static void main(String[] args) throws NoSuchAlgorithmException {
    System.out.println("abcd -> "+md5("abcd"));
    System.out.println("abcd -> "+sha1("abcd"));
    System.out.println("abcd -> "+sha2("abcd"));
    System.out.println("abcd -> "+sha3("abcd"));
//    System.out.println(md5("abcd") + " <- abcd");
//    System.out.println(md5("明月几时有，把酒问青天，不知天上宫阙，今夕是何年") + " <- 明月几时有，把酒问青天，不知天上宫阙，今夕是何年");
//    System.out.println(md5("1") + " <- 1 ");
//    System.out.println(sha1("1") + " <- 1 ");
  }

  public static String md5(String content) throws NoSuchAlgorithmException {
    MessageDigest instance = MessageDigest.getInstance("MD5");
    instance.update(content.getBytes());
    return HexUtils.toHexString(instance.digest());
  }

  public static String sha1(String content) throws NoSuchAlgorithmException {
    MessageDigest instance = MessageDigest.getInstance("SHA-1");
    instance.update(content.getBytes());
    return HexUtils.toHexString(instance.digest());
  }
  public static String sha2(String content) throws NoSuchAlgorithmException {
    MessageDigest instance = MessageDigest.getInstance("SHA-256");
    instance.update(content.getBytes());
    return HexUtils.toHexString(instance.digest());
  }

  public static String sha3(String content) throws NoSuchAlgorithmException {
    Digest digest = new SHA3Digest(256);
    digest.update(content.getBytes(),0, content.length());
    byte[] hashArray = new byte[digest.getDigestSize()];
    digest.doFinal(hashArray, 0);
    return HexUtils.toHexString(hashArray);
  }
}
