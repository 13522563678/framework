package com.kcwl.framework.utils;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

/**
 * 脱敏工具类
 * 给 Spring 管理
 *
 * @see DesensitizedUtil
 * @see SensitiveEncodeUtil
 */
@Component("deSense")
public class DesensitizedComponent {

    // 是否为 null
    private static boolean isNull(Object param) {
        return param == null || "".equals(param) || "null".equals(param);
    }

    /**
     * 隐藏
     *
     * @param param 脱敏字符串
     * @param start 起始下标
     * @param end   结束下标
     */
    public String hide(Object param, int start, int end) {
        if (isNull(param)) {
            return null;
        }
        return StrUtil.hide(String.valueOf(param), start, end + 1);
    }

    // 手机号
    public String mobile(Object param) {
        if (isNull(param)) {
            return null;
        }
        return DesensitizedUtil.mobilePhone(String.valueOf(param));
    }

    // ssid、session_id
    public String ssid(Object param) {
        if (isNull(param)) {
            return null;
        }
        return SensitiveEncodeUtil.encodeSessionId(String.valueOf(param));
    }

    // user_id
    public String userId(Object param) {
        if (isNull(param)) {
            return null;
        }
        return SensitiveEncodeUtil.encodeUserId(String.valueOf(param));
    }

    // 密码脱敏
    public String password(Object param) {
        if (isNull(param)) {
            return null;
        }
        return SensitiveEncodeUtil.encodePassword(String.valueOf(param));
    }

    // 身份证，保留前 3位，后 4位，长度不足 7 返回 null
    public String idCard(Object param) {
        if (isNull(param)) {
            return null;
        }
        return DesensitizedUtil.idCardNum(String.valueOf(param), 3, 4);
    }

    // 银行卡号
    public String bankCard(Object param) {
        if (isNull(param)) {
            return null;
        }
        return DesensitizedUtil.bankCard(String.valueOf(param));
    }

    // 驾驶证，保留前 3，后 4，与 身份证相同 直接复用。
    public String driverId(Object param) {
        return idCard(param);
    }

}
