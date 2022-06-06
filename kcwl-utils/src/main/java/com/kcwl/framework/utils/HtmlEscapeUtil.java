package com.kcwl.framework.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.text.Normalizer;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * HTML 过滤
 */
@Slf4j
public class HtmlEscapeUtil {

    // 过滤
    public static String escape(String value) {
        if (StrUtil.isBlank(value)) {
            return value;
        }

        String cleanValue = Normalizer.normalize(value, Normalizer.Form.NFD);

        // Avoid null characters
        cleanValue = cleanValue.replaceAll("\0", "");

        // Avoid anything between script tags
        Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
        cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

        // Avoid anything in a src='...' type of expression
        scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

        scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

        // Remove any lonesome </script> tag
        scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
        cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

        // Remove any lonesome <script ...> tag
        scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

        // Avoid eval(...) expressions
        scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

        // Avoid expression(...) expressions
        scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

        // Avoid javascript:... expressions
        scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
        cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

        // Avoid vbscript:... expressions
        scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
        cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

        // Avoid onload= expressions
        scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");
        return cleanValue;
    }

    // 过滤数组
    public static String[] escape(String[] params) {
        if (params == null || params.length == 0) {
            return params;
        }
        String[] escapeValues = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            escapeValues[i] = escape(params[i]);
        }
        return escapeValues;
    }

    // 过滤 json 字符串
    public static String escapeJson(String jsonStr) {
        if (StrUtil.isBlank(jsonStr)) {
            return jsonStr;
        }
        try {
            JSONObject json = new JSONObject(jsonStr);
            escape(json);
            return json.toString();
        } catch (Exception ignored) {
        }
        return jsonStr;
    }

    // 遍历并过滤 JSONObject
    public static void escape(JSONObject jsonObject) {
        jsonObject.forEach((key, value) -> {
            if (value instanceof String) {
                jsonObject.set(key, escape((String) value));
            } else if (value instanceof JSONObject) {
                escape((JSONObject) value);
            } else if (value instanceof JSONArray) {
                escape((JSONArray) value);
            }
        });
    }

    // 遍历并过滤 JSONArray
    public static void escape(JSONArray jsonArray) {
        for (int i = 0, len = jsonArray.size(); i < len; i++) {
            Object obj = jsonArray.get(i);
            if (obj instanceof String) {
                jsonArray.set(i, escape((String) obj));
            } else if (obj instanceof JSONObject) {
                escape((JSONObject) obj);
            } else if (obj instanceof JSONArray) {
                escape((JSONArray) obj);
            }
        }
    }

    // 过滤 map
    public static void escape(Map<String, Object> map) {
        if (CollUtil.isEmpty(map)) {
            return;
        }
        map.forEach((key, value) -> {
            if (value instanceof String) {
                map.put(key, escape((String) value));
            } else if (value instanceof String[] && ((String[]) value).length > 0) {
                String[] valueArr = (String[]) value;
                String[] escapeValue = new String[valueArr.length];
                for (int i = 0; i < valueArr.length; i++) {
                    escapeValue[i] = escape(valueArr[i]);
                }
                map.put(key, escapeValue);
            }
        });
    }

}
