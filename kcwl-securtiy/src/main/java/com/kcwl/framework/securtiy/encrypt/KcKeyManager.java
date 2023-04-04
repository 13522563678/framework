package com.kcwl.framework.securtiy.encrypt;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KcKeyManager {

    private static KcKeyManager keyManager = new KcKeyManager();

    private Map<String, KcKeyPair> keyPairMap = new ConcurrentHashMap<>();

    private KcKeyManager() {
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
}
