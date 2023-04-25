package com.kcwl.ddd.infrastructure.api;

/**
 * @author ckwl
 */

public enum CommonCode {
    /**
     * 定义接口错误码
     */
    SUCCESS("000000200", "请求成功"),
    FAIL("00099", "系统开小差请稍后再试"),
    DATA_MESSAGE_NULL("00001","数据报文为空"),
    DATA_MESSAGE_UN_EXIST("00002","报文指令不存在"),
    DATA_MESSAGE_DECODE_FAIL("00003","报文无法解密"),
    JSON_DECODE_FAIL("00004","JSON decode失败"),
    SYS_ERROR("00005","系统繁忙"),
    FIELD_NULL("00006","字段为空"),
    FIELD_ERROR("00007","字段格式不正确"),
    UN_LOGIN("00008","未登录"),
    OTHER_EQUIPMENT_LOGIN("00009","该账户在别的设备登录"),
    UN_AUTH("00010","未认证"),
    STRING_LENGTH_ERROR("00011","字符串长度不符合格式"),
    AUTH_ERROR_CODE("00012","秘钥无效"),
    PARAM_VALID_ERROR_CODE("00013","字段为空"),
    DATA_LOGIC_ERROR_CODE("00014","字段格式不正确"),
    REQUEST_NOT_FOUND("00015", "请求不存在"),
    REQUEST_METHOD_NOT_SUPPORT("00016", "不支持请求类型"),
    ACCESS_DB_FAIL("00017", "无效的数据"),
    ERROR_SECRET_KEY("00018", "不支持的秘钥格式"),
    ERROR_SECRET_GROUP("00019", "超出秘钥分组范围"),
    BODY_EMPTY_FAIL("00020","请求参数不能为空"),
    RESOURCE_LOCK_FAIL("00021","无法获取资源"),
    API_MOCK_FAIL("00022","执行mock接口失败"),
    CONTAIN_SENSITIVE_WORDS("00023","输入内容不正确"),
    REQUEST_UNDER_RISK("00024","请求存在风险"),
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
