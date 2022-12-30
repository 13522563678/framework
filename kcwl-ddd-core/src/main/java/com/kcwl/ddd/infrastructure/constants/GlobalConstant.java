package com.kcwl.ddd.infrastructure.constants;

/**
 * @author ckwl
 */
public class GlobalConstant {
    public static final String DB_TENANT_FIELD_NAME = "platform_no";
    public static final String PARAM_TENANT_FIELD_NAME = "platformNo";
    public static final String AGENT_TENANT_FIELD_NAME = "x-tenant-id";
    public static final String COOKIE_TENANT_FIELD_NAME = "tenantId";
    public static final String APP_SECRET_FIELD_NAME = "appSecret";
    public static final String KC_APP_ID = "x-kc-appId";
    public static final String KC_APP_ONCE = "x-kc-once";
    public static final String KC_APP_SIGN = "x-kc-sign";
    public static final String KC_APP_TIMESTAMP = "x-kc-timestamp";

    public static final String UNIOIN_TENANT_ID_SUFFIX= "00";
    public static final String UNIOIN_TENANT_ID_V2 = "200";
    public static final String UNIOIN_TENANT_ID = "100";
    public static final String KCWL_TENANT_ID = "101";
    public static final String BOOL_FLAG_TRUE = "true";
    public static final String NULL_STRING = "null";
    public static final String EMPTY_STRING = "";

    public static final int BIZ_ERROR_CODE_LENGHT = 5;


    /**
     * 请求头的灰度标记key
     */
    public static final String GRAY_REQUEST_HEADER_KEY = "x-gray-mark";

    public static final String KC_TRACE = "kctrace";
    public static final String KC_TOKEN = "kctoken";
}
