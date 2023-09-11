package com.kcwl.framework.rest.helper;


import com.kcwl.ddd.application.constants.YesNoEnum;
import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.constants.PrefixConstant;
import com.kcwl.ddd.infrastructure.session.SessionData;
import com.kcwl.framework.cache.ICacheService;
import com.kcwl.framework.rest.web.CommonWebProperties;
import com.kcwl.framework.session.ISessionEventListener;
import com.kcwl.framework.utils.*;
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

    @Resource
    CommonWebProperties commonWebProperties;

    public SessionCacheProxy() {
    }

    // CrmUserSessionData 类是否存在
    private final static boolean CRM_USER_SESSION_DATA_CLASS_EXISTS = null != ClassUtil.tryLoadClass("com.kcwl.auth.biz.entity.CrmUserSessionData");
    private final static String PREFIX_DELIMITER=":";
    /**
     * 从缓存获取sessionData
     * @param requestUserAgent
     * @return
     */
    public SessionData getSessionData(UserAgent requestUserAgent) {
        SessionData sessionData = null;
        String sessionKey = determineSessionKey(requestUserAgent, requestUserAgent.getSessionId());
        try {
            sessionData = (SessionData)userTokenCache.get(sessionKey);
            if ( sessionData != null ) {
                if ( !RequestUtil.isInternalRequest() && commonWebProperties.getSession().isSingleSession() ) {
                    sessionData.setSessionId(getActiveSessionId(requestUserAgent, sessionData.getUserId()));
                }
                renewSession(sessionData, sessionKey);
            }
        } catch (Exception e) {
            log.error("无法获取session信息：{}", e.getMessage());
        }
        return sessionData;
    }


    /**
     * 把sessionData保存到缓存里面
     * @param sessionData
     * @param timeout 超时时间，单位为秒
     */
    public void saveSession(SessionData sessionData, int timeout) {
        String sessionKey = getSessionKey(sessionData.getProduct(), sessionData.getSessionId());
        userTokenCache.save(sessionKey, sessionData, timeout);

        if ( !RequestUtil.isInternalRequest() ) {
            //把当前会话设置为有效的会话
            String activeSessionKey = getSessionKey(sessionData.getProduct(), sessionData.getUserId());
            userTokenCache.save(activeSessionKey, sessionData.getSessionId());
        }
    }

    /**
     * 把sessionData保存到缓存里面
     * @param sessionData
     * @param platformNo 平台码
     * @param timeout 超时时间，单位为秒
     */
    public void saveSession(SessionData sessionData, String platformNo, int timeout) {
        String sessionKey = getSessionKey(platformNo, sessionData.getProduct(), sessionData.getSessionId());
        userTokenCache.save(sessionKey, sessionData, timeout);
        if ( !RequestUtil.isInternalRequest() ) {
            //把当前会话设置为有效的会话
            String activeSessionKey = getSessionKey(platformNo, sessionData.getProduct(), sessionData.getUserId());
            userTokenCache.save(activeSessionKey, sessionData.getSessionId());
        }
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

    /**
     * 从缓存删除sessionData
     * @param
     */
    public void deleteSession(String product, String sessionId) {
        String sessionKey = getSessionKey(product, sessionId);
        userTokenCache.remove(sessionKey);
    }

    /**
     * 从缓存删除sessionData
     * @param
     */
    public void deleteSession(String platformNo, String product, String sessionId) {
        String sessionKey = getSessionKey(platformNo, product, sessionId);
        userTokenCache.remove(sessionKey);
    }


    private String getSessionKey(Object product, Object sessionId) {
        StringBuilder sb = new StringBuilder();
        String realProduct = commonWebProperties.getSession().getRealProduct(product);
        sb.append(PrefixConstant.REDIS_USER_SESSION).append(realProduct).append(PREFIX_DELIMITER).append(sessionId);
        return sb.toString();
    }

    private String getSessionKey(String platformNo, Object product, Object sessionId) {
        StringBuilder sb = new StringBuilder();
        String realProduct = commonWebProperties.getSession().getRealProduct(product);
        sb.append(PrefixConstant.REDIS_USER_SESSION).append(platformNo).append(PREFIX_DELIMITER).append(realProduct).append(PREFIX_DELIMITER).append(sessionId);
        return sb.toString();
    }

    private String determineSessionKey(UserAgent userAgent, Object key) {
        String sessionKey = null;
        CommonWebProperties.AppPodInfo appPodInfo = commonWebProperties.getAppPod();
        if ( !appPodInfo.isIsolation() ) {
            sessionKey = getSessionKey(userAgent.getProduct(), key);
        } else {
            String platformNo;
            if ( appPodInfo.isSupportUserPlatform() ) {
                platformNo = userAgent.getUserPlatformNo();
            } else {
                platformNo = userAgent.getPlatform();
            }
            sessionKey = getSessionKey(platformNo, userAgent.getProduct(), key);
        }
        return sessionKey;
    }

    private boolean renewSession(SessionData sessionData, String sessionKey) {
        CommonWebProperties.SessionConfig sessionConfig = KcBeanRepository.getInstance().getBean(ConfigBeanName.SESSION_CONFIG_NAME, CommonWebProperties.SessionConfig.class);
        if ( sessionConfig != null && sessionConfig.isRenew() ) {
            String renewSessionKey = sessionKey + ":renew";
            Object renewSessionFlag = userTokenCache.get(renewSessionKey);
            if ( renewSessionFlag == null ) {
                userTokenCache.expire(sessionKey, sessionConfig.getTimeout());
                userTokenCache.save(renewSessionKey, YesNoEnum.YEA.getValue(), sessionConfig.getTimeout()/2);
                ISessionEventListener eventListener = getSessionEventListener();
                if ( eventListener != null ) {
                    eventListener.onSessionRenew(sessionData,sessionConfig.getTimeout());
                }
                log.info("renew session timeout {} by {}", sessionData.getSessionId(), sessionConfig.getTimeout());
                return true;
            }
        }
        return false;
    }

    private String getActiveSessionId(UserAgent userAgent, Long userId) {
        String activeSessionKey = determineSessionKey(userAgent, userId);
        return (String)userTokenCache.get(activeSessionKey);
    }

    private ISessionEventListener getSessionEventListener() {
        return ContextBeanUtil.getBean(ISessionEventListener.class);
    }
}
