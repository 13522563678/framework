package com.kcwl.framework.securtiy.encrypt.impl;

import com.kcwl.framework.securtiy.encrypt.IKcCrypt;

public class KcEmptyCrypt implements IKcCrypt {

    private static KcEmptyCrypt kcEmptyCrypt = new KcEmptyCrypt();

    @Override
    public String encrypt(String plainText) {
        return plainText;
    }

    @Override
    public String decrypt(String encryptedText) {
        return encryptedText;
    }

    public static KcEmptyCrypt getKcEmptyCrypt(){
        return kcEmptyCrypt;
    }
}
