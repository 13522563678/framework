package com.kcwl.framework.rest.helper;


import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.constants.PrefixConstant;
import com.kcwl.ddd.infrastructure.session.SessionData;
import com.kcwl.framework.cache.ICacheService;
import com.kcwl.framework.utils.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ckwl
 */
@Slf4j
@Component
public class SessionCacheProxy {

    @Resource
    private ICacheService userTokenCache;

    public SessionCacheProxy() {
    }

    // CrmUserSessionData 类是否存在
    private final static boolean CRM_USER_SESSION_DATA_CLASS_EXISTS = null != ClassUtil.tryLoadClass("com.kcwl.auth.biz.entity.CrmUserSessionData");

    /**
     * 从缓存获取sessionData
     * @param requestUserAgent
     * @return
     */
    public SessionData getSessionData(UserAgent requestUserAgent) {
        Object sessionData = null;
        String sessionKey = getSessionKey(requestUserAgent.getProduct(), requestUserAgent.getSessionId());

        try {
            if (sessionKey != null) {
                sessionData = userTokenCache.get(sessionKey);
            }
        } catch (Exception e) {
            log.error("无法获取session信息：{}", e.getMessage());
        }
        return (sessionData != null) ? (SessionData) sessionData : null;
    }

    /**
     * 把sessionData保存到缓存里面
     * @param sessionData
     * @param timeout 超时时间，单位为秒
     */
    public void saveSession(SessionData sessionData, int timeout) {
        String sessionKey = getSessionKey(sessionData.getProduct(), sessionData.getSessionId());
        userTokenCache.save(sessionKey, sessionData, timeout);
    }

    /**
     * 从缓存删除sessionData
     * @param sessionData
     */
    public void deleteSession(SessionData sessionData) {
        if ( sessionData != null ) {
            String sessionKey = getSessionKey(sessionData.getProduct(), sessionData.getSessionId());
            userTokenCache.remove(sessionKey);
        }
    }

    private String getSessionKey(Object product, String sessionId) {
        StringBuilder sb = new StringBuilder();
        sb.append(PrefixConstant.REDIS_USER_SESSION).append(product).append(":").append(sessionId);
        return sb.toString();
    }
}
