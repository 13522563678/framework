package com.kcwl.framework.securtiy.encrypt;

/**
 * @author ckwl
 */

public enum EncryptType {

    /**
     * 定义加密类型
     */
    ENCRYPT_MOBILE("MOBILE"),
    ENCRYPT_SIMPLE("SIMPLE"),
    ENCRYPT_EMPTY("EMPTY"),
    ENCRYPT_AES("AES"),
    ENCRYPT_RSA("RSA");

    private String name;
    EncryptType(String name) {
        this.name=name;
    }

    public boolean sameAsName(String name) {
        return (this.name.equals(name));
    }

    public String getName() {return this.name;}

}
