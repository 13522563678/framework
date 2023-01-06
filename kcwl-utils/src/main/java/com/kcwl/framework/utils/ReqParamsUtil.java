package com.kcwl.framework.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReqParamsUtil {
    public static String composeStringToSign(String path, Map<String, String> headers) {
        StringBuilder sb = new StringBuilder();
        List<String> list = headers.keySet().stream().sorted().collect(Collectors.toList());
        for (String s : list) {
            sb.append(s).append(":").append(headers.get(s));
        }
        sb.append(path);
        return sb.toString();
    }
}
