package com.kcwl.framework.rest.web.interceptor.impl;

import com.kcwl.common.web.ApiAuthInfo;
import com.kcwl.framework.rest.service.IAuthService;
import com.kcwl.framework.utils.EncryptAesUtil;
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
        sb.append(authInfo.getSsid()).append(authInfo.getNonce()).append(authInfo.getTimeStamp()).append(authInfo.getUrl());

        try {
            //偏移量，必须16位
            String key  = authInfo.getKey().substring(0, AUTH_KEY_SIZE);
            String iv = key;
            String signInfo = EncryptAesUtil.encrypt( sb.toString(), key, iv);
            String md5Hex = DigestUtils.md5Hex(signInfo);
            log.debug("key={}; md5Hex={}; src={}", key, md5Hex, sb.toString());
            if ( md5Hex.equals(authInfo.getSign()) ) {
                return true;
            }
        } catch (Exception e) {
            log.info("{}", e.getMessage());
        }
        return false;
    }
}
