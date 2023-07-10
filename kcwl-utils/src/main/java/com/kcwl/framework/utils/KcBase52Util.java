package com.kcwl.framework.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author ckwl
 */
public class KcBase52Util {
    private static String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static int scale = 52;

    private KcBase52Util() {
    }

    public static String longToBase52(long num, int length) {
        StringBuilder sb = new StringBuilder();

        for(boolean var4 = false; num > (long)(scale - 1); num /= (long)scale) {
            int remainder = Long.valueOf(num % (long)scale).intValue();
            sb.append(chars.charAt(remainder));
        }

        sb.append(chars.charAt(Long.valueOf(num).intValue()));
        String value = sb.reverse().toString();
        return StringUtils.leftPad(value, length, '0');
    }

    public static long base52ToLong(String str) {
        str = str.replace("^0*", "");
        long num = 0L;

        for(int i = 0; i < str.length(); ++i) {
            int index = chars.indexOf(str.charAt(i));
            num += (long)((double)index * Math.pow((double)scale, (double)(str.length() - i - 1)));
        }

        return num;
    }
}
