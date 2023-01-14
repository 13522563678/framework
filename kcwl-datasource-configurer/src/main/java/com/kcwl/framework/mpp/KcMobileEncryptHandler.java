package com.kcwl.framework.mpp;

import com.kcwl.framework.securtiy.encrypt.EncryptType;

public class KcMobileEncryptHandler extends KcEncryptTypeHandler {
    String getEncryptName() {
        return EncryptType.ENCRYPT_MOBILE.getName();
    }
}
