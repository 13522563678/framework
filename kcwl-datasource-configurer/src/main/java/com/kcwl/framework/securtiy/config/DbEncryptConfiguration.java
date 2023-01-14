package com.kcwl.framework.securtiy.config;

import com.kcwl.framework.securtiy.DbEncryptManager;
import com.kcwl.framework.securtiy.encrypt.KcEncryptFactory;
import com.kcwl.framework.securtiy.properties.DbEncryptProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

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
        if ( encrypt != null ) {
            encrypt.forEach((k, v)->{
                kcEncryptFactory.createKcCrypt(k, v.getKey());
            });
        }
        return kcEncryptFactory;
    }

}
