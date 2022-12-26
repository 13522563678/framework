package com.kcwl.framework.utils;

import org.apache.commons.lang3.StringUtils;

public class DecimalConvert {
    private static String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static int scale = 62;

    /**
     * 十进制转62进制
     * @param num
     * @param length
     * @return
     */
    public static String longToBase62(long num, int length) {

        StringBuilder sb = new StringBuilder();

        int remainder = 0;

        while (num > scale - 1) {

            remainder = Long.valueOf(num % scale).intValue();

            sb.append(chars.charAt(remainder));

            num = num / scale;

        }

        sb.append(chars.charAt(Long.valueOf(num).intValue()));

        String value = sb.reverse().toString();

        return StringUtils.leftPad(value, length, '0');

    }

    /**
     * 62进制转10进制数
     * @param str
     * @return
     */
    public static long base62ToLong(String str) {

        str = str.replace("^0*", "");

        long num = 0;

        int index = 0;

        for (int i = 0; i < str.length(); i++) {

            index = chars.indexOf(str.charAt(i));

            num += (long) (index * (Math.pow(scale, str.length() - i - 1)));

        }

        return num;
    }
}

