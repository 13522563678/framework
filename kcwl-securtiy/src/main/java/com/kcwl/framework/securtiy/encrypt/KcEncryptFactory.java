package com.kcwl.framework.securtiy.encrypt;

import com.kcwl.framework.securtiy.encrypt.impl.*;

import java.util.HashMap;
import java.util.Map;

public class KcEncryptFactory {
    private static KcEncryptFactory instance = new KcEncryptFactory();
    private Map<String, IKcCrypt> cryptMap;

    private KcEncryptFactory(){
        cryptMap = new HashMap<String, IKcCrypt>();
    }

    public static KcEncryptFactory getInstance() {return instance;}

    public static IKcCrypt getKcCrypt(String name) {
        IKcCrypt kcCrypt = instance.findKcCrypt(name);
        if ( kcCrypt == null ) {
            kcCrypt = KcEmptyCrypt.getKcEmptyCrypt();
        }
        return kcCrypt;
    }

    public IKcCrypt createKcCrypt(String name, String password) {
        IKcCrypt kcCrypt = null;
        if ( EncryptType.ENCRYPT_AES.equals(name) ) {
            kcCrypt = new KcAesCrypt(password);
        } else if ( EncryptType.ENCRYPT_MOBILE.equals(name)) {
            kcCrypt = new KcMobileCrypt(password);
        } else if ( EncryptType.ENCRYPT_SIMPLE.equals(name) ) {
            kcCrypt = new KcSimpleCrypt(password);
        } else if (EncryptType.ENCRYPT_RSA.equals(name) ) {
            KcKeyPair keyPair = KcKeyManager.getInstance().getKeyPair(password);
            if ( keyPair != null ) {
                kcCrypt = new KcRsaCrypt(keyPair.getPrivateKey(), keyPair.getPublicKey());
            }
        }
        if ( kcCrypt != null ) {
            cryptMap.put(name, kcCrypt);
        }
        return kcCrypt;
    }

    private IKcCrypt findKcCrypt(String name) {
        return cryptMap.get(name);
    }
}

