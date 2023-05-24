package com.kcwl.framework.rest.helper;

import com.kcwl.framework.utils.RequestUtil;
import com.kcwl.framework.utils.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author ckwl
 */
@Slf4j
@Component
public class KcServiceProxy {

    private static final int MODE_LOAD_BALANCED = 1;
    private static final int MODE_SINGLE_HOST = 2;

    @Resource
    RestTemplate feignRestTemplate;

    @Resource
    RestTemplate restTemplate;

    /**
     * @param serviceName 服务名
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void forward(String serviceName, HttpServletRequest request, HttpServletResponse response) {
        forward(serviceName, getPublishUri(request), request, response);
    }

    /**
     * @param serviceName  服务名
     * @param apiPath 转发的请求路径
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @SneakyThrows
    public void forward(String serviceName, String apiPath, HttpServletRequest request, HttpServletResponse response) {
        String queryString = request.getQueryString();
        int restMode = choiceRestMode(serviceName);
        String apiUrl = getServiceApiUrl(serviceName, apiPath, queryString, restMode);
        Map<String, String> headers = RequestUtil.getHeaderMap(request);
        String contentType = request.getContentType();
        if ( contentType == null ) {
            contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE;
            headers.put(HttpHeaders.CONTENT_TYPE, contentType);
        }
        Object params = getRequestBody(contentType, request);
        if ( log.isDebugEnabled() ) {
            log.debug("apiUrl={}; queryString={}", apiUrl, queryString);
            log.debug("httpMethod={}; contentType={}; requestBody={}", HttpMethod.resolve(request.getMethod()), contentType, params);
        }
        String resp = invoke(apiUrl, params, headers, HttpMethod.resolve(request.getMethod()), restMode);

        reply(response, resp);
    }

   public String invoke(String apiUrl, Object params, Map headers, HttpMethod httpMethod, int restMode) {
        HttpEntity<Object> requestEntity = null;
       ResponseEntity<String> response = null;
        if ( params instanceof  Map ) {
            requestEntity = createFormRequestEntity((Map)params, headers);
        } else {
            requestEntity = new HttpEntity<Object>(params, createHeaders(headers));
        }
        if ( MODE_LOAD_BALANCED == restMode ) {
            response = feignRestTemplate.exchange(apiUrl, httpMethod, requestEntity, String.class);
        } else {
            response = restTemplate.exchange(apiUrl, httpMethod, requestEntity, String.class);
        }
        return response.getBody();
    }
    private HttpHeaders createHeaders(Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpHeaders.add(entry.getKey(), entry.getValue());
        }
        return httpHeaders;
    }

    private String getServiceApiUrl(String serviceName, String apiPath, String queryString, int restMode) throws UnsupportedEncodingException {
        String originalQueryString = decodeQueryString(queryString);
        StringBuilder sbApiUrl = new StringBuilder();
        if ( MODE_LOAD_BALANCED == restMode ) {
            sbApiUrl.append("http://");
        }
        sbApiUrl.append(serviceName).append("/").append(apiPath);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(sbApiUrl.toString()).query(originalQueryString);
        UriComponents uriComponents = uriBuilder.build();
        return  uriComponents.toUriString();
    }

    private HttpEntity<MultiValueMap<String, Object>> createFormRequestEntity(Map<String, String> paramsMap, Map headers) {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<String, Object>();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            Object val = entry.getValue();
            if ( val instanceof String[] ) {
                String[] arrayValues = (String[])val;
                if ( arrayValues.length > 0 ) {
                    multiValueMap.add(entry.getKey(), arrayValues[0]);
                } else {
                    multiValueMap.add(entry.getKey(), arrayValues);
                }
            } else {
                multiValueMap.add(entry.getKey(), entry.getValue());
            }
        }

        return new HttpEntity<MultiValueMap<String, Object>>(multiValueMap, createHeaders(headers));
    }

    private Object getRequestBody(String contentType, HttpServletRequest request) throws IOException {
        Object body = null;
        if ( contentType != null && contentType.startsWith(MediaType.APPLICATION_JSON_VALUE) ) {
            body = StreamUtils.copyToString(request.getInputStream(),  StandardCharsets.UTF_8);
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
