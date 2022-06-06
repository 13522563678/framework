package com.kcwl.framework.rest.web.interceptor;

import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.constants.GlobalConstant;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import com.kcwl.framework.grayscale.utils.GrayMarkContextHolder;
import com.kcwl.tenant.TenantDataHolder;
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
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private String appSecretKey;

    public RestTemplateInterceptor(String appSecretKey) {
        this.appSecretKey = appSecretKey;
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
            headers.add(UserAgent.REQUEST_AGENT_HEADER_NAME, requestUserAgent.toString());
        }
        headers.add(UserAgent.REQUEST_AGENT_CLIENT_FIELD_NAME, UserAgent.AGENT_CLIENT_FEIGN);
        headers.add(GlobalConstant.APP_SECRET_FIELD_NAME, this.appSecretKey);

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
}
