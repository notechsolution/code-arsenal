package com.ru.arsenal.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SymmetricUtil {

  static Logger logger = LoggerFactory.getLogger("SymmetricUtil");

  public static void main(String[] args) throws Exception {
    String message = "I am Vita";

    String desPassword = "lemonIsG"; // 8bit
    testCryptgo("DES","DES/CBC/PKCS5Padding", message, desPassword );

    String aesPassword = "lemonIsGoodForYo"; // 128bit
    testCryptgo("AES","AES/CBC/PKCS5Padding", message, aesPassword );

  }

  public static void testCryptgo(String family, String algorithm, String message, String password) throws Exception {
    String cipherText = encrypt(family, algorithm, message, password);
    logger.info("[{}] message '{}' with password '{}', cipher text: '{}'", family, message, password, cipherText);
    String plainText = decrypt(family, algorithm, cipherText, password);
    logger.info("[{}] cipher '{}' with password '{}', plain text: '{}'", family, cipherText, password, plainText);
  }

  public static String encrypt(String family, String algorithm, String message, String password)
      throws Exception{
    SecretKeySpec key = new SecretKeySpec(password.getBytes(), family);
    Cipher cipher = Cipher.getInstance(algorithm);
//    REMARKS: if you want to use self defined IV, use below logic to generate IV. here use the system default IV by SecureRandom
//    String ivString = RandomStringUtils.randomAlphanumeric(cipher.getBlockSize());
//    IvParameterSpec iv = new IvParameterSpec(ivString.getBytes());
//    cipher.init(Cipher.ENCRYPT_MODE, key,iv);
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] cipherByte = cipher.doFinal(message.getBytes());
    return HexUtils.toHexString(ArrayUtils.addAll(cipher.getIV(), cipherByte));
  }

  public static String decrypt(String family,String algorithm, String message, String password)
      throws Exception{
    byte[] messageByte = HexUtils.fromHexString(message);
    SecretKeySpec key = new SecretKeySpec(password.getBytes(), family);
    Cipher cipher = Cipher.getInstance(algorithm);
    // retrieve IV from cipherText
    byte[] iv = ArrayUtils.subarray(messageByte,0, cipher.getBlockSize());
    // retrieve the real cipher content from input
    byte[] cipherContent = ArrayUtils.subarray(messageByte,cipher.getBlockSize(), messageByte.length);
    IvParameterSpec ivParams = new IvParameterSpec(iv);
    cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
    byte[] cipherByte = cipher.doFinal(cipherContent);
    return new String(cipherByte);
  }

}
