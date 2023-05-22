package com.kcwl.framework.rest.web.interceptor;

import com.kcwl.common.web.ApiAuthInfo;
import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import com.kcwl.ddd.infrastructure.session.SessionData;
import com.kcwl.framework.auth.IKcSsoAuth;
import com.kcwl.framework.rest.helper.ResponseHelper;
import com.kcwl.framework.rest.service.IAuthService;
import com.kcwl.framework.utils.ContextBeanUtil;
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
            ResponseHelper.buildResponseBody(CommonCode.FIELD_NULL.getCode(), "未登录00006", response);
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
        if ( !userAgent.getSessionId().equals(sessionData.getSessionId()) ) {
            ResponseHelper.buildResponseBody(CommonCode.OTHER_EQUIPMENT_LOGIN, response);
            return false;
        }
        if ( (authService != null)  && authService.verify(getApiAuthInfo(request, userAgent, sessionData))) {
            ResponseHelper.buildResponseBody(CommonCode.AUTH_ERROR_CODE, response);
            return false;
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
        apiAuthInfo.setTimeStamp(userAgent.getTimestamp());
        apiAuthInfo.setSign(userAgent.getAppSign());
        apiAuthInfo.setUrl(request.getRequestURI());
        apiAuthInfo.setKey(sessionData.getKey());
        return apiAuthInfo;
    }
}
