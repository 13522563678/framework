package com.kcwl.framework.securtiy.properties;

import com.kcwl.framework.securtiy.encrypt.KcKeyPair;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ckwl
 */
@Data
@ConfigurationProperties(prefix = "kcwl.db")
public class DbEncryptProperties {

    private ConcurrentHashMap<String, EncryptStore> encrypt;
    private ConcurrentHashMap<String, KcKeyPair> keyPair;

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
