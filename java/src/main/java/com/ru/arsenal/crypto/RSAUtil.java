package com.ru.arsenal.crypto;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RSAUtil {

  static Logger logger = LoggerFactory.getLogger("RSAUtil");

  public static void main(String[] args) throws Exception {
    KeyPair keyPair = generateKeyPair(1024);
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    String publicKeyString = new String(Base64.getEncoder().encode(publicKey.getEncoded()));
    String privateKeyString = new String(Base64.getEncoder().encode(privateKey.getEncoded()));
    logger.info("generate {} bits public key, format {},  {}", publicKey.getModulus().bitLength(), publicKey.getFormat(), publicKeyString);
    logger.info("private key format {}, {}", privateKey.getFormat(),  privateKeyString);

    String message = "I am Coco Cola!";
    String cipherText = encrypt(publicKeyString, message);
    logger.info("plainText '{}' encrypted as: {}", message, cipherText);
    String plainText = decrypt(privateKeyString, cipherText);
    logger.info("cipherText '{}' decrypted as: {}", cipherText, plainText);

  }


  public static String encrypt(String publicKeyString, String message)
      throws  NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
    RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
    byte[] encrypted = cipher.doFinal(message.getBytes());
    return new String(Base64.getEncoder().encode(encrypted));
  }


  public static String decrypt(String privateKeyString, String cipherText)
      throws  NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
    PrivateKey priKey = KeyFactory.getInstance("RSA").generatePrivate(privateKeySpec);
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, priKey);
    byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText.getBytes()));
    return new String(decrypted);
  }

  public static KeyPair generateKeyPair(int keySize) throws NoSuchAlgorithmException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(keySize);
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    return keyPair;
  }
}
