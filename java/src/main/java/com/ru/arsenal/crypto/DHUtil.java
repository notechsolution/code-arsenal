package com.ru.arsenal.crypto;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Random;
import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DHUtil {

  static Logger logger = LoggerFactory.getLogger("DHUtil");

  //http://www.cs.ucf.edu/~dmarino/ucf/cis3362/progs/ECC.java
  public static void main(String[] args)
      throws Exception {
    BigInteger p = BigInteger.probablePrime(512, new Random());
    BigInteger g = new BigInteger("2");
    KeyPair keyPairA = generateDHKeyPair(p, g);
    KeyPair keyPairB = generateDHKeyPair(p, g);

    String publicKeyA = new String(Base64.getEncoder().encode(keyPairA.getPublic().getEncoded()));
    String publicKeyB = new String(Base64.getEncoder().encode(keyPairB.getPublic().getEncoded()));
    BigInteger publicKeyAY = ((DHPublicKey) keyPairA.getPublic()).getY();
    BigInteger publicKeyBY = ((DHPublicKey) keyPairB.getPublic()).getY();

    logger.info("publicKeyA, format {},  {}", keyPairA.getPublic().getFormat(), publicKeyA);
    logger.info("publicKeyB, format {},  {}", keyPairB.getPublic().getFormat(), publicKeyB);
    logger.info("publicKeyA Y,  {}", publicKeyAY);
    logger.info("publicKeyB Y,  {}", publicKeyBY);
//
    byte[] secretA = computeSharedSecret(keyPairA.getPrivate(), publicKeyBY.toByteArray(), p, g);
    String secretAString = new String(Base64.getEncoder().encode(secretA));

    byte[] secretB = computeSharedSecret(keyPairB.getPrivate(), publicKeyAY.toByteArray(), p, g);
    String secretBString = new String(Base64.getEncoder().encode(secretB));

    logger.info("secret A equals to secret B: {}", secretAString.equals(secretBString));
  }

  public static KeyPair generateDHKeyPair(BigInteger p, BigInteger g)
      throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
    DHParameterSpec dhParameterSpec = new DHParameterSpec(p, g);
    KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("DiffieHellman");
    keyGenerator.initialize(dhParameterSpec);
    return keyGenerator.generateKeyPair();
  }

  public static byte[] computeSharedSecret(PrivateKey myPrivateKey, byte[] hisPublicKeyByte,
      BigInteger p, BigInteger g)
      throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
    KeyFactory keyFactory = KeyFactory.getInstance("DiffieHellman");
    BigInteger hisPublicKeyY = new BigInteger(1, hisPublicKeyByte);
    PublicKey hisPublicKey = keyFactory.generatePublic(new DHPublicKeySpec(hisPublicKeyY, p, g));

    KeyAgreement keyAgreement = KeyAgreement.getInstance("DiffieHellman");
    keyAgreement.init(myPrivateKey);
    keyAgreement.doPhase(hisPublicKey, true);
    byte[] secret = keyAgreement.generateSecret();
    logger.info("secret {} generated by privateKey {} with hisPublicKey {}", new String(Base64.getEncoder().encode(secret)),
        new String(Base64.getEncoder().encode(myPrivateKey.getEncoded())),
        hisPublicKeyY);
    return secret;
  }
}
