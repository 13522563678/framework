package com.kcwl.framework.securtiy.encrypt;

/**
 * @author ckwl
 */
public interface IKcCrypt {
    /**
     * 数据加密
     * @param plainText 待加密的字符串
     * @return 返回加密后的数据
     */
    public String encrypt(String plainText);

    /**
     * 解密数据
     * @param encryptedText  加密后的数据
     * @return 返回解密后的数据     *
     */
    public String decrypt(String encryptedText);

}
