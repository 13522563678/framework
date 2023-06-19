package com.kcwl.framework.rest.web.interceptor;

import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.constants.GlobalConstant;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import com.kcwl.ddd.infrastructure.session.SessionData;
import com.kcwl.framework.rest.helper.RequestUserAgentHelper;
import com.kcwl.framework.rest.helper.SessionCacheProxy;
import com.kcwl.framework.rest.helper.SessionJwtHelper;
import com.kcwl.framework.utils.StringUtil;
import com.kcwl.tenant.TenantDataHolder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ckwl
 */
@Slf4j
public class UserSessionInterceptor extends HandlerInterceptorAdapter {

    private SessionCacheProxy sessionCacheProxy;
    private Set<String> ignorePathPatterns;
    private boolean ignoreSession;
    public UserSessionInterceptor(SessionCacheProxy sessionCacheProxy, List<String> ignorePathPatterns, boolean ignoreSession) {
        this.sessionCacheProxy = sessionCacheProxy;
        this.ignorePathPatterns = new HashSet<>(ignorePathPatterns);
        this.ignoreSession = ignoreSession;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SessionData sessionData = null;
        UserAgent requestUserAgent = RequestUserAgentHelper.getRequestUserAgent(request);
        if ( requestUserAgent != null ) {
            SessionContext.setRequestUserAgent(requestUserAgent);
            //是否是直接从服务器端发起的请求，如job
            if (!requestUserAgent.isServerRequest()) {
                if (!ignoreSession && !ignoreRequestUri(request)) {
                    String jwtSession = request.getHeader(GlobalConstant.KC_SESSION_JWT);
                    if ( (jwtSession != null) && SessionJwtHelper.isEnableJwtAuth() ) {
                        sessionData = SessionJwtHelper.getJwtSessionData(jwtSession, requestUserAgent);;
                        if ( log.isDebugEnabled() ) {
                            log.debug("read sessionData from from  jwt");
                        }
                        requestUserAgent.setJwtSession(jwtSession);
                    } else {
                        sessionData = sessionCacheProxy.getSessionData(requestUserAgent);
                    }
                    SessionContext.setSessionData(sessionData);
                }
                setUserAgentPlatform(requestUserAgent, request, sessionData);
                TenantDataHolder.set(requestUserAgent.getUserPlatformNo());
            }
        }
        String apiVersion = request.getHeader("x-api-version");
        if (!StringUtil.isEmpty(apiVersion) ) {
            SessionContext.setApiVersion(Integer.parseInt(apiVersion));
        }

        String clientType = request.getHeader(UserAgent.REQUEST_AGENT_CLIENT_FIELD_NAME);
        SessionContext.setRequestClient(clientType);

        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SessionContext.setApiVersion(null);
        SessionContext.setRequestUserAgent(null);
        SessionContext.setRequestClient(null);
        SessionContext.setSessionData(null);
        super.afterCompletion(request, response, handler, ex);
    }

    private boolean ignoreRequestUri(HttpServletRequest request) {
        String reqUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (!StringUtil.isEmpty(contextPath)) {
            reqUri = reqUri.substring(contextPath.length());
        }
        return ignorePathPatterns.contains(reqUri);
    }

    private void setUserAgentPlatform(UserAgent requestUserAgent, HttpServletRequest request, SessionData sessionData) {
        //优先从请求中指定的平台码;其次从用户会话中选择
        if (sessionData != null) {
            String userTenantId = sessionData.getPlatformNo();
            if ( !StringUtil.isEmpty(userTenantId) ) {
                requestUserAgent.setUserPlatformNo(userTenantId);
            }
        }
    }
}
