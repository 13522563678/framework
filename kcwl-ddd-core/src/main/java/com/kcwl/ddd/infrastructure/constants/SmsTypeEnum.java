package com.kcwl.ddd.infrastructure.constants;

/**
 * @author kcwl
 */

public enum SmsTypeEnum {
    MESSAGE_SMS_REGULAR(1, "普通的短信消息"),
    CARRIER_LOGIN_SIMS_VERILY(10, "司机登录短信验证码"),
    CARRIER_REGISTER_SIMS_VERILY(11, "司机注册短信验证码"),
    CARRIER_CHANGE_LOGIN_SIMS_VERILY(12, "司机修改登录密码"),
    CARRIER_PAY_SIMS_VERILY(13, "司机支付短信验证码"),
    CARRIER_FORGET_LOGIN_SIMS_VERILY(14, "司机忘记短信验证码"),
    SHIPPER_WEB_LOGIN_SIMS_VERILY(20, "发运web登录短信验证码"),
    SHIPPER_WEB_REGISTER_SIMS_VERILY(21, "发运web用户注册短信验证码"),
    SHIPPER_WEB_FORGOT_PASSWORD_VERILY(22, "发运web用户忘记密码短信验证码"),
    SHIPPER_WEB_CHANGE_PASSWORD_VERILY(23, "发运web用户重置密码短信验证码"),
    SHIPPER_WEB_RESET_PAYMENT_PASSWORD_VERILY(24, "发运web用户重置支付密码短信验证码"),
    SHIPPER_WEB_ACTIVATE_ACCOUNT_VERILY(25, "发运web激活账户短信验证码"),
    SHIPPER_WEB__CHANGE_LOGIN_PWD_SIMS_VERILY(26, "发运web货主修改登录密码短信验证码"),
    SHIPPER_WEB_TRANSFER_VERILY(27, "发运web转账短信验证码"),
    SHIPPER_WEB__CHANGE_MOBILE_VERILY(28, "发运web货主修改用户手机号"),
    SHIPPER_WEB__DESTROY_SIMS_VERILY(29, "发运web货主注销用户短信验证码"),
    SHIPPER_APP_LOGIN_SIMS_VERILY(30, "发运APP登录短信验证码"),
    SHIPPER_APP_REGISTER_SIMS_VERILY(31, "发运APP注册短信验证码"),
    SHIPPER_APP_FORGOT_PASSWORD_VERILY(32, "发运APP用户忘记密码短信验证码"),
    SHIPPER_APP_RESET__PASSWORD_VERILY(33, "发运APP用户重置密码短信验证码"),
    SHIPPER_WEB_PARTNER_REFUND(40, "发运web合作关系申请退款短信验证码"),
    ICMS_LOGIN_VERILY(71, "数智平台登录短信验证码"),
    ICMS_RETRIEVE_PASSWORD_VERILY(72, "数智平台找回密码短信验证码"),
    ICMS_PARTNER_REFUND(73, "数智平台合作关系申请退款短信验证码"),
    BPMS_LOGIN_VERILY(81, "业绩系统登录短信验证码"),
    BPMS_RETRIEVE_PASSWORD_VERILY(82, "业绩系统找回密码短信验证码");

    private int code;
    private String desc;

    SmsTypeEnum(int code, String desc) {
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
