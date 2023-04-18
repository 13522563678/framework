package com.kcwl.ddd.infrastructure.api;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResponseMessage<T> {
    /**
     * 响应编号
     */
    private String code;
    /**
     * 响应描述
     */
    private String message;

    /**
     * 响应数据
     */
    private T result;

    public ResponseMessage() {
    }

    public ResponseMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result == null ? null : result;
    }
    @JsonIgnore
    public boolean isSuccess() {
        return (this.code != null && this.code.endsWith("200") ) ? true : false;
    }
}
