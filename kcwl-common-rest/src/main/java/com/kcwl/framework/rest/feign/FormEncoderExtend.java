package com.kcwl.framework.rest.feign;

import feign.codec.Encoder;
import feign.form.ContentProcessor;
import feign.form.ContentType;
import feign.form.FormEncoder;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 自定义 feign form encoder，用于支持自定义序列化
 */
public class FormEncoderExtend extends FormEncoder {

    public FormEncoderExtend(Encoder delegate) throws NoSuchFieldException, IllegalAccessException {
        super(delegate);
        Field field = FormEncoder.class.getDeclaredField("processors");
        try {
            field.setAccessible(true);
            Map<ContentType, ContentProcessor> processors = (Map<ContentType, ContentProcessor>) field.get(this);

            // 使用自定义 form-url-encoded processor 覆盖
            UrlencodedFormContentProcessorExtend urlencodedFormContentProcessorExtend = new UrlencodedFormContentProcessorExtend();
            processors.put(urlencodedFormContentProcessorExtend.getSupportedContentType(), urlencodedFormContentProcessorExtend);

        } finally {
            field.setAccessible(false);
        }
    }

}
