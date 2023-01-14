package com.kcwl.framework.securtiy.encrypt;

public enum EncryptType {

    ENCRYPT_MOBILE("MOBILE"),
    ENCRYPT_SIMPLE("SIMPLE"),
    ENCRYPT_EMPTY("EMPTY"),
    ENCRYPT_AES("AES");

    private String name;
    EncryptType(String name) {
        this.name=name;
    }

    public boolean equals(String name) {
        return (name!=null && this.name.equals(name));
    }

    public String getName() {return this.name;}


}
