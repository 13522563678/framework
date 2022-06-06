package com.kcwl.framework.rest.helper;


import com.kcwl.ddd.application.constants.ProductEnum;
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

        UserAgent requestUserAgent = getKsRequestUserAgent(request);

        if ( requestUserAgent == null ) {
            requestUserAgent = getKsRequestUserAgent(request);
        }

        setKcTokenAndTrace(requestUserAgent, request);

        return requestUserAgent;
    }

    private static UserAgent getRequestUserAgentFromCookie(HttpServletRequest request) {
        UserAgent  requestUserAgent =null;
        String platform = RequestUtil.getCookieValue(request, UserAgent.FIELD_PLATFORM);
        String product = RequestUtil.getCookieValue(request, UserAgent.FILED_PRODUCT);
        if ( platform != null && product != null ) {
            requestUserAgent = new UserAgent();
            requestUserAgent.setProduct(product);
            requestUserAgent.setPlatform(platform);
            requestUserAgent.setToken(RequestUtil.getCookieValue(request, UserAgent.FILED_TOKEN));
            if ( ProductEnum.CARRIER_APP.equals(product) ) {
                requestUserAgent.setSessionId(RequestUtil.getCookieValue(request, UserAgent.FILED_SESSION_ID));
            } else {
                requestUserAgent.setSessionId(request.getHeader(UserAgent.FIELD_USER_SSID));
            }
            requestUserAgent.setAppVersion(RequestUtil.getCookieValue(request, UserAgent.FILED_APPVERSION));
        }
        return requestUserAgent;
    }


    private static UserAgent getKsRequestUserAgent(HttpServletRequest request) {
        UserAgent  requestUserAgent = null;
        String sessionId = request.getHeader(UserAgent.FIELD_USER_SSID);
        if ( sessionId != null ) {
            requestUserAgent = new UserAgent();
            requestUserAgent.setSessionId(sessionId);
            requestUserAgent.setProduct(ProductEnum.SHIPPER_WEB.getCode());
            requestUserAgent.setPlatform(getPlatformNo(request));
        }
        return requestUserAgent;
    }

    private static String getPlatformNo(HttpServletRequest request) {
        String platformNo = request.getHeader(GlobalConstant.AGENT_TENANT_FIELD_NAME);
        return !StringUtil.isEmpty(platformNo) ? platformNo : GlobalConstant.UNIOIN_TENANT_ID_V2;
    }

    private static void setKcTokenAndTrace(UserAgent  requestUserAgent, HttpServletRequest request) {
        if ( requestUserAgent != null ) {
            requestUserAgent.setKcToken(request.getHeader(GlobalConstant.KC_TOKEN));
            requestUserAgent.setKcTrace(request.getHeader(GlobalConstant.KC_TRACE));
        }
    }
}
