package com.kcwl.framework.sm;

import org.junit.jupiter.api.Test;

public class SM4Test {

    @Test
    public void testSM4() {
        String plainText = "abcd";

        SM4Utils sm4 = new SM4Utils();
        sm4.setSecretKey("JeF8U9wHFOMfs2Y8");
        sm4.setHexString(false);

        System.out.println("ECB模式");
        String cipherText = sm4.encryptDataECB(plainText);
        System.out.println("密文: " + cipherText);
        System.out.println("");

        plainText = sm4.decryptDataECB(cipherText);
        System.out.println("明文: " + plainText);
        System.out.println("");

        System.out.println("CBC模式");
        sm4.setIv("UISwD9fW6cFh9SNS");
        cipherText = sm4.encryptDataCBC(plainText);
        System.out.println("密文: " + cipherText);
        System.out.println("");

        plainText = sm4.decryptDataCBC(cipherText);
        System.out.println("明文: " + plainText);
    }
}
