package com.kcwl.framework.utils;

import java.util.Map;

/**
 * @Author: mengzr
 * @Date: 2019/9/12 15:36
 */
public class BeanMapUtil {
    public static long getLong(Map<String, String> map, String fieldName, long defaultValue){
        if ( map != null ) {
            Object value = map.get(fieldName);
            if ((value != null) && (!value.toString().isEmpty())) {
                return StringUtil.toLong(value.toString(), defaultValue);
            }
        }
        return defaultValue;
    }

    public static String getString(Map<String, String> map, String fieldName, String defaultValue) {
        if ( map != null ) {
            Object value = map.get(fieldName);
            if (value != null) {
                return value.toString();
            }
        }
        return defaultValue;
    }

    public static int getInteger(Map<String, String> map, String fieldName, int defaultValue) {
        if ( map != null ) {
            Object value = map.get(fieldName);
            if ((value != null) && (!value.toString().isEmpty())) {
                return StringUtil.toInt(value.toString(), defaultValue);
            }
        }
        return defaultValue;
    }

    public static int parseInteger(Map<String, Object> map, String fieldName, int defaultValue) {
        if ( map != null ) {
            Object value = map.get(fieldName);
            if ((value != null) && (!value.toString().isEmpty())) {
                return StringUtil.toInt(value.toString(), defaultValue);
            }
        }
        return defaultValue;
    }
}
