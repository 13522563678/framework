package com.kcwl.framework.utils;
import org.springframework.util.Base64Utils;

/**
 * @author 姚华成
 * @date 2018-03-23
 */
public class KcBase64Util extends Base64Utils {

    public static final byte BASE64_PAD_CHAR = '=';

    /**
     * 将数据按URL安全的方式编码，取消结尾的“=”
     *
     * @param data
     * @return
     */
    public static byte[] encodeUrlSafe(byte[] data) {
        int padLen = 0;
        byte[] bytes = Base64Utils.encodeUrlSafe(data);
        int len = bytes.length;
        if (bytes[len - 1] == BASE64_PAD_CHAR) {
            padLen++;
        }
        if (bytes[len - 1 - 1] == BASE64_PAD_CHAR) {
            padLen++;
        }
        if (padLen == 0) {
            return bytes;
        }
        int dstLen = len - padLen;
        byte[] dst = new byte[dstLen];
        System.arraycopy(bytes, 0, dst, 0, dstLen);
        return dst;
    }

    /**
     * 将数据按URL安全的方式编码，取消结尾的“=”
     *
     * @param data
     * @return
     */
    public static String encodeToUrlSafeString(byte[] data) {
        return new String(encodeUrlSafe(data));
    }
}
