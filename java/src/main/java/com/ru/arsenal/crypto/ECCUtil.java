package com.ru.arsenal.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.util.Base64;
import javax.crypto.Cipher;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ECCUtil {

  static Logger logger = LoggerFactory.getLogger("ECCUtil");

  static {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }

  public static void main(String[] args) throws Exception {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
    keyPairGenerator.initialize(256);
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
    ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();

    String publicKeyString = new String(Base64.getEncoder().encode(publicKey.getEncoded()));
    String privateKeyString = new String(Base64.getEncoder().encode(privateKey.getEncoded()));

    logger.info("generate {} public key, format {},  {}", publicKey.getAlgorithm(), publicKey.getFormat(),publicKeyString);
    logger.info("private key format {}, {}", privateKey.getFormat(),  privateKeyString);

    String text = "I am coco-cola again!";
    Cipher encryptors = Cipher.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);
    Cipher decrypts = Cipher.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);
    encryptors.init(Cipher.ENCRYPT_MODE, publicKey);
    decrypts.init(Cipher.DECRYPT_MODE, privateKey);

    byte[] cipherText = encryptors.doFinal(text.getBytes());
    logger.info("plainText '{}' encrypted as: {}", text, cipherText);
    byte[] plainText = decrypts.doFinal(cipherText);
    logger.info("cipherText '{}' decrypted as: {}", cipherText, plainText);
  }
}
