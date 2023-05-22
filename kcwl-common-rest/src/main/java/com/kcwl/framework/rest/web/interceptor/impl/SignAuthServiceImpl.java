package com.kcwl.framework.rest.web.interceptor.impl;

import com.kcwl.common.web.ApiAuthInfo;
import com.kcwl.framework.rest.service.IAuthService;
import com.kcwl.framework.utils.KcEncryptAesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author ckwl
 */
@Slf4j
public class SignAuthServiceImpl implements IAuthService {

    private static final int AUTH_KEY_SIZE = 16;

    public SignAuthServiceImpl() {

    }

    @Override
    public boolean verify(Object obj) {
        if ( !(obj instanceof ApiAuthInfo) ) {
            log.error("verify param is not valid!");
            return false;
        }
        ApiAuthInfo authInfo = (ApiAuthInfo)obj;

        if ( authInfo.getKey().length() < AUTH_KEY_SIZE ) {
            log.error("key size less than 16!");
            return false;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(authInfo.getKcToken()).append(authInfo.getSsid()).append(authInfo.getNonce()).append(authInfo.getTimeStamp()).append(authInfo.getUrl());

        try {
            //偏移量，必须16位
            String key  = authInfo.getKey().substring(0, AUTH_KEY_SIZE);
            String iv = key;
            String signSrc = sb.toString();

            String signInfo = KcEncryptAesUtil.encrypt(signSrc, key, iv);
            String md5Hex = DigestUtils.md5Hex(signInfo);
            if ( md5Hex.equals(authInfo.getSign()) ) {
                return true;
            }
            log.warn("si0={}; si1={}; si2={}; k={}; sr={}", signInfo, authInfo.getSign(), md5Hex, key, signSrc);

        } catch (Exception e) {
            log.info("签名异常：{}", e.getMessage());
        }
        return false;
    }
}
