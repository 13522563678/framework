package com.kcwl.framework.rest;

import cn.hutool.core.collection.CollUtil;
import com.kcwl.ddd.infrastructure.api.ResponseMessage;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ckwl
 */
@Component
public class FeignClientTemplate {

    @Resource
    RestTemplate feignRestTemplate;

    HttpHeaders httpJsonHeaders = new HttpHeaders(){
        {
            add("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE);
            add("Accept",MediaType.APPLICATION_JSON.toString());
        }
    };

    HttpHeaders httpFormHeaders = new HttpHeaders(){
        {
            add("Content-Type",MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            add("Accept",MediaType.APPLICATION_JSON.toString());
        }
    };

    /**
     * @param apiUri
     * @param params
     * @return
     */
    public ResponseMessage httpGet(String apiUri, String params, Type typeOfResult) {
        return invoke(apiUri, params, HttpMethod.GET, typeOfResult);
    }

    /**
     * @param apiUri
     * @param params
     * @return
     */
    public ResponseMessage httpPost(String apiUri, Object params, Type typeOfResult) {
        return invoke(apiUri, params, HttpMethod.POST, typeOfResult);
    }

    public ResponseMessage httpPost(String apiUri, HttpHeaders httpHeaders, Object params, Type typeOfResult) {
        return invoke(apiUri, httpHeaders, params, HttpMethod.POST, typeOfResult);
    }

    public ResponseMessage invoke(String apiUri, HttpHeaders httpHeaders, Object params, HttpMethod httpMethod, Type typeOfT) {
        HttpHeaders headers = new HttpHeaders();
        if (CollUtil.isNotEmpty(httpHeaders)) {
            headers.addAll(httpHeaders);
        }
        headers.addAll(httpJsonHeaders);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(params, headers);
        ResponseEntity<ResponseMessage> response = feignRestTemplate.exchange(apiUri, httpMethod, requestEntity, ResponseMessage.class);
        return response.getBody();
    }


    public ResponseMessage invoke(String apiUri, Object params, HttpMethod httpMethod, Type typeOfT) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(params, httpJsonHeaders);
        ResponseEntity<ResponseMessage> response = feignRestTemplate.exchange(apiUri, httpMethod, requestEntity, ResponseMessage.class);
        return response.getBody();
    }

    /**
     * 创建表单形式的HttpEntity
     * @param paramsMap
     * @return
     */
    private HttpEntity<MultiValueMap<String, Object>> createFormRequestEntity(HashMap<String, String> paramsMap) {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<String, Object>();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            multiValueMap.add(entry.getKey(), entry.getValue());
        }
        return new HttpEntity<MultiValueMap<String, Object>>(multiValueMap, httpFormHeaders);
    }

    private boolean isMapType(Object params) {
        Class clazz = params.getClass();
        return (clazz.equals(HashMap.class) || clazz.equals(LinkedHashMap.class));
    }
}
