package com.kcwl.framework.rest;

import com.kcwl.ddd.infrastructure.api.ResponseMessage;
import com.kcwl.ddd.infrastructure.exception.BaseException;
import com.kcwl.framework.utils.JsonUtil;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;


/**
 * @author ckwl
 */
@Configuration
public class FeignClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        Exception exception = null;
        try {
            String json = Util.toString(response.body().asReader());
            ResponseMessage responseMessage = JsonUtil.fromJson(json, ResponseMessage.class);
            exception = new BaseException(responseMessage.getCode(), responseMessage.getMessage());
        } catch ( Exception e ) {
            exception = e;
        }
        return exception;
    }
}
