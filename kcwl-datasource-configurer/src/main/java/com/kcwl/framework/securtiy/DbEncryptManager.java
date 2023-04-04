package com.kcwl.framework.securtiy;

import com.kcwl.framework.securtiy.properties.DbEncryptProperties;

/**
 * @author ckwl
 */
public class DbEncryptManager {

    private static DbEncryptManager instance;

    DbEncryptProperties encryptProperties;

    private DbEncryptManager() {

    }

    public static DbEncryptManager getInstance() {
        if (instance == null) {
            instance = new DbEncryptManager();
        }
        return instance;
    }

    public String getEncryptKey(String encryptType){
        return encryptProperties.getEncryptKey(encryptType);
    }

    public void initDbEncryptProperties(DbEncryptProperties encryptProperties ) {
        this.encryptProperties = encryptProperties;
    }
}
