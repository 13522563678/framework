package com.kcwl.framework.rest.helper;

import cn.hutool.core.util.StrUtil;
import com.kcwl.ddd.application.constants.ProductEnum;
import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.api.IErrorPromptDecorator;
import com.kcwl.ddd.infrastructure.constants.GlobalConstant;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import com.kcwl.framework.rest.web.CommonErrorProperties;
import com.kcwl.framework.rest.web.CommonWebProperties;
import com.kcwl.framework.utils.StringPaddingBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

/**
 * @author ckwl
 */
@Slf4j
@Component
public class ResponseDecorator {

    private static ResponseDecorator decorator;

    private IErrorPromptDecorator promptDecorator;

    public static ResponseDecorator getDecorator() {
        return decorator;
    }

    @Resource
    CommonWebProperties commonWebProperties;
    @Resource
    CommonErrorProperties commonErrorProperties;

    @Value("${kcwl.common.web.error.prompt.enable:false}")
    private Boolean hotUpdateEnable;

    public String paddingResponseCode(String code) {
        if (code.length() <= GlobalConstant.BIZ_ERROR_CODE_LENGHT) {
            StringPaddingBuilder spb = new StringPaddingBuilder();
            spb.appendByLeftZero(commonWebProperties.getService().getType(), 2);
            spb.appendByLeftZero(getProductCode(), 2);
            spb.appendByLeftZero(code, GlobalConstant.BIZ_ERROR_CODE_LENGHT);
            return spb.toString();
        }
        return code;
    }

    public String getErrorPromptMessage(String code, String defaultMessage) {
        return commonErrorProperties.decoratorMessageWithTraceId(code, getHotPromptMessage(code, defaultMessage));
    }

    private String getHotPromptMessage(String code, String defaultMessage) {
        if (!hotUpdateEnable) {
            return defaultMessage;
        }
        String productType = getProductCode();
        // 货主Web和货主app 不区分，统一使用货主app的编码
        // 2023/8/3 又区分了...
        //        if (Objects.toString(ProductEnum.SHIPPER_WEB.getId()).equals(productType)) {
        //            productType = ProductEnum.SHIPPER_APP.getId().toString();
        //        }
        try {
            if (!StrUtil.EMPTY.equals(productType)) {
                String finalProductType = productType;
                return Optional.ofNullable(promptDecorator)
                    .map(errorPromptDecorator -> errorPromptDecorator.getErrorPrompt(code, finalProductType))
                    .filter(StrUtil::isNotBlank)
                    .orElse(defaultMessage);
            } else {
                log.warn("处理错误码提示语，未获取到product，UserAgent: {}", SessionContext.getRequestUserAgent());
                return defaultMessage;
            }
        } catch (Exception exception) {
            log.error("获取错误码提示语异常，入参, code:{}, message：{}, product: {}", code, defaultMessage, productType, exception);
            return defaultMessage;
        }
    }

    private String getProductCode() {
        String product = null;
        UserAgent userAgent = SessionContext.getRequestUserAgent();
        if (userAgent != null) {
            product = userAgent.getProduct();
        }
        return (product != null) ? product : StrUtil.EMPTY;
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
