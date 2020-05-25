package com.ru.arsenal.crypto;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi.SHA3_256;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.jcajce.provider.util.SecretKeyUtil;
import sun.security.krb5.internal.ktab.KeyTabConstants;


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
    testHashSalt();
  }

  public static void testHashSalt() throws NoSuchAlgorithmException {
    String content = "password1";
    System.out.println(String.format("'%s' m5 hash          -> %s", content, md5(content)));
    String salt = RandomStringUtils.randomAlphabetic(10);
    System.out.println(String.format("'%s' md5 hash+salt    -> %s", content, md5WithSalt(content, salt)));
    System.out.println(String.format("'%s' sha256 hash      -> %s", content, sha2(content)));
    System.out.println(String.format("'%s' sha256 hash+salt -> %s", content, sha256WithSalt(content, salt)));
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

  public static String md5WithSalt(String content, String salt) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("md5");
    String forHashContent = content + salt;
    digest.update(forHashContent.getBytes());
    return HexUtils.toHexString(digest.digest());
  }

  public static String sha256WithSalt(String content, String salt) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    String forHashContent = content + salt;
    digest.update(forHashContent.getBytes());
    return HexUtils.toHexString(digest.digest());
  }


}
