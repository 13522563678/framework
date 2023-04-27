package com.kcwl.framework.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author 姚华成
 * @date 2018-01-30
 */
public class RequestUtil {
    private static final String[] IP_HEADER_NAMES = new String[]
            {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP"};
    private static final String IP_UNKNOWN = "unknown";

    private RequestUtil() {
    }

    public static Map<String, String> getHeaderMap(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>(8);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headerMap.put(key, value);
        }
        return headerMap;
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        if (StringUtil.hasText(cookieName)) {
            return getCookieValue(request, cookieName::equals);
        }
        return null;
    }

    public static String getCookieValueIgnoreCase(HttpServletRequest request, String cookieName) {
        if (StringUtil.hasText(cookieName)) {
            return getCookieValue(request, cookieName::equalsIgnoreCase);
        }
        return null;
    }

    private static String getCookieValue(HttpServletRequest request, Function<String, Boolean> function) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (function.apply(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static String getClientIpAddr(HttpServletRequest request) {
        for (String headerName : IP_HEADER_NAMES) {
            String ip = request.getHeader(headerName);
            if (StringUtil.hasText(ip) && !IP_UNKNOWN.equalsIgnoreCase(ip)) {
                int index = ip.indexOf(',');
                if (index > 0) {
                    return ip.substring(0, index);
                } else {
                    return ip;
                }
            }
        }
        return request.getRemoteAddr();
    }

    /**
     * 获得请求路径
     *
     * @param request
     * @return
     */
    public static String getRequestPath(HttpServletRequest request) {
        String requestPath = request.getRequestURI() ;
        // 去掉其他参数
        if (requestPath.indexOf("&") > -1) {
            requestPath = requestPath.substring(0, requestPath.indexOf("&"));
        }
        requestPath = requestPath.substring(request.getContextPath().length() + 1);// 去掉项目路径
        return requestPath;
    }
}
