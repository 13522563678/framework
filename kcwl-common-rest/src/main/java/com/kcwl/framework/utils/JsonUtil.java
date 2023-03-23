package com.kcwl.framework.utils;

import com.google.gson.*;

import java.util.*;
import java.util.Map.Entry;

/**
 * 使用Gson把json字符串转成Map
 * @author lianqiang
 * @date 2014/06/12
 */
public class JsonUtil {
    private static Gson gson = new Gson();

    /**
     * 获取JsonObject
     *
     * @param json
     * @return
     */
    public static JsonObject parseJson(String json) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(json).getAsJsonObject();
        return jsonObj;
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static JsonArray parseJsonArray(String json) {
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();
        return jsonArray;
    }

    /**
     * 根据json字符串返回Map对象
     *
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(String json) {
        return JsonUtil.toMap(JsonUtil.parseJson(json));
    }

    /**
     * 将JSONObjec对象转换成Map-List集合
     *
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(JsonObject json) {
        Map<String, Object> map = new HashMap<String, Object>();
        Set<Entry<String, JsonElement>> entrySet = json.entrySet();
        for (Iterator<Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext(); ) {
            Entry<String, JsonElement> entry = iter.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JsonArray) {
                map.put((String) key, toList((JsonArray) value));
            } else if (value instanceof JsonObject) {
                map.put((String) key, toMap((JsonObject) value));
            } else if (value instanceof JsonPrimitive ) {
                if ( !isNullString((JsonPrimitive)value) ) {
                    map.put((String) key, value);
                }
            } else if ( value instanceof JsonNull ) {
                //空对象时不放到map中
            } else {
                map.put((String) key, value);
            }
        }
        return map;
    }

    public static void copyToMap(JsonObject fromJson, Map toMap) {
        Set<Map.Entry<String, JsonElement>> entrySet = fromJson.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            toMap.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 将JSONArray对象转换成List集合
     *
     * @param json
     * @return
     */
    public static List<Object> toList(JsonArray json) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < json.size(); i++) {
            Object value = json.get(i);
            if (value instanceof JsonArray) {
                list.add(toList((JsonArray) value));
            } else if (value instanceof JsonObject) {
                list.add(toMap((JsonObject) value));
            } else {
                list.add(value);
            }
        }
        return list;
    }


    public static boolean isNullObject(Object obj) {
        if ( (obj == null) || (obj instanceof JsonNull) ) {
            return true;
        }
        return false;
    }
    public static boolean isNullString(JsonPrimitive obj) {
        return "null".equals(obj.getAsString());
    }

    public static boolean isJsonPrimitive(Object obj) {
        if ( !isNullObject(obj) && obj instanceof JsonPrimitive ) {
            return true;
        }
        return false;
    }

    public static Gson getGson() {
        return gson;
    }
}