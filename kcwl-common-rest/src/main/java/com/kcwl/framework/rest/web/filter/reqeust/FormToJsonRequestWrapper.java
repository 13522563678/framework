package com.kcwl.framework.rest.web.filter.reqeust;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IteratorEnumeration;
import cn.hutool.core.map.MapUtil;
import com.kcwl.framework.utils.JsonUtil;
import com.kcwl.framework.utils.MapParamUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author ckwl
 */
public class FormToJsonRequestWrapper extends DecryptRequestWrapper {

    private Map<String ,Object> originalParam;

    public FormToJsonRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public FormToJsonRequestWrapper(HttpServletRequest request , Map<String ,Object> encryptParam, Map<String, String[]> originalParam) {
        super(request, MapParamUtil.convertToMultiValueMap(encryptParam));
        this.originalParam = encryptParam;
        appendPlainParam(originalParam);
    }

    @Override
    public String getContentType() {
        return MediaType.APPLICATION_JSON_VALUE;
    }

    @Override
    public String getHeader(String name) {
        if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(name)) {
            return MediaType.APPLICATION_JSON_VALUE;
        }
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(name)) {
            List<String> list = CollUtil.list(true, super.getHeaders(name));
            list.set(0, MediaType.APPLICATION_JSON_VALUE);
            return new IteratorEnumeration<>(list.iterator());
        }
        return super.getHeaders(name);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new CustomServletInputStream(JsonUtil.toJson(originalParam).getBytes(StandardCharsets.UTF_8));
    }
}
