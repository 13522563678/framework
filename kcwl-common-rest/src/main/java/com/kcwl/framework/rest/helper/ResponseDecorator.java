package com.kcwl.framework.rest.helper;

import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.api.IErrorPromptDecorator;
import com.kcwl.ddd.infrastructure.constants.GlobalConstant;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import com.kcwl.framework.rest.web.CommonWebProperties;
import com.kcwl.framework.utils.StringPaddingBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author ckwl
 */
@Component
public class ResponseDecorator {

    private static ResponseDecorator decorator;

    private IErrorPromptDecorator promptDecorator;

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

    public String getErrorPromptMessage(String code, String defaultMessage) {
        String promptMenage = null;
        if ( promptDecorator != null) {
            promptMenage = promptDecorator.getErrorPrompt(code, getProductCode());
        }
        return !StringUtils.isEmpty(promptMenage) ? promptMenage : defaultMessage;
    }

    private String getProductCode() {
        String product = null;
        UserAgent userAgent = SessionContext.getRequestUserAgent();
        if ( userAgent != null ) {
            product = userAgent.getProduct();
        }
        return (product != null) ? product : "00";
    }

    @Autowired(required = false)
    public void setPromptDecorator(IErrorPromptDecorator errorPromptDecorator) {
        this.promptDecorator = errorPromptDecorator;
    }

    @PostConstruct
    private void init() {
        ResponseDecorator.decorator = this;
    }
}
