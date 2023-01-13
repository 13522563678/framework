package com.kcwl.framework.securtiy.encrypt.impl;

import com.kcwl.framework.securtiy.codec.PassDict;
import com.kcwl.framework.securtiy.encrypt.CryptMode;
import com.kcwl.framework.securtiy.encrypt.IKcCrypt;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ckwl
 */
public class KcSimpleCrypt implements IKcCrypt {

    private PassDict passDict;
    private int groupCount;

    public KcSimpleCrypt(String password){
        groupCount = 1;
        passDict = new PassDict(password, this.groupCount);
    }

    public KcSimpleCrypt(String password, int groupCount){
        if ( groupCount<=0 ) {
            this.groupCount=1;
        }
        passDict = new PassDict(password, this.groupCount);
    }


    /**
     * 数据加密
     *
     * @param plainText 待加密的字符串
     * @return 返回加密后的数据
     */
    @Override
    public String encrypt(String plainText) {
        return convert(plainText, CryptMode.ENCODE);
    }

    /**
     * 解密数据
     *
     * @param encryptedText 加密后的数据
     * @return 返回解密后的数据     *
     */
    @Override
    public String decrypt(String encryptedText) {
        return convert(encryptedText, CryptMode.DECODE);
    }

    private String convert(String s, CryptMode cryptMode) {
        if ( StringUtils.isEmpty(s) ) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length());
        for ( int i=0; i<s.length(); i++){
            int groupId = (i+1)%groupCount;
            if (CryptMode.DECODE.equals(cryptMode) ) {
                sb.append(passDict.decode(s.charAt(i), groupId));
            } else {
                sb.append(passDict.encode(s.charAt(i), groupId));
            }
        }
        return sb.toString();
    }
}
