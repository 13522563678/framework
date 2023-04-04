package com.kcwl.framework.securtiy.encrypt;

import com.kcwl.framework.securtiy.codec.PassDict;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ckwl
 */
public class KcKeyManager {

    private static KcKeyManager keyManager = new KcKeyManager();

    private Map<String, KcKeyPair> keyPairMap = new ConcurrentHashMap<>();

    private KcKeyManager() {
        initKeyPair();
    }

    public static KcKeyManager getInstance() {
        return keyManager;
    }

    public void addKeyPair(String keyName, KcKeyPair keyPair) {
        keyPairMap.put(keyName, keyPair);
    }

    public KcKeyPair getKeyPair(String keyName) {
        return keyPairMap.get(keyName);
    }

    private void initKeyPair(){
        addKeyPair(PassDict.DEFAULT_RSA_KEY, new KcKeyPair(PassDict.RSA_PRIVATE_KEY, PassDict.RSA_PUBLIC_KEY));
    }
}
