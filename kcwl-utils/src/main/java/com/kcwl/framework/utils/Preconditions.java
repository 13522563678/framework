package com.kcwl.framework.utils;

import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.exception.BaseException;

/**
 * 断言工具类
 */
public class Preconditions {

    /**
     * @throws BaseException 抛出 CommonCode.ASSERT_FAILED
     */
    public static void check(boolean expression, String message) {
        check(expression, CommonCode.ASSERT_FAILED, message);
    }

    /**
     * @throws BaseException 根据 CommonCode 抛出异常，默认抛出消息内容
     */
    public static void check(boolean expression, CommonCode commonCode) {
        check(expression, commonCode, null);
    }

    /**
     * @throws BaseException 根据 CommonCode 抛出异常，指定抛出消息内容
     */
    public static void check(boolean expression, CommonCode commonCode, String message) {
        if (!expression) {
            throw new BaseException(commonCode.getCode(), message);
        }
    }

}
