package com.kcwl.framework.mpp;

import com.kcwl.framework.securtiy.encrypt.EncryptType;

/**
 * @author ckwl
 */
public class KcMobileEncryptHandler extends KcEncryptTypeHandler {
    @Override
    String getEncryptName() {
        return EncryptType.ENCRYPT_MOBILE.getName();
    }
}
