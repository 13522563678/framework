package com.kcwl.framework.rest.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.kcwl.framework.rest.jackson.serializer.NullArrayJsonSerializer;
import com.kcwl.framework.rest.jackson.serializer.NullJsonSerializer;
import com.kcwl.framework.rest.jackson.serializer.NullObjectJsonSerializer;
import com.kcwl.framework.rest.jackson.serializer.NullStringJsonSerializer;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author ckwl
 */
public class NullFieldBeanSerializerModifier extends BeanSerializerModifier {

    EnumMap<FieldTypeEnum, JsonSerializer<Object>> nullJsonSerializerMap;

    public NullFieldBeanSerializerModifier() {
        this.initNullJsonSerializerMap();
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
        List<BeanPropertyWriter> beanProperties) {
        beanProperties.forEach(writer ->{
            FieldTypeEnum beanType = getBeanType(writer);
            if ( beanType != null ) {
                JsonSerializer<Object> nullJsonSerializer = nullJsonSerializerMap.get(beanType);
                if (nullJsonSerializer != null) {
                    writer.assignNullSerializer(nullJsonSerializer);
                }
            }
        });
        return super.changeProperties(config, beanDesc, beanProperties);
    }

    private FieldTypeEnum getBeanType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        if ( clazz.equals(String.class) ) {
            return FieldTypeEnum.STRING;
        }
        if ( clazz.isPrimitive() ) {
            return FieldTypeEnum.PRIMITIVE;
        }
        if ( clazz.isArray() || clazz.equals(List.class) || clazz.equals(Set.class) ) {
            return FieldTypeEnum.ARRAY;
        }
        if ( clazz.isAssignableFrom(Object.class) ) {
            return FieldTypeEnum.OBJECT;
        }
        return null;
    }

    //@PostConstruct
    private void initNullJsonSerializerMap(){
        nullJsonSerializerMap = new EnumMap<>(FieldTypeEnum.class);
        nullJsonSerializerMap.put(FieldTypeEnum.STRING, new NullStringJsonSerializer());
        nullJsonSerializerMap.put(FieldTypeEnum.PRIMITIVE, new NullJsonSerializer());
        nullJsonSerializerMap.put(FieldTypeEnum.ARRAY, new NullArrayJsonSerializer());
        nullJsonSerializerMap.put(FieldTypeEnum.OBJECT, new NullObjectJsonSerializer());
    }
}
