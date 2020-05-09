package com.ru.arsenal.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtil {
    public static String ALPHA = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String NUMBERIC = "0123456789";
    public static String ALPHA_NUMBERIC = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println("--- Generate 10 random String using Random() ---");
        for (int i = 0; i < 10; i++) {
            System.out.println(RandomUtil.generate_alphaString_using_Random(32));
        }

        System.out.println("\n--- Generate 10 random String using apache.RandomStringUtils ---");
        for (int i = 0; i < 10; i++) {
            System.out.println(RandomUtil.generate_alphaString_using_RandomUtils(32));
        }

        System.out.println("\n--- sameSeed_generate_sameResult ---");
        for (int i = 0; i < 20; i++) {
            sameSeed_generate_sameResult(3000);
        }
        System.out.println("\n--- defaultSeed_generate_different ---");
        for (int i = 0; i < 20; i++) {
            defaultSeed_generate_different();
        }

        System.out.println("\n--- using Math.random ---");
        for (int i = 0; i < 20; i++) {
            System.out.println(Math.random() * 100);
        }

        System.out.println("\n--- Generate 10 random String using ThreadLocalRandom() ---");
        for (int i = 0; i < 10; i++) {
            System.out.println(RandomUtil.generate_alphaString_using_ThreadLocalRandom(32));
        }


        System.out.println("\n--- Generate 10 random String using generate_alphaString_using_SecureRandom() ---");
        for (int i = 0; i < 10; i++) {
            System.out.println(RandomUtil.generate_alphaString_using_SecureRandom(32));
        }


    }

    public static String generate_alphaString_using_Random(int length) {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        int bound = ALPHA.length();
        for(int i=0; i< length; i++){
            buffer.append(ALPHA.charAt(random.nextInt(bound)));
        }
        return buffer.toString();
    }

    public static String generate_alphaString_using_ThreadLocalRandom(int length) {
        Random random = ThreadLocalRandom.current();
        StringBuffer buffer = new StringBuffer();
        int bound = ALPHA.length();
        for(int i=0; i< length; i++){
            buffer.append(ALPHA.charAt(random.nextInt(bound)));
        }
        return buffer.toString();
    }

    public static String generate_alphaString_using_SecureRandom(int length)
        throws NoSuchAlgorithmException {
//        Random random =new SecureRandom(); // default is SHA1PRNG
        Random random =SecureRandom.getInstance("SHA1PRNG");
        StringBuffer buffer = new StringBuffer();
        int bound = ALPHA.length();
        for(int i=0; i< length; i++){
            buffer.append(ALPHA.charAt(random.nextInt(bound)));
        }
        return buffer.toString();
    }


    public static void sameSeed_generate_sameResult(long seed){
        Random random1 = new Random(seed);
        Random random2 = new Random(seed);
        System.out.println(String.format("random1: %d, random2: %d", random1.nextInt(100), random2.nextInt(100)));
    }

    public static void defaultSeed_generate_different(){
        Random random1 = new Random();
        Random random2 = new Random();
        System.out.println(String.format("random1: %d, random2: %d", random1.nextInt(100), random2.nextInt(100)));
    }

    // using RandomStringUtils. which actually use Random() in the background
    public static String generate_alphaString_using_RandomUtils(int length){
       return RandomStringUtils.randomAlphabetic(length);
    }
}
