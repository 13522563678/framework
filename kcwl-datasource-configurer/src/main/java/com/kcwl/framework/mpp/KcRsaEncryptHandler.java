package com.kcwl.framework.mpp;

import com.kcwl.framework.securtiy.encrypt.EncryptType;

/**
 * @author ckwl
 */
public class KcRsaEncryptHandler extends KcEncryptTypeHandler {
    @Override
    String getEncryptName() {
        return EncryptType.ENCRYPT_RSA.getName();
    }
}
