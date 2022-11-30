package com.kcwl.framework.rest.web.interceptor;

import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.framework.rest.helper.KcServiceProxy;
import com.kcwl.framework.rest.helper.ResponseHelper;
import com.kcwl.framework.rest.web.interceptor.impl.ApiMockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ApiMockInterceptor extends HandlerInterceptorAdapter {

    //String apiMockHost;

    ApiMockRepository apiMockRepository;

    KcServiceProxy kcServiceProxy;

    public ApiMockInterceptor(ApiMockRepository apiMockRepository, KcServiceProxy kcServiceProxy) {
        this.apiMockRepository = apiMockRepository;
        this.kcServiceProxy = kcServiceProxy;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ( log.isDebugEnabled() ) {
            log.debug("mock：{}/{}", apiMockRepository.getMockUrl(), request.getRequestURI());
        }
        if ( apiMockRepository.isMockApi(request.getRequestURI()) ) {
            try {
                kcServiceProxy.forward(apiMockRepository.getMockUrl(), request, response);
            }catch (Exception e) {
                ResponseHelper.buildResponseBody(CommonCode.API_MOCK_FAIL.getCode(), e.getMessage(), response);
            }
            return false;
        }
        return true;
    }
}