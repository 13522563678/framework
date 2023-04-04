package com.kcwl.framework.securtiy.config;

import com.kcwl.framework.securtiy.DbEncryptManager;
import com.kcwl.framework.securtiy.encrypt.KcEncryptFactory;
import com.kcwl.framework.securtiy.encrypt.KcKeyManager;
import com.kcwl.framework.securtiy.encrypt.KcKeyPair;
import com.kcwl.framework.securtiy.properties.DbEncryptProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ckwl
 */
@Configuration
@EnableConfigurationProperties({DbEncryptProperties.class})
public class DbEncryptConfiguration {

    @Resource
    DbEncryptProperties dbEncryptProperties;

    @Bean
    DbEncryptManager initDbEncryptManager() {
        DbEncryptManager dbEncryptManager = DbEncryptManager.getInstance();
        dbEncryptManager.initDbEncryptProperties(dbEncryptProperties);
        return dbEncryptManager;
    }

    @Bean
    KcEncryptFactory initKcEncryptFactory() {
        KcEncryptFactory kcEncryptFactory = KcEncryptFactory.getInstance();
        ConcurrentHashMap<String, DbEncryptProperties.EncryptStore> encrypt = dbEncryptProperties.getEncrypt();
        initKeyManager();
        if ( encrypt != null ) {
            encrypt.forEach((k, v)->{
                kcEncryptFactory.createKcCrypt(k, v.getKey());
            });
        }
        return kcEncryptFactory;
    }

    private void initKeyManager(){
        KcKeyManager kcKeyManager = KcKeyManager.getInstance();
        Map<String, KcKeyPair> keyPairMap =dbEncryptProperties.getKeyPair();
        if ( keyPairMap != null ) {
            keyPairMap.forEach(kcKeyManager::addKeyPair);
        }
    }
}
