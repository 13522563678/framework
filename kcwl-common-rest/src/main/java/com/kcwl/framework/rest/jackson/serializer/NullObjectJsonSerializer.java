package com.kcwl.framework.rest.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author ckwl
 */
public class NullObjectJsonSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
        if (o == null) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeEndObject();
        }
    }
}
