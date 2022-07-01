package com.kcwl.framework.rest.helper;

import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.constants.GlobalConstant;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import com.kcwl.framework.rest.web.CommonWebProperties;
import com.kcwl.framework.utils.StringPaddingBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class ResponseDecorator {

    private static ResponseDecorator decorator;

    public static ResponseDecorator getDecorator() {
        return decorator;
    }

    @Resource
    CommonWebProperties commonWebProperties;
    public String paddingResponseCode(String code) {
        if ( code.length() <= GlobalConstant.BIZ_ERROR_CODE_LENGHT ) {
            StringPaddingBuilder spb = new StringPaddingBuilder();
            spb.appendByLeftZero(commonWebProperties.getService().getType(),2);
            spb.appendByLeftZero(getProductCode(),2);
            spb.appendByLeftZero(code, GlobalConstant.BIZ_ERROR_CODE_LENGHT);
            return spb.toString();
        }
        return code;
    }

    private String getProductCode() {
        String product = null;
        UserAgent userAgent = SessionContext.getRequestUserAgent();
        if ( userAgent != null ) {
            product = userAgent.getProduct();
        }
        return (product != null) ? product : "00";
    }

    @PostConstruct
    private void init() {
        ResponseDecorator.decorator = this;
    }
}
