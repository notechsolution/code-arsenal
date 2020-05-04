package com.ru.arsenal.crypto;

import java.util.Random;

public class RandomUtil {
    public static String ALPHA = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String NUMBERIC = "0123456789";
    public static String ALPHA_NUMBERIC = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static void main(String[] args) {
        System.out.println("--- Start RandomUtils ---");
        for (int i = 0; i < 100; i++) {
            System.out.println(RandomUtil.generateAlphaString(10));
        }

    }

    public static String generateAlphaString(int length) {
        Random seed = new Random();
        StringBuffer buffer = new StringBuffer();
        int bound = ALPHA.length();
        for(int i=0; i< length; i++){
            buffer.append(ALPHA.charAt(seed.nextInt(bound)));
        }
        return buffer.toString();
    }
}
