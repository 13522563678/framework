package com.kcwl.framework.utils;

import cn.hutool.core.convert.Convert;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从map参数中获取基本数据类型
 * @author ckwl
 */
public class MapParamUtil {

    public static String getString(Map<String, Object> paramsMap, String fieldName) {
        String strVal = null;
        Object objVal = paramsMap.get(fieldName);
        if (JsonUtil.isJsonPrimitive(objVal)) {
            strVal = ((JsonPrimitive) objVal).getAsString();
        } else if (objVal != null) {
            strVal = objVal.toString();
        }
        return strVal;
    }

    public static Integer getInteger(Map<String, Object> paramsMap, String fieldName) {
        Integer iVal = null;
        Object objVal = paramsMap.get(fieldName);
        if (JsonUtil.isJsonPrimitive(objVal)) {
            iVal = ((JsonPrimitive) objVal).getAsInt();
        } else if (objVal != null) {
            iVal = Convert.toInt(objVal);
        }
        return iVal;
    }

    public static Long getLong(Map<String, Object> paramsMap, String fieldName) {
        Long iVal = null;
        Object objVal = paramsMap.get(fieldName);
        if (JsonUtil.isJsonPrimitive(objVal)) {
            iVal = ((JsonPrimitive) objVal).getAsLong();
        } else if (objVal != null) {
            iVal = Convert.toLong(objVal);
        }
        return iVal;
    }

    public static Double getDouble(Map<String, Object> paramsMap, String fieldName) {
        Double iVal = null;
        Object objVal = paramsMap.get(fieldName);
        if (JsonUtil.isJsonPrimitive(objVal)) {
            iVal = ((JsonPrimitive) objVal).getAsDouble();
        } else if (objVal != null) {
            iVal = Convert.toDouble(objVal);
        }
        return iVal;
    }

    public static Boolean getBoolean(Map<String, Object> paramsMap, String fieldName) {
        Boolean bVal = null;
        Object objVal = paramsMap.get(fieldName);
        if (JsonUtil.isJsonPrimitive(objVal)) {
            bVal = ((JsonPrimitive) objVal).getAsBoolean();
        } else if (objVal != null) {
            bVal = Convert.toBool(objVal);
        }
        return bVal;
    }

    /**
     * 把value为JsonPrimitive的map转成普通对象类型的Map
     * @param params
     * @return
     */
    public static HashMap<String, Object> convertPrimitiveMap(Map params) {
        HashMap<String, Object> objMap = new HashMap<String, Object>(params.size());
        params.forEach((k, v) -> {
            if (v instanceof JsonPrimitive) {
                objMap.put(k.toString(), ((JsonPrimitive) v).getAsString());
            } else if (v instanceof JsonArray) {
                objMap.put(k.toString(), jsonArrayToList((JsonArray) v));
            } else {
                objMap.put(k.toString(), v.toString());
            }
        });
        return objMap;
    }

    /**
     * 把map的value对象转为List形式的value
     * @param param
     * @return
     */
    public static Map<String, String[]> convertToMultiValueMap(Map<String, Object> param) {
        Map<String, String[]> multiValueMap = new HashMap<String, String[]>();
        for (String key : param.keySet()) {
            Object o = param.get(key);
            if (o instanceof List) {
                multiValueMap.put(key, listToStringArray((List) o));
            } else {
                multiValueMap.put(key, new String[]{castToString(o)});
            }
        }
        return multiValueMap;
    }

    public static Map<String, String[]> convertToMultiValueMapV2(Map<String, Object> param) {
        Map<String, String[]> multiValueMap = new HashMap<String, String[]>();
        for (String key : param.keySet()) {
            Object o = param.get(key);
            if (o instanceof List) {
                multiValueMap.put(key, listToStringArray((List) o));
            } else {
                multiValueMap.put(key, new String[]{castToStringV2(o)});
            }
        }
        return multiValueMap;
    }

    public static String[] listToStringArray(List list) {
        String[] arrays = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arrays[i] = castToString(list.get(i));
        }
        return arrays;
    }

    public static String castToString(Object o) {
        return o == null ? null : o.toString().replaceAll("\"", "");
    }

    public static String castToStringV2(Object o) {
        return o == null ? null : o.toString();
    }

    public static List<Object> jsonArrayToList(JsonArray json) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < json.size(); i++) {
            Object value = json.get(i);
            if (value instanceof JsonArray) {
                list.add(jsonArrayToList((JsonArray) value));
            } else if (value instanceof JsonPrimitive) {
                list.add(((JsonPrimitive) value).getAsString());
            } else {
                list.add(value);
            }
        }
        return list;
    }
}
