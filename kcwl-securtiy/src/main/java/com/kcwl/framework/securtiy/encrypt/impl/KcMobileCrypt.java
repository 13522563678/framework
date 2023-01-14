package com.kcwl.framework.securtiy.encrypt.impl;

import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.exception.BizException;
import com.kcwl.framework.securtiy.codec.PassDict;
import com.kcwl.framework.securtiy.encrypt.CryptMode;
import com.kcwl.framework.securtiy.encrypt.IKcCrypt;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author ckwl
 */
public class KcMobileCrypt implements IKcCrypt {


    private static final int HEAD_UN_ENCRYPT_LEN = 3;
    private static final int TAIL_UN_ENCRYPT_LEN = 2;
    private static final int MOBILE_MIN_LEN = HEAD_UN_ENCRYPT_LEN+TAIL_UN_ENCRYPT_LEN;
    private static final int PASS_GROUP_COUNT = 6;
    private static final int MIN_KEY_LEN = 6;

    private PassDict passDict;

    public KcMobileCrypt(String password) {
        passDict = createPassDict(password);
    }

    @Override
    public String encrypt(String srcMobile) {
        return convert(srcMobile,CryptMode.ENCODE);
    }

    @Override
    public String decrypt(String encryptedMobile) {
        return convert(encryptedMobile,CryptMode.DECODE);
    }

    private String convert(String s, CryptMode cryptMode){
        if ( StringUtils.isEmpty(s) || s.length()<=MOBILE_MIN_LEN) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length());
        sb.append(s.substring(0, HEAD_UN_ENCRYPT_LEN));
        for ( int i=HEAD_UN_ENCRYPT_LEN; i<s.length()-TAIL_UN_ENCRYPT_LEN; i++){
            int groupId = (i+1)%PASS_GROUP_COUNT;
            if (CryptMode.DECODE.equals(cryptMode) ) {
                sb.append(passDict.decode(s.charAt(i), groupId));
            } else {
                sb.append(passDict.encode(s.charAt(i), groupId));
            }
        }
        sb.append(s.substring(s.length()-TAIL_UN_ENCRYPT_LEN));
        return sb.toString();
    }
    private PassDict createPassDict(String password) {
        if ( !NumberUtils.isDigits(password) || (password.length()<MIN_KEY_LEN) ) {
            throw new BizException(CommonCode.ERROR_SECRET_KEY.getCode(), CommonCode.ERROR_SECRET_KEY.getDescription());
        }

        return new PassDict(password, PASS_GROUP_COUNT);
    }
}
