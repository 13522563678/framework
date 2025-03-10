package com.kcwl.ddd.infrastructure.exception;

/**
 * @author ckwl
 */
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String code;
    private Object result;

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * @return
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
