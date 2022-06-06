package com.kcwl.framework.utils;

import java.util.Random;

/**
 * @author ckwl
 */
public class RandomString {

    private static final String SOURCES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

    public static String generate(int length) {
        return generate(new Random(), length);
    }

    public static String generate(Random random, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = SOURCES.charAt(random.nextInt(SOURCES.length()));
        }
        return new String(text);
    }
}
