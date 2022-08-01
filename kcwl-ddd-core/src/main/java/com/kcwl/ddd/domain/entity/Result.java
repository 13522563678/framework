package com.kcwl.ddd.domain.entity;
/**
 * 返回值封装，用于方法之间调用返回，非浏览器调用返回值
 */
public class Result<T> extends BaseResult {
    private T data;

    public Result() {
    }

    public Result(Integer code, String message, T data) {
        super(code, message);
        this.data = data;
    }

    public boolean success() {
        return CODE_SUCCESS.equals(getCode());
    }

    public boolean systemFail() {
        return CODE_SYSTEM_ERROR.equals(getCode());
    }

    public static Result<Object> ok() {
        return new Result<>(CODE_SUCCESS, "", null);
    }

    public static Result<Object> ok(String message) {
        return new Result<>(CODE_SUCCESS, message, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(CODE_SUCCESS, MESSAGE_SUCCESS, data);
    }

    public static <T> Result<T> success(T data, String message) {
        return new Result<>(CODE_SUCCESS, message, data);
    }

    public static Result<Object> error(String message) {
        return Result.error(CODE_SYSTEM_ERROR, message, null);
    }

    public static Result<Object> error(String errorCode, String message) {
        return Result.error(CODE_SYSTEM_ERROR, message, null);
    }
    public static Result<Object> error(Integer code, String message, Object data) {
        return new Result<>(code, message, data);
    }

}
