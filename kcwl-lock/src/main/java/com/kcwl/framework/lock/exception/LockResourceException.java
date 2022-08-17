package com.kcwl.framework.lock.exception;

import com.kcwl.ddd.infrastructure.exception.BaseException;

/**
 * @author ckwl
 */
public class LockResourceException extends BaseException {
    public LockResourceException(String code, String message) {
        super(code, message);
    }
}
