package com.kcwl.framework.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

/**
 * 敏感信息编码
 */
public class SensitiveEncodeUtil {

    /**
     * SSID md5 盐
     */
    private static final String SSID_ENCODE_SALT = "vS3!Q8";

    /**
     * USER_ID md5 盐
     */
    private static final String USERID_ENCODE_SALT = "xSY)o9";

    /**
     * 密码 md5 盐
     */
    private static final String PASSWORD_ENCODE_SALT_PREFIX = "88e8221a2fdeb4be85181dc97fff8a92";
    private static final String PASSWORD_ENCODE_SALT_SUFFIX = "e34a1d96db351fee7706a471d38701c3";

    /**
     * sessionId 摘要
     *
     * @param sessionId ssid
     */
    public static String encodeSessionId(String sessionId) {
        return encode(sessionId, SSID_ENCODE_SALT);
    }

    /**
     * userId 摘要
     *
     * @param userId userId
     */
    public static String encodeUserId(String userId) {
        return encode(userId, USERID_ENCODE_SALT);
    }


    /**
     * 密码摘要
     * md5( md5(key1) + password + md5(key2) )
     */
    public static String encodePassword(String password) {
        return SecureUtil.md5(PASSWORD_ENCODE_SALT_PREFIX + password + PASSWORD_ENCODE_SALT_SUFFIX);
    }

    /**
     * md5(param + salt)
     */
    public static String encode(String param, String salt) {
        if (StrUtil.isBlank(param)) {
            return param;
        }
        return SecureUtil.md5(param + salt);
    }

}
