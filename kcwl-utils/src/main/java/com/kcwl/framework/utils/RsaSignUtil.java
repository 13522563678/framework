package com.kcwl.framework.utils;

import cn.hutool.core.codec.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaSignUtil {
    private static final String FLAG_RSA = "RSA";
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 私钥对象
     */
    private PrivateKey privateKey;

    public RsaSignUtil(String privateKey) throws Exception {
        this.privateKey = getPrivateKey(privateKey);
    }

    /**
     * 获取签名
     *
     * @return
     * @throws Exception
     */
    public String getSign(String str) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);// 签名的算法
        signature.initSign(privateKey);
        signature.update(str.getBytes());
        return Base64.encode(signature.sign());
    }

    /**
     * 验证签名
     *
     * @param data
     *            原文
     * @param mySign
     *            签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, byte[] mySign,String publicKey) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(getPublicKey(publicKey));
        signature.update(data);
        return signature.verify(mySign);
    }


    /**
     * 通过预制公钥生成PublicKey
     *
     * @param publicKey
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] encoded =  Base64.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory factory = KeyFactory.getInstance(FLAG_RSA);
        return factory.generatePublic(keySpec);
    }

    /**
     * 将指定的字符串转换为私钥key
     *
     * @param priKey
     * @return
     * @throws Exception
     */
    private PrivateKey getPrivateKey(String priKey) throws Exception {
        // 首先进行base64解码。
        byte[] encoded =  Base64.decode(priKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory factory = KeyFactory.getInstance(FLAG_RSA);
        return factory.generatePrivate(keySpec);
    }

}
