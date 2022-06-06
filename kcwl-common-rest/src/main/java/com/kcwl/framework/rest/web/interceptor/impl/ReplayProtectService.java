package com.kcwl.framework.rest.web.interceptor.impl;

import cn.hutool.cache.Cache;
import com.kcwl.common.web.ApiAuthInfo;
import com.kcwl.framework.rest.service.IAuthService;
import com.kcwl.framework.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * @author ckwl
 */
@Slf4j
public class ReplayProtectService implements IAuthService {

    Cache<String, Long> apiRepeatCache;

    private long requestTimeOut = 180*1000;

    public ReplayProtectService(Cache<String, Long> apiRepeatCache, long requestTimeOut) {
        this.apiRepeatCache = apiRepeatCache;
        this.requestTimeOut = requestTimeOut;
    }

    /**
     * @param obj
     * @return
     */
    @Override
    public boolean verify(Object obj) {
        if ( !(obj instanceof ApiAuthInfo) ) {
            log.error("verify param is not valid!");
            return false;
        }
        ApiAuthInfo authInfo = (ApiAuthInfo)obj;
        long timestamp = StringUtil.toLong(authInfo.getTimeStamp(), 0);
        long timeInterval = Math.abs(System.currentTimeMillis() - timestamp);
        if ( timeInterval > requestTimeOut ) {
            return false;
        }
        if ( apiRepeatCache.get(authInfo.getNonce()) != null) {
            return false;
        }
        apiRepeatCache.put(authInfo.getNonce(), timestamp);
        return true;
    }
}
