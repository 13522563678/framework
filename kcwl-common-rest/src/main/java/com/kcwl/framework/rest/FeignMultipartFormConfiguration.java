package com.kcwl.framework.rest;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author: renyp
 * @description: feign 解析请求中的文件流配置
 * @date: created in 16:22 2021/6/1
 * @modify by:
 */

public class FeignMultipartFormConfiguration {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    @Primary
    @Scope("prototype")
    public Encoder multipartFormEncoder() {
        return new FeignMultiAndFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    public feign.Logger.Level multipartLoggerLevel() {
        return feign.Logger.Level.FULL;
    }


    /**
     * @author: renyp
     * @description:  有以下需求时，可在 @FeignClient的configuration属性中 引用此类
     *                  1. 通过feign在请求体中传递 java.util.Map 及其子类型 的参数
     *                  2. 通过feign在请求体中传递 java.util.Map 且 Map 类型参数中 存在value的数据类型是Multipartfile的
     * @date: created in 17:41 2021/6/9
     */
    public class FeignMultiAndFormEncoder extends SpringFormEncoder {

        public FeignMultiAndFormEncoder() {
            this(new Default());
        }

        public FeignMultiAndFormEncoder(Encoder delegate) {
            super(delegate);
        }

        @Override
        public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
            Map data;
            if (bodyType != null && bodyType.toString().contains("java.util.") && bodyType.toString().contains("Map")) {
                data = (Map) object;
                super.encode(data, MAP_STRING_WILDCARD, template);
            } else {
                super.encode(object, bodyType, template);
            }
        }
    }

}
