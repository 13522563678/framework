package com.kcwl.framework.rest.web.interceptor.impl;

import com.kcwl.framework.rest.web.CommonWebProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ApiMockRepository {

    @Resource
    private CommonWebProperties webProperties;


    public String getMockUrl() {
        return webProperties.getMockUrl();
    }

    public boolean isMockApi(String apiPath) {
        if ( webProperties.getMock().isEnabled() ) {
            List<String> mockApiList = webProperties.getMock().getPathPatterns();
            if (mockApiList != null) {
                return mockApiList.contains(apiPath);
            }
        }
        return false;
    }
}
