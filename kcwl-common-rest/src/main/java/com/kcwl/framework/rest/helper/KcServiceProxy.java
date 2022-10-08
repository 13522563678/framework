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
import java.util.StringJoiner;

@Slf4j
@Component
public class KcServiceProxy {

    private static final int MODE_LOAD_BALANCED = 1;
    private static final int MODE_SINGLE_HOST = 2;

    @Resource
    RestTemplate feignRestTemplate;

    @Resource
    RestTemplate restTemplate;


    @SneakyThrows
    public void forward(String serviceName, HttpServletRequest request, HttpServletResponse response) {
        String queryString = request.getQueryString();
        int restMode = choiceRestMode(serviceName);
        String apiUrl = getServiceApiUrl(serviceName, getPublishUri(request), queryString, restMode);
        Object params = null;
        String contentType = request.getContentType();
        if ( log.isDebugEnabled() ) {
            log.debug("contentType={}", contentType);
        }
        if ( contentType == null ) {
            contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE;
        }
        params = getRequestBody(contentType, request);

        if ( log.isDebugEnabled() ) {
            log.debug("apiUrl={}; queryString={}", apiUrl, queryString);
            log.debug("contentType={}; requestBody={}", contentType, params);
        }
        String resp = invoke(apiUrl, queryString, params, contentType, HttpMethod.resolve(request.getMethod()), restMode);

        reply(response, resp);
    }

   public String invoke(String apiUrl, String queryString, Object params, String contentType, HttpMethod httpMethod, int restMode) {
        HttpEntity<Object> requestEntity = null; //new HttpEntity<Object>(params, createHeaders(header));
        if ( params instanceof  Map ) {
            requestEntity = createFormRequestEntity((Map)params, contentType);
        } else {
            requestEntity = new HttpEntity<Object>(params, createHeaders(contentType));
        }

        ResponseEntity<String> response;

        if ( MODE_LOAD_BALANCED == restMode ) {
            response = feignRestTemplate.exchange(apiUrl, httpMethod, requestEntity, String.class);
        } else {
            response = restTemplate.exchange(apiUrl, httpMethod, requestEntity, String.class);
        }
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

    private String getServiceApiUrl(String serviceName, String apiPath, String queryString, int restMode) throws UnsupportedEncodingException {
        String originalQueryString = decodeQueryString(queryString);
        StringBuffer sbApiUrl = new StringBuffer();
        if ( MODE_LOAD_BALANCED == restMode ) {
            sbApiUrl.append("http://").append(serviceName);
        }
        sbApiUrl.append(serviceName).append("/").append(apiPath);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(sbApiUrl.toString()).query(originalQueryString);
        UriComponents uriComponents = uriBuilder.build();
        return  uriComponents.toUriString();
    }

    private HttpEntity<MultiValueMap<String, Object>> createFormRequestEntity(Map<String, String> paramsMap, String contentType) {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<String, Object>();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            Object val = entry.getValue();
            if ( val instanceof String[] ) {
                multiValueMap.add(entry.getKey(), ((String[])val)[0]);
            } else {
                multiValueMap.add(entry.getKey(), entry.getValue());
            }
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

    private int choiceRestMode(String serviceName) {
        return  serviceName.startsWith("http") ? MODE_SINGLE_HOST : MODE_LOAD_BALANCED;
    }
}
