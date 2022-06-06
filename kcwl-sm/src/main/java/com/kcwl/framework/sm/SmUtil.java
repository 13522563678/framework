package com.kcwl.framework.sm;
import java.io.IOException;
import java.util.Base64;

/**
 * 国密算法统一的对外使用工具类
 * SM3是签名算法，对应md5, sha256等
 * SM4是对称加密算法，对应des, 3des, aes等
 * SM2是非对称加密算法，对应rsa, rsa2048等
 * ECC-SM2是签名验签算法，对应ECC-Secp256K1等
 *
 * @author 姚华成
 * @date 2018-05-24
 */
public class SmUtil {

    private static Base64.Encoder noPaddingUrlEncoder = Base64.getUrlEncoder().withoutPadding();

    public static byte[] sm2Encrypt(byte[] key, byte[] data) {
        try {
            return SM2Utils.encrypt(key, data);
        } catch (IOException e) {
            throw new RuntimeException("数据加密错误！错误原因：" + e.getMessage());
        }
    }

    public static byte[] sm2Decrypt(byte[] key, byte[] data) {
        try {
            return SM2Utils.decrypt(key, data);
        } catch (IOException e) {
            throw new RuntimeException("数据加密错误！错误原因：" + e.getMessage());
        }
    }

    public static String sm2EncryptToHex(String key, String data) {
        return Util.encodeHexString(sm2Encrypt(key.getBytes(), data.getBytes()));
    }

    public static String sm2DecryptFromHex(String key, String data) {
        return new String(sm2Decrypt(key.getBytes(), Util.hexToByte(data)));
    }

    public static String sm2EncryptToBase64(String key, String data) {
        return Base64.getEncoder().encodeToString(sm2Encrypt(key.getBytes(), data.getBytes()));
    }

    public static String sm2DecryptFromBase64(String key, String data) {
        return new String(sm2Decrypt(key.getBytes(), Base64.getDecoder().decode(data)));
    }

    public static String sm2EncryptToBase64UrlSafe(String key, String data) {
        return noPaddingUrlEncoder.encodeToString(sm2Encrypt(key.getBytes(), data.getBytes()));
    }

    public static String sm2DecryptFromBase64UrlSafe(String key, String data) {
        return new String(sm2Decrypt(key.getBytes(), Base64.getUrlDecoder().decode(data)));
    }

    public static byte[] sm3(byte[] data) {
        byte[] md = new byte[32];
        SM3Digest sm3 = new SM3Digest();
        sm3.update(data, 0, data.length);
        sm3.doFinal(md, 0);
        return md;
    }

    public static String sm3AsHex(String data) {
        return Util.encodeHexString(sm3(data.getBytes()));
    }

    public static String sm3AsBase64(String data) {
        return Base64.getEncoder().encodeToString(sm3(data.getBytes()));
    }

    public static String sm3AsBase64UrlSafe(String data) {
        return noPaddingUrlEncoder.encodeToString(sm3(data.getBytes()));
    }

    public static byte[] sm4Ecb(int mode, byte[] key, byte[] data) {
        try {
            SM4_Context ctx = new SM4_Context(mode);
            SM4 sm4 = new SM4();
            sm4.sm4SetKeyEnc(ctx, key);
            return sm4.sm4CryptEcb(ctx, data);
        } catch (Exception e) {
            throw new RuntimeException("数据加解密出错，错误原因：" + e.getMessage());
        }
    }

    public static byte[] sm4EncryptEcb(byte[] key, byte[] data) {
        return sm4Ecb(SM4.SM4_ENCRYPT, key, data);
    }

    public static byte[] sm4DecryptEcb(byte[] key, byte[] data) {
        return sm4Ecb(SM4.SM4_DECRYPT, key, data);
    }

    public static String sm4EncryptEcbToHex(String key, String data) {
        return Util.encodeHexString(sm4Ecb(SM4.SM4_ENCRYPT, key.getBytes(), data.getBytes()));
    }

    public static String sm4DecryptEcbFromHex(String key, String data) {
        return new String(sm4Ecb(SM4.SM4_DECRYPT, key.getBytes(), Util.hexStringToBytes(data)));
    }

    public static String sm4EncryptEcbToBase64(String key, String data) {
        return Base64.getEncoder().encodeToString(sm4Ecb(SM4.SM4_ENCRYPT, key.getBytes(), data.getBytes()));
    }

    public static String sm4DecryptEcbFromBase64(String key, String data) {
        return new String(sm4Ecb(SM4.SM4_DECRYPT, key.getBytes(), Base64.getDecoder().decode(data)));
    }
    public static String sm4EncryptEcbToBase64UrlSafe(String key, String data) {
        return noPaddingUrlEncoder.encodeToString(sm4Ecb(SM4.SM4_ENCRYPT, key.getBytes(), data.getBytes()));
    }

    public static String sm4DecryptEcbFromBase64UrlSafe(String key, String data) {
        return new String(sm4Ecb(SM4.SM4_DECRYPT, key.getBytes(), Base64.getUrlDecoder().decode(data)));
    }
    public static byte[] sm4Cbc(int mode, byte[] key, byte[] data, byte[] iv) {
        try {
            SM4_Context ctx = new SM4_Context(mode);
            SM4 sm4 = new SM4();
            sm4.sm4SetKeyEnc(ctx, key);
            return sm4.sm4CryptCbc(ctx, iv, data);
        } catch (Exception e) {
            throw new RuntimeException("数据加解密出错，错误原因：" + e.getMessage());
        }
    }

    public static byte[] sm4Cbc(int mode, byte[] key, byte[] data) {
        return sm4Cbc(mode, key, data, new byte[0]);
    }

    public static byte[] sm4EncryptCbc(byte[] key, byte[] data) {
        return sm4Cbc(SM4.SM4_ENCRYPT, key, data);
    }

    public static byte[] sm4DecryptCbc(byte[] key, byte[] data) {
        return sm4Cbc(SM4.SM4_DECRYPT, key, data);
    }

    public static String sm4EncryptCbcToHex(String key, String data) {
        return Util.encodeHexString(sm4Cbc(SM4.SM4_ENCRYPT, key.getBytes(), data.getBytes()));
    }

    public static String sm4DecryptCbcFromHex(String key, String data) {
        return new String(sm4Cbc(SM4.SM4_DECRYPT, key.getBytes(), Util.hexStringToBytes(data)));
    }

    public static String sm4EncryptCbcToBase64(String key, String data) {
        return Base64.getEncoder().encodeToString(sm4Cbc(SM4.SM4_ENCRYPT, key.getBytes(), data.getBytes()));
    }

    public static String sm4DecryptCbcFromBase64(String key, String data) {
        return new String(sm4Cbc(SM4.SM4_DECRYPT, key.getBytes(), Base64.getDecoder().decode(data)));
    }
    public static String sm4EncryptCbcToBase64UrlSafe(String key, String data) {
        return noPaddingUrlEncoder.encodeToString(sm4Cbc(SM4.SM4_ENCRYPT, key.getBytes(), data.getBytes()));
    }

    public static String sm4DecryptCbcFromBase64UrlSafe(String key, String data) {
        return new String(sm4Cbc(SM4.SM4_DECRYPT, key.getBytes(), Base64.getUrlDecoder().decode(data)));
    }
}
