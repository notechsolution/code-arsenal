package com.ru.arsenal.crypto;

import java.io.UnsupportedEncodingException;
import org.apache.tomcat.util.buf.HexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RC4Util {
  static Logger logger = LoggerFactory.getLogger("RC4Util");

  public static void main(String[] args) throws UnsupportedEncodingException {
    String message = "I am Vita";
    String password = "lemon";
    String cipherText = encrytRC4String(message, password);
    logger.info("message '{}' with password '{}', cipher text: '{}'", message, password, cipherText);
    String plainText = decrytRC4String(cipherText, password);
    logger.info("cipher '{}' with password '{}', plain text: '{}'", cipherText, password, plainText);
  }


  public static String encrytRC4String(String data, String key) throws UnsupportedEncodingException {
    if (data == null || key == null) {
      return null;
    }
    return HexUtils.toHexString(RC4Encryption(data.getBytes(), key));
  }


  public static String decrytRC4String(String cipherText, String key) {
    if (cipherText == null || key == null) {
      return null;
    }
    return new String(RC4Encryption(HexUtils.fromHexString(cipherText), key));
  }
  private static byte[] RC4Encryption(byte[] data, String mKkey) {
    int x = 0;
    int y = 0;
    byte key[] = keyDerivation(mKkey);
    int xorIndex;
    byte[] result = new byte[data.length];
    for (int i = 0; i < data.length; i++) {
      x = (x + 1) & 0xff;
      y = ((key[x] & 0xff) + y) & 0xff;
      byte tmp = key[x];
      key[x] = key[y];
      key[y] = tmp;
      xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
      result[i] = (byte) (data[i] ^ key[xorIndex]);
    }
    return result;

  }

  private static byte[] keyDerivation(String aKey) {
    byte[] bkey = aKey.getBytes();
    byte state[] = new byte[256];

    for (int i = 0; i < 256; i++) {
      state[i] = (byte) i;
    }
    int index1 = 0;
    int index2 = 0;
    if (bkey.length == 0) {
      return null;
    }
    for (int i = 0; i < 256; i++) {
      index2 = ((bkey[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
      byte tmp = state[i];
      state[i] = state[index2];
      state[index2] = tmp;
      index1 = (index1 + 1) % bkey.length;
    }
    return state;
  }

}
