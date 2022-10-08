package com.kcwl.framework.rest.web.interceptor;

import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.framework.rest.helper.KcServiceProxy;
import com.kcwl.framework.rest.helper.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ApiMockInterceptor extends HandlerInterceptorAdapter {

    String apiMockHost;

    KcServiceProxy kcServiceProxy;

    public ApiMockInterceptor(String apiMockHost, KcServiceProxy kcServiceProxy) {
        this.apiMockHost = apiMockHost;
        this.kcServiceProxy = kcServiceProxy;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ( log.isDebugEnabled() ) {
            log.debug("mock：{}/{}", apiMockHost, request.getRequestURI());
        }
        try {
            kcServiceProxy.forward(apiMockHost, request, response);
        }catch (Exception e) {
            ResponseHelper.buildResponseBody(CommonCode.API_MOCK_FAIL.getCode(), e.getMessage(), response);
        }
        return false;
    }
}