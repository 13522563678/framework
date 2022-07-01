package com.kcwl.framework.rest.helper;

import com.kcwl.framework.utils.StreamUtil;
import com.kcwl.framework.utils.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Map;

@Slf4j
@Component
public class KcServiceProxy {

    @Resource
    RestTemplate feignRestTemplate;

    @SneakyThrows
    public void forward(String serviceName, HttpServletRequest request, HttpServletResponse response) {
        String queryString = request.getQueryString();
        String apiUrl = getServiceApiUrl(serviceName, getPublishUri(request), queryString);
        //String contentType = request.getContentType();
        String contentType = "application/json;charset=UTF-8";

        Object params = getRequestBody(contentType, request);
        if ( log.isDebugEnabled() ) {
            log.debug("apiUrl={}; queryString={}", apiUrl, queryString);
            log.debug("contentType={}; requestBody={}", contentType, params);
        }
        String resp = invoke(apiUrl, queryString, params, contentType, HttpMethod.resolve(request.getMethod()));

        reply(response, resp);
    }

   public String invoke(String apiUrl, String queryString, Object params, String contentType, HttpMethod httpMethod) {
        HttpEntity<Object> requestEntity = null; //new HttpEntity<Object>(params, createHeaders(header));
        if ( params instanceof  Map ) {
            requestEntity = createFormRequestEntity((Map)params, contentType);
        } else {
            requestEntity = new HttpEntity<Object>(params, createHeaders(contentType));
        }
        ResponseEntity<String> response = feignRestTemplate.exchange(apiUrl, httpMethod, requestEntity, String.class);
        return response.getBody();
    }
    private HttpHeaders createHeaders(String contentType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        /*
        for (Map.Entry<String, String> entry : header.entrySet()) {
            httpHeaders.add(entry.getKey(), entry.getValue());
        }
         */
        httpHeaders.add("Content-Type",contentType);
        return httpHeaders;
    }

    private String getServiceApiUrl(String serviceName, String apiPath, String queryString) throws UnsupportedEncodingException {
        String originalQueryString = decodeQueryString(queryString);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl("http://" + serviceName  + "/" + apiPath).query(originalQueryString);
        UriComponents uriComponents = uriBuilder.build();
        return  uriComponents.toUriString();
    }

    private HttpEntity<MultiValueMap<String, Object>> createFormRequestEntity(Map<String, String> paramsMap, String contentType) {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<String, Object>();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            multiValueMap.add(entry.getKey(), entry.getValue());
        }

        return new HttpEntity<MultiValueMap<String, Object>>(multiValueMap, createHeaders(contentType));
    }

    private Object getRequestBody(String contentType, HttpServletRequest request) throws IOException {
        Object body = null;
        if ( contentType != null && contentType.startsWith("application/json") ) {
            body = StreamUtil.copyToString(request.getInputStream(),  Charset.forName("UTF-8"));
        } else {
            body = request.getParameterMap();
        }
        return body;
    }

    private String getPublishUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        // 格式化一下请求的路径
        uri = uri.replaceAll("//", "/");
        String contextPath = request.getContextPath();
        if (StringUtil.hasText(contextPath)) {
            uri = uri.substring(contextPath.length());
        }
        return uri;
    }


    private void reply(HttpServletResponse response, Object httpResponse)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print(httpResponse);
    }

    private String decodeQueryString(String queryString) throws UnsupportedEncodingException {
        return !StringUtil.isEmpty(queryString) ? URLDecoder.decode(queryString, "utf-8") : queryString;
    }
}
