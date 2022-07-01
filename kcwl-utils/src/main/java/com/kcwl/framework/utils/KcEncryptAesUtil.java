package com.kcwl.framework.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

/**
 *
 */
public class KcEncryptAesUtil {

    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    private static final String CHARSET = "utf-8";

    /**
     * 加密
     *
     * @param context
     * @return
     */
    public static String encrypt(String context, String key, String iv) {
        try {
            byte[] decode = context.getBytes(CHARSET);
            byte[] bytes = createKeyAndIv(decode, Cipher.ENCRYPT_MODE,key,iv);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param context
     * @return
     */
    public static String decrypt(String context, String key, String iv) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decode = decoder.decode(context);
            byte[] bytes = createKeyAndIv(decode, Cipher.DECRYPT_MODE,key,iv);
            return new String(bytes, CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取key & iv
     *
     * @param context
     * @param opmode
     * @return
     * @throws Exception
     */
    public static byte[] createKeyAndIv(byte[] context, int opmode, String key, String iv) throws Exception {
        byte[] bytekey = key.getBytes(CHARSET);
        byte[] byteiv = iv.getBytes(CHARSET);
        return cipherFilter(context, opmode, bytekey, byteiv);
    }

    /**
     * 执行操作
     *
     * @param context
     * @param opmode
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static byte[] cipherFilter(byte[] context, int opmode, byte[] key, byte[] iv) throws Exception {
        Key secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(opmode, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(context);
    }
}
