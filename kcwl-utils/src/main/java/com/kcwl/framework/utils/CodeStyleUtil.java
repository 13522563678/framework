package com.kcwl.framework.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ckwl
 */
public class CodeStyleUtil {

    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 驼峰转下划线,最后转为大写
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString().toLowerCase();
    }

    /**
     * 下划线转驼峰,正常输出
     * @param name
     * @return
     */
    public static String lineToHump(String name) {
        if ( (name == null) || (name.isEmpty()) ) {
            return "";
        }
        StringBuilder result = new StringBuilder(name.length());
        boolean isUnderline = false;
        result.append(Character.toLowerCase(name.charAt(0)));
        for ( int i=1; i<name.length(); i++ ) {
            char ch = name.charAt(i);
            if ( ch == '_' ) {
                isUnderline = true;
            } else {
                if ( isUnderline ) {
                    result.append(Character.toUpperCase(ch));
                    isUnderline = false;
                } else {
                    result.append(Character.toLowerCase(ch));
                }
            }
        }
        return result.toString();
    }
}
