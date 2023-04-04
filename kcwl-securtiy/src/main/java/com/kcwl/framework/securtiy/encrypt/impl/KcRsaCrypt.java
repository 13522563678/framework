package com.kcwl.framework.securtiy.encrypt.impl;

import com.kcwl.framework.securtiy.encrypt.IKcCrypt;
import lombok.SneakyThrows;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author ckwl
 */
public class KcRsaCrypt implements IKcCrypt {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    private String privateKey;
    private String publicKey;

    public KcRsaCrypt(String privateKey, String publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    /**
     * 数据加密
     *
     * @param plainText 待加密的字符串
     * @return 返回加密后的数据
     */
    @SneakyThrows
    @Override
    public String encrypt(String plainText) {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    /**
     * 解密数据
     *
     * @param encryptedText 加密后的数据
     * @return 返回解密后的数据
     */
    @SneakyThrows
    @Override
    public String decrypt(String encryptedText) {
        byte[] encryptedData = Base64.getDecoder().decode(encryptedText.getBytes());
        byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        return new String(cipher.doFinal(encryptedData));
    }
}
