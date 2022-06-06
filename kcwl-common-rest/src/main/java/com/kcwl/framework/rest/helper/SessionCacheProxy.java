package com.kcwl.framework.rest.helper;


import com.kcwl.ddd.application.constants.ProductEnum;
import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.constants.PrefixConstant;
import com.kcwl.ddd.infrastructure.session.SessionData;
import com.kcwl.framework.cache.ICacheService;
import com.kcwl.framework.utils.ClassUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ckwl
 */
@Slf4j
public class SessionCacheProxy {
    private ICacheService jsonCacheService;
    private ICacheService tokenCacheService;

    public SessionCacheProxy(ICacheService jsonCacheService, ICacheService tokenCacheService) {
        this.jsonCacheService  = jsonCacheService;
        this.tokenCacheService = tokenCacheService;
    }

    // CrmUserSessionData 类是否存在
    private final static boolean CRM_USER_SESSION_DATA_CLASS_EXISTS = null != ClassUtil.tryLoadClass("com.kcwl.auth.biz.entity.CrmUserSessionData");

    public SessionData getSessionData(UserAgent requestUserAgent) {
        Object  obj = null;
        String sessionKey = null;
        boolean isNewVersion = false;
        if ( ProductEnum.CARRIER_APP.equals(requestUserAgent.getProduct()) ) {
            sessionKey = PrefixConstant.REDIS_CARRIER_LOGIN_TOKEN + requestUserAgent.getToken();
        } else if ( ProductEnum.SHIPPER_WEB.equals(requestUserAgent.getProduct()) || ProductEnum.SHIPPER_APP.equals(requestUserAgent.getProduct())  )  {
            sessionKey = PrefixConstant.REDIS_KS_LOGIN_TOKEN + requestUserAgent.getSessionId();
            isNewVersion = true;
        }  else if ( ProductEnum.CRM_WEB.equals(requestUserAgent.getProduct()) )  {
            if (CRM_USER_SESSION_DATA_CLASS_EXISTS) {
                sessionKey = PrefixConstant.REDIS_CRM_LOGIN_TOKEN + requestUserAgent.getSessionId();
                isNewVersion = true;
            }
        }

        try {
            if ( sessionKey != null ) {
                if (isNewVersion) {
                    obj = jsonCacheService.get(sessionKey);
                } else {
                    obj = tokenCacheService.get(sessionKey);
                }
            }
        } catch (Exception e) {
            log.error("无法获取session信息：{}", e.getMessage());
        }
        return (obj!=null) ? (SessionData)obj : null;
    }
}
