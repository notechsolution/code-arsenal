package com.ru.arsenal.crypto;

import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtil {
    public static String ALPHA = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String NUMBERIC = "0123456789";
    public static String ALPHA_NUMBERIC = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static void main(String[] args) {
        System.out.println("--- Generate 10 random String using Random() ---");
        for (int i = 0; i < 10; i++) {
            System.out.println(RandomUtil.generate_alphaString_using_Random(32));
        }

        System.out.println("\n--- Generate 10 random String using apache.RandomStringUtils ---");
        for (int i = 0; i < 10; i++) {
            System.out.println(RandomUtil.generate_alphaString_using_RandomUtils(32));
        }

    }

    public static String generate_alphaString_using_Random(int length) {
        Random seed = new Random();
        StringBuffer buffer = new StringBuffer();
        int bound = ALPHA.length();
        for(int i=0; i< length; i++){
            buffer.append(ALPHA.charAt(seed.nextInt(bound)));
        }
        return buffer.toString();
    }

    // using RandomStringUtils. which actually use Random() in the background
    public static String generate_alphaString_using_RandomUtils(int length){
       return RandomStringUtils.randomAlphabetic(length);
    }
}
