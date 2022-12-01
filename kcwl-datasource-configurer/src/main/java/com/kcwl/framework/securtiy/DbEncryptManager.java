package com.kcwl.framework.securtiy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DbEncryptManager {

    private static DbEncryptManager instance;

    private Map<String, String> encryptKey;

    private DbEncryptManager() {
        encryptKey = new ConcurrentHashMap<String, String>();
    }

    public static DbEncryptManager getInstance() {
        if (instance == null) {
            instance = new DbEncryptManager();
        }
        return instance;
    }

    public String getEncryptKey(String encryptType){
        return this.encryptKey.get(encryptType);
    }

    public void setEncryptKey(String encryptType, String encryptKey) {
        this.encryptKey.put(encryptType, encryptKey);
    }
}
