package com.kcwl.framework.rest;

import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.api.ResponseMessage;
import com.kcwl.ddd.infrastructure.exception.BaseException;
import com.kcwl.framework.utils.JsonUtil;
import com.kcwl.framework.utils.ServiceHttpStatus;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;


/**
 * @author ckwl
 */
@Slf4j
@Configuration
public class FeignClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        Exception exception = null;
        String responseBody = null;
        try {
            int httpStatus = response.status();
            responseBody = Util.toString(response.body().asReader());

            if ( (HttpStatus.OK.value() == httpStatus) || (ServiceHttpStatus.SERVICE_EXCEPTION_STATUS == httpStatus) ) {
                ResponseMessage responseMessage = JsonUtil.fromJson(responseBody, ResponseMessage.class);
                BaseException baseException = new BaseException(responseMessage.getCode(), responseMessage.getMessage());
                baseException.setResult(responseMessage.getResult());
                exception = baseException;
            } else {
                log.error("errorCode={}, httpStatus={}, errorMessage={}", CommonCode.PARAM_VALID_ERROR_CODE.getCode(), httpStatus, responseBody);
                exception = new BaseException(CommonCode.FAIL.getCode(), CommonCode.FAIL.getDescription());
            }
        } catch ( Exception e ) {
            log.error("responseBody={}, exp={}", responseBody, e.getMessage());
            exception = e;
        }
        return exception;
    }
}
