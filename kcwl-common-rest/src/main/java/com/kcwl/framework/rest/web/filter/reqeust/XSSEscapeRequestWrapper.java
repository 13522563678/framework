package com.kcwl.framework.rest.web.filter.reqeust;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.kcwl.framework.utils.HtmlEscapeUtil;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * XSS 过滤 Request Wrapper
 */
public class XSSEscapeRequestWrapper extends HttpServletRequestWrapper {

    public XSSEscapeRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        return HtmlEscapeUtil.escape(super.getParameter(name));
    }

    @Override
    public String[] getParameterValues(String name) {
        return HtmlEscapeUtil.escape(super.getParameterValues(name));
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = super.getParameterMap(); // 防止 super 返回无法修改的 map
        if (CollUtil.isEmpty(map)) {
            return map;
        }

        Map copyMap =new HashMap<>(map);
        HtmlEscapeUtil.escape(copyMap);
        return copyMap;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        String header = getHeader(CONTENT_TYPE);
        if ((APPLICATION_JSON_VALUE.equals(header) || APPLICATION_JSON_UTF8_VALUE.equals(header))
                && inputStreamRepeatable()) {
            String escapedJsonStr = HtmlEscapeUtil.escapeJson(IoUtil.read(super.getInputStream(), StandardCharsets.UTF_8));
            return new CustomServletInputStream(escapedJsonStr.getBytes(StandardCharsets.UTF_8));
        }

        return super.getInputStream();
    }

    // InputStream 能否重复读取
    private boolean inputStreamRepeatable() {
        ServletRequest request = getRequest();
        return request instanceof com.kcwl.framework.rest.web.filter.reqeust.ContentCachingRequestWrapper
                || request instanceof org.springframework.web.util.ContentCachingRequestWrapper
                || request instanceof FormToJsonRequestWrapper;
    }

}
