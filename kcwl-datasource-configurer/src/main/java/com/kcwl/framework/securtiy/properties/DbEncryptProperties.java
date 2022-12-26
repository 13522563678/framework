package com.kcwl.framework.securtiy.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.ConcurrentHashMap;

@Data
@ConfigurationProperties(prefix = "kcwl.db")
public class DbEncryptProperties {

    private ConcurrentHashMap<String, EncryptStore> encrypt;

    public String getEncryptKey(String encryptType) {
        if ( encrypt != null ) {
            EncryptStore encryptStore = encrypt.get(encryptType);
            if ( encryptStore != null ) {
                return encryptStore.getKey();
            }
        }
        return null;
    }

    public static class EncryptStore {
        private String key;
        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
    }
}
