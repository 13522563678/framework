package com.kcwl.framework.securtiy.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    public static final String bm = "UTF-8";
    public static String encrypt( String cleartext,String dataPassword) throws Exception {
        SecretKeySpec key = new SecretKeySpec(dataPassword.getBytes(bm), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(cleartext.getBytes(bm));
        return new String (parseByte2HexStr(encryptedData));
    }

    /**
     * AES解密
     * @param encrypted
     * @param dataPassword
     * @return
     * @throws Exception
     */
    public static String decrypt( String encrypted,String dataPassword) throws Exception {
        // 对密钥进行处理-S
        byte[] byteMi = parseHexStr2Byte(encrypted);
        SecretKeySpec key = new SecretKeySpec(dataPassword.getBytes(bm), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedData = cipher.doFinal(byteMi);
        return new String(decryptedData,bm);
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
