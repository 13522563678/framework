package com.kcwl.framework.mpp;

import com.kcwl.framework.securtiy.encrypt.EncryptType;

public class KcAesEncryptHandler extends KcEncryptTypeHandler  {
    String getEncryptName() {
        return EncryptType.ENCRYPT_AES.getName();
    }
}
