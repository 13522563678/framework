package com.kcwl.ddd.infrastructure.api;

/**
 * @author ckwl
 */

public enum CommonCode {
    SUCCESS("000000200", "请求成功"),

    AUTH_ERROR_CODE("00101","秘钥无效"),
    PARAM_VALID_ERROR_CODE("00102","字段为空"),
    DATA_LOGIC_ERROR_CODE("00103","字段格式不正确"),

    FAIL("00104", "请求失败"),
    DATA_MESSAGE_NULL("00001","数据报文为空"),
    DATA_MESSAGE_UN_EXIST("00002","报文指令不存在"),
    DATA_MESSAGE_DECODE_FAIL("00003","报文无法解密"),
    JSON_DECODE_FAIL("00004","JSON decode失败"),
    SYS_ERROR("00005","系统繁忙"),
    FIELD_NULL("0006","字段为空"),
    FIELD_ERROR("00007","字段格式不正确"),
    UN_LOGIN("00008","未登录"),
    UN_AUTH("00011","未认证"),
    OTHER_EQUIPMENT_LOGIN("00009","该账户在别的设备登录"),
    STRING_LENGTH_ERROR("00010","字符串长度不符合格式"),
    INVALID_USER_AGENT("00900","该功能需要升级您的APP版本。"),
    APP_VERSION_UPDATE("00901","该功能需要升级您的APP版本。"),
    APP_MAINTENANCE("00902","系统正在升级中，给您带来的不便，敬请谅解。"),
    API_VERSION_NOT_SUPPORT("00903","不支持该请求"),
    UNKNOWN_ERROR_CODE("00999","未知错误类型");


    private String code;
    private String description;

    private CommonCode(String code, String description){
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
