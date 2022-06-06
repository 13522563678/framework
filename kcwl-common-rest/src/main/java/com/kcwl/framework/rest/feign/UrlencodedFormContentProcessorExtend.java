package com.kcwl.framework.rest.feign;

import cn.hutool.core.date.DateUtil;
import feign.Request;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.form.UrlencodedFormContentProcessor;
import lombok.SneakyThrows;
import lombok.val;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;


/**
 * 覆盖 feign form encoder 中的 form-urlencoded 处理
 */
public class UrlencodedFormContentProcessorExtend extends UrlencodedFormContentProcessor {

    private static final char QUERY_DELIMITER = '&';
    private static final char EQUAL_SIGN = '=';

    @SneakyThrows
    private static String encode(Object string, Charset charset) {
        if (string.getClass() == Date.class) {
            // 迫于 Date.toString() 本地时间格式化以后 还加上 CST 时区
            return URLEncoder.encode(DateUtil.formatHttpDate((Date) string), charset.name());
        } else {
            return URLEncoder.encode(string.toString(), charset.name());
        }
    }

    @Override
    public void process(RequestTemplate template, Charset charset, Map<String, Object> data) throws EncodeException {
        val bodyData = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry == null || entry.getKey() == null) {
                continue;
            }
            if (bodyData.length() > 0) {
                bodyData.append(QUERY_DELIMITER);
            }
            bodyData.append(createKeyValuePair(entry, charset));
        }

        String contentTypeValue = new StringBuilder()
                .append(getSupportedContentType().getHeader())
                .append("; charset=").append(charset.name())
                .toString();

        byte[] bytes = bodyData.toString().getBytes(charset);
        Request.Body body = Request.Body.encoded(bytes, charset);

        template.header(CONTENT_TYPE_HEADER, Collections.<String>emptyList()); // reset header
        template.header(CONTENT_TYPE_HEADER, contentTypeValue);
        template.body(body);
    }

    private String createKeyValuePair(Map.Entry<String, Object> entry, Charset charset) {
        String encodedKey = encode(entry.getKey(), charset);
        Object value = entry.getValue();

        if (value == null) {
            return encodedKey;
        } else if (value.getClass().isArray()) {
            return createKeyValuePairFromArray(encodedKey, value, charset);
        } else if (value instanceof Collection) {
            return createKeyValuePairFromCollection(encodedKey, value, charset);
        }
        return new StringBuilder()
                .append(encodedKey)
                .append(EQUAL_SIGN)
                .append(encode(value, charset))
                .toString();
    }

    private String createKeyValuePairFromCollection(String key, Object values, Charset charset) {
        Collection collection = (Collection) values;
        Object[] array = collection.toArray(new Object[0]);
        return createKeyValuePairFromArray(key, array, charset);
    }

    private String createKeyValuePairFromArray(String key, Object values, Charset charset) {
        StringBuilder result = new StringBuilder();
        Object[] array = (Object[]) values;

        for (int index = 0; index < array.length; index++) {
            Object value = array[index];
            if (value == null) {
                continue;
            }

            if (index > 0) {
                result.append(QUERY_DELIMITER);
            }

            result.append(key).append(EQUAL_SIGN).append(encode(value, charset));
        }
        return result.toString();
    }

}
