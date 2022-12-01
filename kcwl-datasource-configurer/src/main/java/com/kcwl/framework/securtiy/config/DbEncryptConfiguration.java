package com.kcwl.framework.securtiy.config;

import com.kcwl.framework.securtiy.DbEncryptManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@ConfigurationProperties(prefix = "kcwl.db")
public class DbEncryptConfiguration {

    private ConcurrentHashMap<String, EncryptStore> encrypt;

    @Bean
    DbEncryptManager initDbEncryptManager() {
        DbEncryptManager dbEncryptManager = DbEncryptManager.getInstance();
        if ( encrypt != null ) {
            encrypt.forEach((key, value)->{
                dbEncryptManager.setEncryptKey(key, value.toString());
            });
        }
        return dbEncryptManager;
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
