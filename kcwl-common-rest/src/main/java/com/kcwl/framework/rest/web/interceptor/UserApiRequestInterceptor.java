package com.kcwl.framework.rest.web.interceptor;

import com.kcwl.common.web.ApiAuthInfo;
import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.constants.GlobalConstant;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import com.kcwl.ddd.infrastructure.session.SessionData;
import com.kcwl.framework.auth.IKcSsoAuth;
import com.kcwl.framework.rest.helper.ConfigBeanName;
import com.kcwl.framework.rest.helper.ResponseHelper;
import com.kcwl.framework.rest.helper.SessionCacheProxy;
import com.kcwl.framework.rest.service.IAuthService;
import com.kcwl.framework.rest.web.CommonWebProperties;
import com.kcwl.framework.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author ckwl
 */
@Slf4j
public class UserApiRequestInterceptor extends HandlerInterceptorAdapter{

    List<String> supportProducts;
    IKcSsoAuth kcSsoAuth;
    IAuthService authService;
    SessionCacheProxy sessionCacheProxyInstance;

    public UserApiRequestInterceptor(List<String> supportProducts, IAuthService authService) {
        this.supportProducts = supportProducts;
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserAgent userAgent = SessionContext.getRequestUserAgent();
        if (userAgent == null) {
            ResponseHelper.buildResponseBody(CommonCode.INVALID_USER_AGENT, response);
            return false;
        }

        if ( StringUtils.isEmpty(userAgent.getSessionId() ) ) {
            ResponseHelper.buildResponseBody(CommonCode.UN_LOGIN, response);
            return false;
        }

        //校验用户是否登录
        SessionData sessionData = SessionContext.getSessionData();

        if ( (sessionData == null) && isSsoProduct(userAgent.getProduct()) ) {
            sessionData = ssoLogin();
        }

        if ( sessionData == null ) {
            ResponseHelper.buildResponseBody(CommonCode.UN_LOGIN, response);
            return false;
        }
        if ( !KcRequestContextUtil.isInternalRequest() ) {
            if ( !isSessionValid(userAgent, sessionData) ) {
                ResponseHelper.buildResponseBody(CommonCode.OTHER_EQUIPMENT_LOGIN, response);
                return false;
            }
            if ( enableSignVerify() && !authService.verify(getApiAuthInfo(request, userAgent, sessionData))) {
                ResponseHelper.buildResponseBody(CommonCode.AUTH_ERROR_CODE, response);
                return false;
            }
        }

        return true;
    }

    private boolean isSsoProduct(String product) {
        return (supportProducts != null) && supportProducts.contains(product);
    }

    private SessionData ssoLogin() {
        IKcSsoAuth kcSsoAuthImpl = getKcSsoAuth();
        if ( log.isDebugEnabled() ) {
            log.debug("begin ssoLogin");
        }
        if ( kcSsoAuthImpl == null ) {
            log.warn("Can not find IKcSsoAuth implementation");
            return null;
        }
        SessionData sessionData = kcSsoAuthImpl.ssoLogin();
        SessionContext.setSessionData(sessionData);
        return sessionData;
    }

    private IKcSsoAuth getKcSsoAuth() {
        if ( kcSsoAuth == null ) {
            kcSsoAuth = ContextBeanUtil.getBean(IKcSsoAuth.class);
        }
        return kcSsoAuth;
    }

    private ApiAuthInfo getApiAuthInfo(HttpServletRequest request, UserAgent userAgent, SessionData sessionData) {
        ApiAuthInfo apiAuthInfo = new ApiAuthInfo();
        apiAuthInfo.setKcToken(userAgent.getKcToken());
        apiAuthInfo.setSsid(userAgent.getSessionId());
        apiAuthInfo.setNonce(userAgent.getKcTrace());
        apiAuthInfo.setTimeStamp(getAppSignValue(request, GlobalConstant.KC_APP_TIMESTAMP));
        apiAuthInfo.setSign(getAppSignValue(request, GlobalConstant.KC_APP_SIGN));
        apiAuthInfo.setUrl(request.getRequestURI());
        apiAuthInfo.setKey(sessionData.getKey());
        return apiAuthInfo;
    }

    private boolean enableSignVerify() {
        CommonWebProperties commonWebProperties = KcBeanRepository
            .getInstance().getBean(ConfigBeanName.COMMON_WEB_CONFIG_NAME, CommonWebProperties.class);
        return commonWebProperties.getApi().isSignVerify();
    }

    private String getAppSignValue(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if ( StringUtils.isEmpty(value) ) {
            value = RequestUtil.getCookieValue(request, name);
        }
        return value;
    }

    private boolean isSessionValid(UserAgent userAgent, SessionData sessionData) {
        CommonWebProperties commonWebProperties = KcBeanRepository.getInstance().getBean(ConfigBeanName.COMMON_WEB_CONFIG_NAME, CommonWebProperties.class);
        SessionCacheProxy sessionCacheProxy = getSessionCacheProxy();
        if ( commonWebProperties.getSession().isSingleSession() && (sessionCacheProxy != null) ) {
            String activeSessionId = sessionCacheProxy.getActiveSessionId(userAgent, sessionData.getUserId());
            return userAgent.getSessionId().equals(activeSessionId);
        }
        return true;
    }

    private SessionCacheProxy getSessionCacheProxy() {
        if (  sessionCacheProxyInstance == null ) {
            sessionCacheProxyInstance =ContextBeanUtil.getBean(SessionCacheProxy.class);
        }
        return sessionCacheProxyInstance;
    }
}
