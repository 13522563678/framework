package com.kcwl.ddd.domain.entity;

/**
 * 返回值封装，用于方法之间调用返回，非浏览器调用返回值
 */
public class BaseResult {
    /**
     * 业务code
     */
    private Integer code;

    /**
     * 业务信息
     */
    private String message;

    /**
     * 链路id
     */
    private String traceId;

    public BaseResult() {
    }

    public BaseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    /**
     * 通用业务请求状态码
     */
    public static final Integer CODE_SUCCESS = 200;
    public static final Integer CODE_SYSTEM_ERROR = 500;

    /**
     * 通用请求信息
     */
    public static final String SYSTEM_ERROR = "系统错误";
    public static final String MESSAGE_SUCCESS = "请求成功";
    public static final String QUERY_SUCCESS = "查询成功";
    public static final String INSERT_SUCCESS = "新增成功";
    public static final String UPDATE_SUCCESS = "更新成功";
    public static final String DELETE_SUCCESS = "删除成功";
    public static final String IMPORT_SUCCESS = "导入成功";
    public static final String EXPORT_SUCCESS = "导出成功";
    public static final String DOWNLOAD_SUCCESS = "下载成功";
}
