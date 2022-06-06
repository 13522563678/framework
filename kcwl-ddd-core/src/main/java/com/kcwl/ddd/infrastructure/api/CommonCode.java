package com.kcwl.ddd.infrastructure.api;

/**
 * @author ckwl
 */

public enum CommonCode {
    SUCCESS("000000200", "请求成功"),

    AUTH_ERROR_CODE("100101","秘钥无效"),
    PARAM_VALID_ERROR_CODE("100102","字段为空"),
    DATA_LOGIC_ERROR_CODE("100103","字段格式不正确"),

    FAIL("000000201", "请求失败"),
    DATA_MESSAGE_NULL("000000001","数据报文为空"),
    DATA_MESSAGE_UN_EXIST("000000002","报文指令不存在"),
    DATA_MESSAGE_DECODE_FAIL("000000003","报文无法解密"),
    JSON_DECODE_FAIL("000000004","JSON decode失败"),
    SYS_ERROR("000000005","系统繁忙"),
    FIELD_NULL("000000006","字段为空"),
    FIELD_ERROR("000000007","字段格式不正确"),
    UN_LOGIN("000000008","未登录"),
    UN_AUTH("000000011","未认证"),
    OTHER_EQUIPMENT_LOGIN("000000009","该账户在别的设备登录"),
    STRING_LENGTH_ERROR("000000010","字符串长度不符合格式"),
    INVALID_USER_AGENT("000000900","该功能需要升级您的APP版本。"),
    APP_VERSION_UPDATE("000000901","该功能需要升级您的APP版本。"),
    APP_MAINTENANCE("0000005902","系统正在升级中，给您带来的不便，敬请谅解。"),
    API_VERSION_NOT_SUPPORT("0000005903","不支持该请求"),
    UNKNOWN_ERROR_CODE("0000000999","未知错误类型");


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
