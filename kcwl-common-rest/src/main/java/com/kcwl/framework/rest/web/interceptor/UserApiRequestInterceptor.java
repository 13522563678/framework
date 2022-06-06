package com.kcwl.framework.rest.web.interceptor;

import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import com.kcwl.ddd.infrastructure.session.SessionData;
import com.kcwl.framework.rest.helper.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ckwl
 */
@Slf4j
public class UserApiRequestInterceptor extends HandlerInterceptorAdapter{

    public UserApiRequestInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserAgent requestUserAgent = SessionContext.getRequestUserAgent();
        if (requestUserAgent == null) {
            ResponseHelper.buildResponseBody(CommonCode.INVALID_USER_AGENT, response);
            return false;
        }

        if ( StringUtils.isEmpty(requestUserAgent.getSessionId() ) ) {
            ResponseHelper.buildResponseBody(CommonCode.FIELD_NULL.getCode(), "sessionID不能为空", response);
            return false;
        }

        //校验用户是否登录
        SessionData sessionData = SessionContext.getSessionData();
        if ( sessionData == null ) {
            ResponseHelper.buildResponseBody(CommonCode.UN_LOGIN, response);
            return false;
        }

        if ( !requestUserAgent.getSessionId().equals(sessionData.getSessionId()) ) {
            ResponseHelper.buildResponseBody(CommonCode.OTHER_EQUIPMENT_LOGIN, response);
            return false;
        }
        return true;
    }
}
