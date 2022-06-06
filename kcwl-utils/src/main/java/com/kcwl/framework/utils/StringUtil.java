package com.kcwl.framework.utils;

import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author 姚华成
 * @date 2017-12-14
 */
public class StringUtil extends StringUtils {

    public static final String EMPTY = "";
    public static final String NULL_STRING = "null";

    /**
     * 将字符串转换成整数，如果为空或者格式错误，则返回defaultValue
     *
     * @param str
     * @param defaultInt
     * @return
     */
    public static int toInt(String str, int defaultInt) {
        if (isEmpty(str)) {
            return defaultInt;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultInt;
        }
    }

    /**
     * 将字符串转换成整数，如果为空或者格式错误，则返回defaultValue
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static long toLong(String str, long defaultValue) {
        long val = defaultValue;
        try {
            if ( !isEmpty(str) ) {
                val = Long.parseLong(str);
            }
        } catch (NumberFormatException e) {
        }
        return val;
    }

    public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultStr) {
        return hasText(str) ? str : defaultStr;
    }

    public static <T extends CharSequence> T defaultIfEmpty(final T str, final T defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    public static String defaultString(final String str) {
        return str == null ? EMPTY : str;
    }

    public static String defaultString(final String str, final String defaultStr) {
        return str == null ? defaultStr : str;
    }

    public static boolean isBlank(CharSequence cs) {
        return !hasText(cs);
    }
    public static boolean isNotBlank(CharSequence cs) {
        return hasText(cs);
    }

    public static boolean isNullOrEmpty(String str) {
        return (str == null) || str.isEmpty() || str.equals(NULL_STRING);
    }
}
