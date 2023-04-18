package com.kcwl.framework.rest.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.kcwl.ddd.infrastructure.constants.EmptyObject;

import java.io.IOException;

/**
 * @author ckwl
 */
public class NullStringJsonSerializer extends JsonSerializer<Object> {
    /**
     * @param o 字段
     * @param jsonGenerator  json生成器
     * @param serializerProvider 序列化
     * @throws IOException 异常类型
     */
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
        if (o == null) {
            jsonGenerator.writeString(EmptyObject.STRING);
        }
    }
}
