package com.kcwl.framework.securtiy.encrypt;

/**
 * @author ckwl
 */

public enum CryptMode {
    /**
     *
     */
    ENCODE(1),
    DECODE(2);
    /**
     *
     */
    private int mode;
    CryptMode(int mode){
        this.mode=mode;
    }
}
