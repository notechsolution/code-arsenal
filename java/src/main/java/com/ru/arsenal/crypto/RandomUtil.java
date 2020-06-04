package com.ru.arsenal.crypto;

import java.util.Random;

public class RandomUtil {
    public static String ALPHA = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String NUMBERIC = "0123456789";
    public static String ALPHA_NUMBERIC = ALPHA + NUMBERIC;

    public static void main(String[] args) {
        System.out.println("--- Start RandomUtils ---");
        for (int i = 0; i < 5; i++) {
            System.out.println(RandomUtil.generateAlpha(10));
        }

        for (int i = 0; i < 5; i++) {
            System.out.println(RandomUtil.generateAlphaNumber(10));
        }

        for (int i = 0; i < 5; i++) {
            System.out.println(RandomUtil.generateInt(10000));
        }
    }

    public static String generateAlpha(int length) {
        return generateRandomString(length, ALPHA);
    }

    public static String generateAlphaNumber(int length) {
        return generateRandomString(length, ALPHA_NUMBERIC);
    }

    public static int generateInt(int bound){
        Random seed = new Random();
        return seed.nextInt(bound);
    }

    private static String generateRandomString(int length, String sourceCharacters){
        Random seed = new Random();
        StringBuffer buffer = new StringBuffer();
        int bound = sourceCharacters.length();
        for(int i=0; i< length; i++){
            buffer.append(sourceCharacters.charAt(seed.nextInt(bound)));
        }
        return buffer.toString();
    }
}
