package com.kcwl.framework.rest.helper;


import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.constants.GlobalConstant;
import com.kcwl.framework.utils.RequestUtil;
import com.kcwl.framework.utils.StringUtil;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ckwl
 */
public class RequestUserAgentHelper {

    public static UserAgent getRequestUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader(UserAgent.REQUEST_AGENT_HEADER_NAME);

        if ( !StringUtil.isEmpty(userAgent) ) {
            return new UserAgent(userAgent);
        }

        UserAgent requestUserAgent = getRequestUserAgentFromCookie(request);

        setKcTokenAndTrace(requestUserAgent, request);

        return requestUserAgent;
    }

    private static UserAgent getRequestUserAgentFromCookie(HttpServletRequest request) {
        UserAgent  requestUserAgent =null;
        String platform = RequestUtil.getCookieValue(request, UserAgent.FIELD_COOKIE_PLATFORM);
        String product = RequestUtil.getCookieValue(request, UserAgent.FILED_PRODUCT);
        if ( platform != null && product != null ) {
            requestUserAgent = new UserAgent();
            requestUserAgent.setProduct(product);
            requestUserAgent.setPlatform(platform);
            requestUserAgent.setToken(RequestUtil.getCookieValue(request, UserAgent.FILED_TOKEN));
            requestUserAgent.setSessionId(RequestUtil.getCookieValue(request, UserAgent.FILED_SESSION_ID));
            requestUserAgent.setAppVersion(RequestUtil.getCookieValue(request, UserAgent.FILED_APPVERSION));
            requestUserAgent.setOsType(RequestUtil.getCookieValue(request, UserAgent.FILED_OS_TYPE));
            requestUserAgent.setAppType(RequestUtil.getCookieValue(request, UserAgent.FILED_APP_TYPE));
            requestUserAgent.setOperatingSystem(RequestUtil.getCookieValue(request, UserAgent.FILED_OPERATING_SYSTEM));
        }
        return requestUserAgent;
    }


    private static String getPlatformNo(HttpServletRequest request) {
        String platformNo = request.getHeader(GlobalConstant.AGENT_TENANT_FIELD_NAME);
        return !StringUtil.isEmpty(platformNo) ? platformNo : GlobalConstant.UNIOIN_TENANT_ID_V2;
    }

    private static void setKcTokenAndTrace(UserAgent  requestUserAgent, HttpServletRequest request) {
        if ( requestUserAgent != null ) {
            requestUserAgent.setKcToken(getHeaderOrParam(request, GlobalConstant.KC_TOKEN));
            requestUserAgent.setKcTrace(getHeaderOrParam(request, GlobalConstant.KC_TRACE));
        }
    }
    private static String getHeaderOrParam(HttpServletRequest request, String name) {
        String value = request.getHeader(name);
        if ( value == null ) {
            value = request.getParameter(name);
        }
        return value;
    }
}
