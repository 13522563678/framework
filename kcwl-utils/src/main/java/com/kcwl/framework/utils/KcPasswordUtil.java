package com.kcwl.framework.utils;

import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 刘旭超
 * @date 2022年06月13日 18:57
 * @Description
 */
@Slf4j
public class KcPasswordUtil {
    /**
     * JAVA6支持以下任意一种算法 PBEWITHMD5ANDDES PBEWITHMD5ANDTRIPLEDES
     * PBEWITHSHAANDDESEDE PBEWITHSHA1ANDRC2_40 PBKDF2WITHHMACSHA1
     *
     * 定义使用的算法为:PBEWITHMD5andDES算法
     */
    public static final String ALGORITHM = "PBEWithMD5AndDES";//加密算法
    public static final String Salt = "83179664";//密钥

    /**
     * 定义迭代次数为1000次
     */
    private static final int ITERATION_COUNT = 1000;

    /**
     * 获取加密算法中使用的盐值,解密中使用的盐值必须与加密中使用的相同才能完成操作. 盐长度必须为8字节
     *
     * @return byte[] 盐值
     */
    public static byte[] getSalt() {
        // 实例化安全随机数
        SecureRandom random = new SecureRandom();
        // 产出盐
        return random.generateSeed(8);
    }

    public static byte[] getStaticSalt() {
        // 产出盐
        return Salt.getBytes();
    }

    /**
     * 根据PBE密码生成一把密钥
     *
     * @param password 生成密钥时所使用的密码
     * @return Key PBE算法密钥
     */
    private static Key getPBEKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 实例化使用的算法
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            // 设置PBE密钥参数
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            // 生成密钥
            return keyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 加密明文字符串
     *
     * @param plaintext 待加密的明文字符串
     * @param password  生成密钥时所使用的密码
     * @param salt      盐值
     * @return 加密后的密文字符串
     */
    public static String encrypt(String plaintext, String password, byte[] salt) {
        try {
            Key key = getPBEKey(plaintext);
            PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

            byte[] encipheredData = cipher.doFinal(password.getBytes());
            return bytesToHexString(encipheredData);
        } catch (Exception e) {
            log.error("密码加密失败", e);
            throw new BizException(CommonCode.DATA_MESSAGE_ENCODE_FAIL.getCode(), CommonCode.DATA_MESSAGE_ENCODE_FAIL.getDescription());
        }
    }

    /**
     * 解密密文字符串
     *
     * @param plaintext   生成密钥时所使用的明文字符串(如需解密,该参数需要与加密时使用的一致)
     * @param ciphertext 待解密的密文字符串
     * @param salt       盐值(如需解密,该参数需要与加密时使用的一致)
     * @return 解密后的明文字符串
     */
    public static String decrypt(String plaintext, String ciphertext, byte[] salt) {

        try {
            Key key = getPBEKey(plaintext);
            PBEParameterSpec parameterSpec = new PBEParameterSpec(getStaticSalt(), ITERATION_COUNT);
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

            byte[] passDec = cipher.doFinal(hexStringToBytes(ciphertext));
            return new String(passDec);
        } catch (Exception e) {
            log.error("密码解密失败", e);
            throw new BizException(CommonCode.DATA_MESSAGE_DECODE_FAIL.getCode(), CommonCode.DATA_MESSAGE_DECODE_FAIL.getDescription());
        }
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param src 字节数组
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 将十六进制字符串转换为字节数组
     *
     * @param hexString 十六进制字符串
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    private static final CharacterRule LOWER_CASE_RULE = new CharacterRule(EnglishCharacterData.LowerCase);

    private static final CharacterRule UPPER_CASE_RULE = new CharacterRule(EnglishCharacterData.UpperCase);

    private static final CharacterRule DIGIT_CASE_RULE = new CharacterRule(EnglishCharacterData.Digit);

    /**
     * 自定义特殊字符
     */
    private static final CharacterRule LOCAL_SPECIAL_RULE = new CharacterRule(new CharacterData() {

        @Override
        public String getErrorCode() {
            return "INSUFFICIENT_SPECIAL";
        }

        @Override
        public String getCharacters() {
            return ",.;:?/'|_~`!@#$%^&=+-)(][{}*";
        }
    });

    private static PasswordGenerator passwordGenerator = new PasswordGenerator();

    /**
     * 生成随机密码，包含大写字母、小写字母、数字、特殊字符
     * @param length 密码位数
     */
    public static String getRandomPassword(int length) {
        return passwordGenerator.generatePassword(length, LOWER_CASE_RULE, UPPER_CASE_RULE, DIGIT_CASE_RULE, LOCAL_SPECIAL_RULE);
    }

    /**
     * 密码正则
     */
    private static final String PASSWORD_REGEX_3 = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z,.;:?/'\"|_~`!@#$%^&=+\\-)(\\]\\[\\{\\}*]+$)(?![a-z0-9]+$)(?![a-z,.;:?/'\"|_~`!@#$%^&=+\\-)(\\]\\[\\{\\}*]+$)(?![0-9,.;:?/'\"|_~`!@#$%^&=+\\-)(\\]\\[\\{\\}*]+$)[a-zA-Z0-9,.;:?/'\"|_~`!@#$%^&=+\\-)(\\]\\[\\{\\}*]{8,20}$";

    private static final String PASSWORD_REGEX_4 = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[,.;:?/'\"|_~`!@#$%^&=+\\-)(\\]\\[\\{\\}*])[a-zA-Z0-9,.;:?/'\"|_~`!@#$%^&=+\\-)(\\]\\[\\{\\}*]{8,16}$";    private static final Pattern PWD_PATTERN3 = Pattern.compile(PASSWORD_REGEX_3);

    private static final Pattern PWD_PATTERN4 = Pattern.compile(PASSWORD_REGEX_4);

    /**
     * 密码格式校验，必须包含8-12位字符，大写字母、小写字母、数字、特殊字符
     * @param digit 必须包含的字符种类数量
     */
    public static boolean checkPasswordRex(int digit, String password) {

        Matcher matcher;
        switch (digit) {
            case 4 :
                matcher = PWD_PATTERN4.matcher(password);
                break;
            default:
                matcher = PWD_PATTERN3.matcher(password);
                break;
        }
        return matcher.matches();
    }

    public static void main(String[] args) {
        String pwd = "1Abc#4121.@~AAbc#412";
        boolean b = checkPasswordRex(3, pwd);
        System.out.println(b);


    }
}
