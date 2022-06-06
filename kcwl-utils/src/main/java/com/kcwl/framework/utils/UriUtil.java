package com.kcwl.framework.utils;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * @author 姚华成
 * @date 2018-03-20
 */
public class UriUtil {
    public static String appendParam(String rawUrl, String name, String value) {
        if (rawUrl == null) {
            return null;
        }
        return UriComponentsBuilder.fromUriString(rawUrl).queryParam(name, value).toUriString();
    }

    public static String appendParams(String rawUrl, Map<String, String> nvs) {
        if (StringUtil.isEmpty(rawUrl)) {
            return null;
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(rawUrl);
        for (Map.Entry<String, String> nv : nvs.entrySet()) {
            builder.queryParam(nv.getKey(), nv.getValue());
        }
        return builder.toUriString();
    }
}
