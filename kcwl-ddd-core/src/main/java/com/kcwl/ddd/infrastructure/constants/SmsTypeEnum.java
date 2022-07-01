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
    KS_WEB_LOGIN_SIMS_VERILY(20, "发运web登录短信验证码"),
    KS_WEB_REGISTER_SIMS_VERILY(21, "发运web用户注册短信验证码"),
    KS_WEB_FORGOT_PASSWORD_VERILY(22, "发运web用户忘记密码短信验证码"),
    KS_WEB_CHANGE_PASSWORD_VERILY(23, "发运web用户重置密码短信验证码"),
    KS_WEB_RESET_PAYMENT_PASSWORD_VERILY(24, "发运web用户重置支付密码短信验证码"),
    KS_WEB_ACTIVATE_ACCOUNT_VERILY(25, "发运web激活账户短信验证码"),
    SHIPPER_CHANGE_LOGIN_PWD_SIMS_VERILY(26, "货主修改登录密码短信验证码"),
    SHIPPER_DESTROY_SIMS_VERILY(27, "货主注销用户短信验证码"),
    SHIPPER_CHANGE_MOBILE_VERILY(28, "货主修改用户手机号"),
    KS_APP_LOGIN_SIMS_VERILY(30, "发运APP登录短信验证码"),
    KS_APP_REGISTER_SIMS_VERILY(31, "发运APP注册短信验证码"),
    KS_APP_FORGOT_PASSWORD_VERILY(32, "发运APP用户忘记密码短信验证码"),
    KS_APP_RESET__PASSWORD_VERILY(33, "发运APP用户重置密码短信验证码"),
    CARRIER_PREPAYMENT_OPEN(40, "司机开通运费预付款短信验证码"),
    CARRIER_PREPAYMENT_PAID_OFF(41, "司机本期预付费已还清短信验证码"),
    CARRIER_PREPAYMENT_AVAILABLE_AMOUNT(42, "司机运费预付费可用额度短信验证码"),
    PCARRIER_REPAYMENT_REPAID(43, "司机本期预付费待还款短信验证码"),
    CARRIER_OIL_GAS_CONSUME(50, "司机油气消费短信验证码"),
    CARRIER_FREIGHT_COLLECTION_SET(60, "司机设置运费代收人短信验证码"),
    CARRIER_FREIGHT_COLLECTION_CANCEL(61, "司机解除运费代收人关系短信验证码"),
    CRM_COALMINE_REFUND_VERILY(71, "crm运销部退款短信验证码");

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
