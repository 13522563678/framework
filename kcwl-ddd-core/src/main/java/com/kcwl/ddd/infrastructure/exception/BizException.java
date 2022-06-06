package com.kcwl.ddd.infrastructure.exception;

/**
 * @author 姚华成
 * @date 2017-11-14
 */
public class BizException extends BaseException {
    private static final long serialVersionUID = 1L;

    public BizException(String code, String message) {
        super(code, message);
    }
}

