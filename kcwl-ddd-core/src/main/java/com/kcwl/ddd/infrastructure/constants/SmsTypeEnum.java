package com.kcwl.ddd.infrastructure.constants;

/**
 * @author ckwl
 */

public enum SmsTypeEnum {
    MESSAGE_SMS_REGULAR(1, "普通的短信消息"),
    CARRIER_LOGIN_SIMS_VERILY(10, "司机登录短信验证码"),
    CARRIER_REGISTER_SIMS_VERILY(11, "司机注册短信验证码"),
    CARRIER_CHANGE_LOGIN_SIMS_VERILY(12, "司机修改登录密码"),
    CARRIER_PAY_SIMS_VERILY(13, "司机支付短信验证码"),
    CARRIER_FORGET_LOGIN_SIMS_VERILY(14, "司机忘记短信验证码"),
    KS_WEB_LOGIN_SIMS_VERILY(20, "客商web登录短信验证码"),
    KS_WEB_REGISTER_SIMS_VERILY(21, "客商web用户注册短信验证码"),
    KS_APP_LOGIN_SIMS_VERILY(30, "客商APP登录短信验证码"),
    KS_APP_REGISTER_SIMS_VERILY(31, "客商APP注册短信验证码"),
    PREPAYMENT_OPEN(40, "开通运费预付款"),
    PREPAYMENT_PAID_OFF(41, "本期预付费已还清"),
    PREPAYMENT_AVAILABLE_AMOUNT(42, "运费预付费可用额度"),
    PREPAYMENT_REPAID(43, "本期预付费待还款"),
    OIL_GAS_CONSUME(50, "油气消费"),
    FREIGHT_COLLECTION_SET(60, "设置运费代收人"),
    FREIGHT_COLLECTION_CANCEL(61, "解除运费代收人关系");

    private int code;
    private String desc;

    private SmsTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
