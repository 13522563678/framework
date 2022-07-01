package com.kcwl.framework.utils;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

/**
 * @author 姚华成
 * @date 2018-03-23
 */
public class CipherUtil {
    private static final String ALGORITHM_DES = "DES";
    private static final String ALGORITHM_3DES = "DESede";
    private static final String ALGORITHM_AES = "AES";
    private static final String ALGORITHM_RSA = "RSA";
    private static final String ALGORITHM_DSA = "DSA";

    public static byte[] desEncrypt(byte[] key, byte[] data) {
        return cipherData(ALGORITHM_DES, Cipher.ENCRYPT_MODE,
                new SecretKeySpec(key, ALGORITHM_DES), data);
    }

    public static byte[] desDecrypt(byte[] key, byte[] data) {
        return cipherData(ALGORITHM_DES, Cipher.DECRYPT_MODE,
                new SecretKeySpec(key, ALGORITHM_DES), data);
    }

    /**
     * @param key  字符串格式的Key
     * @param data 字符串格式的data
     * @return data经过des加密后，以16进制表示的数据
     */
    public static String desEncryptToHex(String key, String data) {
        return KcHexUtil.encodeHex(desEncrypt(key.getBytes(), data.getBytes()));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 以16进制表示的经过des加密后的数据
     * @return data经过des解密后的字符串数据
     */
    public static String desDecryptFromHex(String key, String data) {
        return new String(desDecrypt(key.getBytes(), KcHexUtil.decodeHex(data)));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 字符串格式的data
     * @return data经过des加密后，以Base64编码的数据
     */
    public static String desEncryptToBase64(String key, String data) {
        return KcBase64Util.encodeToString(desEncrypt(key.getBytes(), data.getBytes()));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 以Base64编码的经过des加密后的数据
     * @return data经过des解密后的字符串数据
     */
    public static String desDecryptFromBase64(String key, String data) {
        return new String(desDecrypt(key.getBytes(), KcBase64Util.decodeFromString(data)));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 字符串格式的data
     * @return data经过des加密后，以URL安全的Base64编码的数据
     */
    public static String desEncryptToBase64UrlSafe(String key, String data) {
        return KcBase64Util.encodeToUrlSafeString(desEncrypt(key.getBytes(), data.getBytes()));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 以1URL安全的Base64编码的经过des加密后的数据
     * @return data经过des解密后的字符串数据
     */
    public static String desDecryptFromBase64UrlSafe(String key, String data) {
        return new String(desDecrypt(key.getBytes(), KcBase64Util.decodeFromUrlSafeString(data)));
    }

    /**
     * @param key
     * @param data
     * @return
     */
    public static byte[] des3Encrypt(byte[] key, byte[] data) {
        return cipherData(ALGORITHM_3DES, Cipher.ENCRYPT_MODE,
                new SecretKeySpec(key, ALGORITHM_3DES), data);
    }

    /**
     * @param key
     * @param data
     * @return
     */
    public static byte[] des3Decrypt(byte[] key, byte[] data) {
        return cipherData(ALGORITHM_3DES, Cipher.DECRYPT_MODE,
                new SecretKeySpec(key, ALGORITHM_3DES), data);
    }

    /**
     * @param key  字符串格式的Key
     * @param data 字符串格式的data
     * @return data经过3des加密后，以16进制表示的数据
     */
    public static String des3EncryptToHex(String key, String data) {
        return KcHexUtil.encodeHex(des3Encrypt(key.getBytes(), data.getBytes()));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 以16进制表示的经过3des加密后的数据
     * @return data经过3des解密后的字符串数据
     */
    public static String des3DecryptFromHex(String key, String data) {
        return new String(des3Decrypt(key.getBytes(), KcHexUtil.decodeHex(data)));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 字符串格式的data
     * @return data经过3des加密后，以Base64编码的数据
     */
    public static String des3EncryptToBase64(String key, String data) {
        return KcBase64Util.encodeToString(des3Encrypt(key.getBytes(), data.getBytes()));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 以Base64编码的经过3des加密后的数据
     * @return data经过3des解密后的字符串数据
     */
    public static String des3DecryptFromBase64(String key, String data) {
        return new String(des3Decrypt(key.getBytes(), KcBase64Util.decodeFromString(data)));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 字符串格式的data
     * @return data经过3des加密后，以URL安全的Base64编码的数据
     */
    public static String des3EncryptToBase64UrlSafe(String key, String data) {
        return KcBase64Util.encodeToUrlSafeString(des3Encrypt(key.getBytes(), data.getBytes()));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 以1URL安全的Base64编码的经过3des加密后的数据
     * @return data经过3des解密后的字符串数据
     */
    public static String des3DecryptFromBase64UrlSafe(String key, String data) {
        return new String(des3Decrypt(key.getBytes(), KcBase64Util.decodeFromUrlSafeString(data)));
    }

    /**
     * @param key
     * @param data
     * @return
     */
    public static byte[] aesEncrypt(byte[] key, byte[] data) {
        SecretKey securekey = new SecretKeySpec(key, ALGORITHM_AES);
        return cipherData(ALGORITHM_AES, Cipher.ENCRYPT_MODE, securekey, data);
    }

    /**
     * @param key
     * @param data
     * @return
     */
    public static byte[] aesDecrypt(byte[] key, byte[] data) {
        SecretKey securekey = new SecretKeySpec(key, ALGORITHM_AES);
        return cipherData(ALGORITHM_AES, Cipher.DECRYPT_MODE, securekey, data);
    }

    /**
     * @param key  字符串格式的Key
     * @param data 字符串格式的data
     * @return data经过aes加密后，以16进制表示的数据
     */
    public static String aesEncryptToHex(String key, String data) {
        return KcHexUtil.encodeHex(aesEncrypt(key.getBytes(), data.getBytes()));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 以16进制表示的经过aes加密后的数据
     * @return data经过aes解密后的字符串数据
     */
    public static String aesDecryptFromHex(String key, String data) {
        return new String(aesDecrypt(key.getBytes(), KcHexUtil.decodeHex(data)));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 字符串格式的data
     * @return data经过aes加密后，以Base64编码的数据
     */
    public static String aesEncryptToBase64(String key, String data) {
        return KcBase64Util.encodeToString(aesEncrypt(key.getBytes(), data.getBytes()));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 以Base64编码的经过aes加密后的数据
     * @return data经过aes解密后的字符串数据
     */
    public static String aesDecryptFromBase64(String key, String data) {
        return new String(aesDecrypt(key.getBytes(), KcBase64Util.decodeFromString(data)));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 字符串格式的data
     * @return data经过aes加密后，以URL安全的Base64编码的数据
     */
    public static String aesEncryptToBase64UrlSafe(String key, String data) {
        return KcBase64Util.encodeToUrlSafeString(aesEncrypt(key.getBytes(), data.getBytes()));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 以1URL安全的Base64编码的经过aes加密后的数据
     * @return data经过aes解密后的字符串数据
     */
    public static String aesDecryptFromBase64UrlSafe(String key, String data) {
        return new String(aesDecrypt(key.getBytes(), KcBase64Util.decodeFromUrlSafeString(data)));
    }

    public static KeyPair genRsaKeyPair() {
        try {
            return genKeyPair(ALGORITHM_RSA, 1024);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] rsaEncrypt(Key key, byte[] data) {
        return cipherData(ALGORITHM_RSA, Cipher.ENCRYPT_MODE, key, data);
    }

    public static byte[] rsaDecrypt(Key key, byte[] data) {
        return cipherData(ALGORITHM_RSA, Cipher.DECRYPT_MODE, key, data);
    }

    /**
     * @param key  字符串格式的Key
     * @param data 字符串格式的data
     * @return data经过rsa加密后，以16进制表示的数据
     */
    public static String rsaEncryptToHex(Key key, String data) {
        return KcHexUtil.encodeHex(rsaEncrypt(key, data.getBytes()));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 以16进制表示的经过rsa加密后的数据
     * @return data经过rsa解密后的字符串数据
     */
    public static String rsaDecryptFromHex(Key key, String data) {
        return new String(rsaDecrypt(key, KcHexUtil.decodeHex(data)));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 字符串格式的data
     * @return data经过rsa加密后，以Base64编码的数据
     */
    public static String rsaEncryptToBase64(Key key, String data) {
        return KcBase64Util.encodeToString(rsaEncrypt(key, data.getBytes()));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 以Base64编码的经过rsa加密后的数据
     * @return data经过rsa解密后的字符串数据
     */
    public static String rsaDecryptFromBase64(Key key, String data) {
        return new String(rsaDecrypt(key, KcBase64Util.decodeFromString(data)));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 字符串格式的data
     * @return data经过rsa加密后，以URL安全的Base64编码的数据
     */
    public static String rsaEncryptToBase64UrlSafe(Key key, String data) {
        return KcBase64Util.encodeToUrlSafeString(rsaEncrypt(key, data.getBytes()));
    }

    /**
     * @param key  字符串格式的Key
     * @param data 以1URL安全的Base64编码的经过rsa加密后的数据
     * @return data经过rsa解密后的字符串数据
     */
    public static String rsaDecryptFromBase64UrlSafe(Key key, String data) {
        return new String(rsaDecrypt(key, KcBase64Util.decodeFromUrlSafeString(data)));
    }

    public static KeyPair genDsaKeyPair() {
        try {
            return genKeyPair(ALGORITHM_DSA, 2048);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static KeyPair genKeyPair(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
        generator.initialize(keySize);
        return generator.genKeyPair();
    }

    public static byte[] cipherData(String algorithm, int cipherMode, Key key, byte[] data) {
        try {
            /* Cipher对象实际完成加密或解密操作 */
            Cipher cipher = Cipher.getInstance(algorithm);
            /* 用密钥初始化Cipher对象 */
            cipher.init(cipherMode, key, new SecureRandom());
            // 处理数据
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("数据加解密出错！错误原因：" + e.getMessage());
        }
    }
}
