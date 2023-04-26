package com.kcwl.framework.rest.web.interceptor;

import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.constants.GlobalConstant;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import com.kcwl.framework.grayscale.utils.GrayMarkContextHolder;
import com.kcwl.framework.rest.helper.SessionJwtHelper;
import com.kcwl.framework.rest.web.CommonWebProperties;
import com.kcwl.tenant.TenantDataHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * @author ckwl
 */
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {

    private CommonWebProperties.AppAuthInfo appAuthInfo;

    public FeignRequestInterceptor(CommonWebProperties.AppAuthInfo appAuthInfo) {
        this.appAuthInfo = appAuthInfo;
    }

    @Override
    public void apply(RequestTemplate template) {
        String tenantId = TenantDataHolder.get();
        if (tenantId != null) {
            template.header(GlobalConstant.AGENT_TENANT_FIELD_NAME, tenantId);
        }
        UserAgent requestUserAgent = SessionContext.getRequestUserAgent();
        if (requestUserAgent != null) {
            template.header(UserAgent.REQUEST_AGENT_HEADER_NAME, requestUserAgent.nextRequestUserAgent().toString());
            String jwtSession =  requestUserAgent.getJwtSession();
            if ( jwtSession != null ) {
                jwtSession = SessionJwtHelper.createJwtSession(requestUserAgent, SessionContext.getSessionData());
            }
            if ( jwtSession != null ) {
                template.header(GlobalConstant.KC_SESSION_JWT, jwtSession);
            }
            if ( log.isDebugEnabled() ) {
                log.debug("jwt={}", jwtSession);
            }
        }
        template.header(UserAgent.REQUEST_AGENT_CLIENT_FIELD_NAME, UserAgent.AGENT_CLIENT_FEIGN);

        if ( appAuthInfo.isEnabled() ) {
            template.header(GlobalConstant.KC_APP_ID, appAuthInfo.getAppId());
        }
        // 请求灰度标记
        if (null != RequestContextHolder.getRequestAttributes() &&
                !StringUtils.isEmpty(((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest().getHeader(GlobalConstant.GRAY_REQUEST_HEADER_KEY))) {
            String grayMark = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest().getHeader(GlobalConstant.GRAY_REQUEST_HEADER_KEY);
            template.header(GlobalConstant.GRAY_REQUEST_HEADER_KEY, grayMark);
        }
        if (!template.headers().containsKey(GlobalConstant.GRAY_REQUEST_HEADER_KEY) && null != GrayMarkContextHolder.get()) {
            template.header(GlobalConstant.GRAY_REQUEST_HEADER_KEY, GrayMarkContextHolder.get());
        }
        if (!template.headers().containsKey(GlobalConstant.GRAY_REQUEST_HEADER_KEY)) {
            template.header(GlobalConstant.GRAY_REQUEST_HEADER_KEY, "default");
        }
    }
}
