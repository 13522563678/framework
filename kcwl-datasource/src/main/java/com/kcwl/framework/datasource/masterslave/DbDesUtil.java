package com.kcwl.framework.datasource.masterslave;

import com.kcwl.framework.utils.KcBase64Util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * @author 姚华成
 * @date 2017-12-25
 */
public class DbDesUtil {
    private static final String CIPHER_ALGORITHM = "DES";
    private static final String DEFAULT_DES_KEY = "kcwl#!";
    private static final String DES_KEY_ENV_NAME = "DB_DES_KEY";

    private DbDesUtil() {
    }


    /**
     * 根据键值进行加密
     */
    public static String encrypt(String data) {
        try {
            byte[] bt = encrypt(data.getBytes(), getKey().getBytes());
            return KcBase64Util.encodeToString(bt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据键值进行解密
     */
    public static String decrypt(String data) {
        if (data == null) {
            return null;
        }
        try {

            byte[] buf = KcBase64Util.decodeFromString(data);
            byte[] bt = decrypt(buf, getKey().getBytes());
            return new String(bt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getKey() {
        String key = System.getenv(DES_KEY_ENV_NAME);
        if (key == null) {
            key = DEFAULT_DES_KEY;
        }
        return key;
    }

    /**
     * 根据键值进行加密
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        Cipher cipher = cipherInit(key, Cipher.ENCRYPT_MODE);
        return cipher.doFinal(data);
    }

    /**
     * 根据键值进行解密
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        Cipher cipher = cipherInit(key, Cipher.DECRYPT_MODE);
        return cipher.doFinal(data);
    }

    private static Cipher cipherInit(byte[] key, int cipherValue) throws Exception {
        /* 生成一个可信任的随机数源 */
        SecureRandom sr = new SecureRandom();
        /* 从原始密钥数据创建DESKeySpec对象 */
        DESKeySpec dks = new DESKeySpec(key);
        /* 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象 */
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(CIPHER_ALGORITHM);
        SecretKey securekey = keyFactory.generateSecret(dks);
        /* Cipher对象实际完成加密或解密操作 */
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        /* 用密钥初始化Cipher对象 */
        cipher.init(cipherValue, securekey, sr);
        return cipher;
    }
}