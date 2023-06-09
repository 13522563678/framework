package com.kcwl.framework.rest.web.interceptor;

import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.constants.GlobalConstant;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import com.kcwl.framework.grayscale.utils.GrayMarkContextHolder;
import com.kcwl.framework.rest.helper.SessionJwtHelper;
import com.kcwl.framework.rest.web.CommonWebProperties;
import com.kcwl.tenant.TenantDataHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * @author ckwl
 */
@Slf4j
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private CommonWebProperties.AppAuthInfo appAuthInfo;

    public RestTemplateInterceptor(CommonWebProperties.AppAuthInfo appAuthInfo) {
        this.appAuthInfo = appAuthInfo;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        HttpHeaders headers = httpRequest.getHeaders();
        String tenantId = TenantDataHolder.get();
        if (tenantId != null) {
            headers.add(GlobalConstant.AGENT_TENANT_FIELD_NAME, tenantId);
        }
        UserAgent requestUserAgent = SessionContext.getRequestUserAgent();
        if (requestUserAgent != null) {
            headers.add(UserAgent.REQUEST_AGENT_HEADER_NAME, requestUserAgent.nextRequestUserAgent().toString());
            headers.add(HttpHeaders.COOKIE, requestUserAgent.getCookieValue());
            applyJwt(headers, requestUserAgent);
        }
        headers.add(UserAgent.REQUEST_AGENT_CLIENT_FIELD_NAME, UserAgent.AGENT_CLIENT_FEIGN);

        if ( appAuthInfo.isEnabled() ) {
            headers.add(GlobalConstant.KC_APP_ID, appAuthInfo.getAppId());
        }
        // 请求灰度标记
        if (null != RequestContextHolder.getRequestAttributes() &&
                !StringUtils.isEmpty(((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest().getHeader(GlobalConstant.GRAY_REQUEST_HEADER_KEY))) {
            String grayMark = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest().getHeader(GlobalConstant.GRAY_REQUEST_HEADER_KEY);
            headers.add(GlobalConstant.GRAY_REQUEST_HEADER_KEY, grayMark);
        }
        if (!headers.containsKey(GlobalConstant.GRAY_REQUEST_HEADER_KEY) && null != GrayMarkContextHolder.get()) {
            headers.add(GlobalConstant.GRAY_REQUEST_HEADER_KEY, GrayMarkContextHolder.get());
        }
        if (!headers.containsKey(GlobalConstant.GRAY_REQUEST_HEADER_KEY)) {
            headers.add(GlobalConstant.GRAY_REQUEST_HEADER_KEY, "default");
        }
        return clientHttpRequestExecution.execute(httpRequest, body);
    }

    private void applyJwt(HttpHeaders headers, UserAgent requestUserAgent) {
        String jwtSession =  requestUserAgent.getJwtSession();
        if ( jwtSession == null ) {
            jwtSession = SessionJwtHelper.createJwtSession(requestUserAgent, SessionContext.getSessionData());
        }
        if ( jwtSession != null ) {
            headers.add(GlobalConstant.KC_SESSION_JWT, jwtSession);
        }
        if ( log.isDebugEnabled() ) {
            if ( jwtSession != null ) {
                log.debug("jwtSession size: {}", jwtSession.length());
            } else {
                log.debug("jwtSession is empty");
            }
        }
    }
}
