package com.kcwl.framework.rest.helper;


import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.exception.BaseException;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import com.kcwl.framework.rest.service.BaseService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ckwl
 */
@Component
public class ServiceFatrory {

    @Resource
    private ApplicationContext applicationContext;

    public BaseService getService(String serviceType) {
        Object bean = null;
        Integer curApiVersion = SessionContext.getApiVersion();
        if ( (curApiVersion != null) && (curApiVersion>1) ) {
            bean = getServiceByLatestVersion(serviceType, curApiVersion);
        } else {
            bean = applicationContext.getBean(serviceType);
        }

        if ( (bean == null) || !(bean instanceof BaseService) ){
            throw  new BaseException(CommonCode.API_VERSION_NOT_SUPPORT.getCode(), CommonCode.API_VERSION_NOT_SUPPORT.getDescription());
        }
        return (BaseService)bean;
    }

    private Object getServiceByLatestVersion(String serviceType, int curApiVersion) {
        Object bean = null;
        for ( int apiVersion = curApiVersion; apiVersion > 0; apiVersion-- ) {
            bean = applicationContext.getBean(getServiceName(serviceType, apiVersion));
            if ( bean != null ) {
                break;
            }
        }
        return bean;
    }
    private String getServiceName(String serviceType, int apiVersion) {
        return serviceType + "_" + apiVersion;
    }
}
