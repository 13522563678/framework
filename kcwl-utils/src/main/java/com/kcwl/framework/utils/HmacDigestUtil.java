package com.kcwl.framework.utils;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * 带密钥的数字签名工具类
 *
 * @author 姚华成
 * @date 2018-04-09
 */
public class HmacDigestUtil {

    private static final String ALGORITHM_SHA256 = "HmacSHA256";

    /**
     * 以hmacSha256进行带密钥的数字签名
     *
     * @param key  签名的密钥
     * @param data 需要签名的数据
     * @return 数据的签名
     */
    public static byte[] hmacSha256(byte[] key, byte[] data) {
        return digestData(ALGORITHM_SHA256, new SecretKeySpec(key, ALGORITHM_SHA256), data);
    }

    /**
     * 以hmacSha256进行带密钥的数字签名
     *
     * @param key  签名的密钥
     * @param data 需要签名的数据
     * @return 以16进制字符串表示的数据的签名
     */
    public static String hmacSha256AsHex(String key, String data) {
        return KcHexUtil.encodeHex(hmacSha256(key.getBytes(), data.getBytes()));
    }

    /**
     * 以hmacSha256进行带密钥的数字签名
     *
     * @param key  签名的密钥
     * @param data 需要签名的数据
     * @return 以Base64编码的数据的签名
     */
    public static String hmacSha256AsBase64(String key, String data) {
        return KcBase64Util.encodeToString(hmacSha256(key.getBytes(), data.getBytes()));
    }

    /**
     * 以hmacSha256进行带密钥的数字签名
     *
     * @param key  签名的密钥
     * @param data 需要签名的数据
     * @return 以URL安全的Base64编码的数据的签名
     */
    public static String hmacSha256AsBase64UrlSafe(String key, String data) {
        return KcBase64Util.encodeToUrlSafeString(hmacSha256(key.getBytes(), data.getBytes()));
    }

    public static byte[] digestData(String algorithm, Key key, byte[] data) {
        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(key);
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("数据加解密出错！");
        }
    }
}
