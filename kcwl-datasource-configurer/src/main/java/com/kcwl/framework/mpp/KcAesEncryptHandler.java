package com.kcwl.framework.mpp;

import com.kcwl.framework.securtiy.encrypt.EncryptType;

/**
 * @author ckwl
 */
public class KcAesEncryptHandler extends KcEncryptTypeHandler  {
    @Override
    String getEncryptName() {
        return EncryptType.ENCRYPT_AES.getName();
    }
}
