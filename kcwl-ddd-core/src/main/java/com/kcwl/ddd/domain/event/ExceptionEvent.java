package com.kcwl.ddd.domain.event;

public class ExceptionEvent {
    private String code;
    private String message;
    private String reqUri;
    private Exception exception;

    public ExceptionEvent(String code, String message, String reqUri, Exception exception) {
        this.code = code;
        this.message = message;
        this.reqUri = reqUri;
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
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

    public String getReqUri() {
        return reqUri;
    }

    public void setReqUri(String reqUri) {
        this.reqUri = reqUri;
    }
}
