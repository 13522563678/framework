package com.kcwl.ddd.infrastructure.utils;

import com.kcwl.ddd.infrastructure.constants.PrefixConstant;

/**
 * RedisKey的拼接工具类
 * @author ckwl
 */
public class RedisKeyUtil {
    public static String getSmsVerifyKey(String mobile, int msgType) {
        return PrefixConstant.REDIS_SMS_VERIFY_CODE + msgType + ":" + mobile;
    }
}
