package com.kcwl.framework.rest.web;

import cn.hutool.cache.CacheUtil;
import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.kcwl.ddd.infrastructure.constants.EmptyObject;
import com.kcwl.framework.rest.helper.KcServiceProxy;
import com.kcwl.framework.rest.helper.SessionCacheProxy;
import com.kcwl.framework.rest.web.interceptor.*;
import com.kcwl.framework.rest.web.interceptor.impl.ApiMockRepository;
import com.kcwl.framework.rest.web.interceptor.impl.ReplayProtectService;
import com.kcwl.framework.utils.ClassUtil;
import com.kcwl.framework.utils.CollectionUtil;
import com.kcwl.framework.utils.JsonUtil;
import com.kcwl.framework.utils.StringUtil;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author 姚华成
 * @date 2017-12-12
 */
public class CommonWebConfig implements WebMvcConfigurer {
    private static final String ALL_API_PATH_PATTERN = "/**";

    private static final String NULL = "null";
    private final boolean swaggerJsonPresent =
            ClassUtil.isPresent("springfox.documentation.spring.web.json.Json",
                    getClass().getClassLoader());
    @Resource
    private CommonWebProperties webProperties;
    @Resource
    SessionCacheProxy sessionCacheProxy;
    @Resource
    KcServiceProxy kcServiceProxy;
    @Resource
    ApiMockRepository apiMockRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //session拦截器
        CommonWebProperties.ApiAuthConfig sessionConfig = webProperties.getSession();
        addApiAuthInterceptor(sessionConfig, registry, new UserSessionInterceptor(sessionCacheProxy, sessionConfig.getIgnorePathPatterns(), sessionConfig.isIgnoreSession()));

        //user接口接口
        addApiAuthInterceptor(webProperties.getApi(), registry, new UserApiRequestInterceptor(webProperties.getSso().getSupportProducts()));

        //inner接口
        CommonWebProperties.ApiAuthConfig innerApiConfig = webProperties.getInner();
        addApiAuthInterceptor(innerApiConfig, registry, new InnerApiRequestInterceptor(innerApiConfig.getAppSecret()));

        // MDC 拦截器
        registry.addInterceptor(new MDCInterceptor()).addPathPatterns(ALL_API_PATH_PATTERN);

        //mock接口拦截处理
        CommonWebProperties.ApiAuthConfig mockConfig = webProperties.getMock();
        addApiMockInterceptor(mockConfig, registry, new ApiMockInterceptor(apiMockRepository, kcServiceProxy));
    }

    private void addApiAuthInterceptor(CommonWebProperties.ApiAuthConfig apiAuthConfig, InterceptorRegistry registry, HandlerInterceptorAdapter interceptor) {

        List<String> pathPatterns = apiAuthConfig.getPathPatterns();

        if ( apiAuthConfig.isEnabled() &&  pathPatterns.size() > 0 ) {
            InterceptorRegistration registration = registry.addInterceptor(interceptor);
            registration.addPathPatterns(pathPatterns.toArray(new String[0]));
            List<String> excludePathPatterns = apiAuthConfig.getExcludePathPatterns();
            if ( !CollectionUtil.isEmpty(excludePathPatterns) ) {
                registration.excludePathPatterns(excludePathPatterns.toArray(new String[0]));
            }
        }
    }

    private void addApiMockInterceptor(CommonWebProperties.ApiAuthConfig apiAuthConfig, InterceptorRegistry registry, HandlerInterceptorAdapter interceptor) {
        if ( apiAuthConfig.isEnabled() ) {
            InterceptorRegistration registration = registry.addInterceptor(interceptor);
            registration.addPathPatterns(ALL_API_PATH_PATTERN);
        }
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // swagger比较特殊，需要使用用专用的序列化类
        // 将MappingJackson2HttpMessageConverter和默认的GsonHttpMessageConverter都删除
        // 创建自定义的GsonHttpMessageConverter
        if ( !webProperties.getJson().isCustomHttpMessageConverter() ) {
            return;
        }

        converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter
                || converter instanceof GsonHttpMessageConverter);
        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();

        GsonBuilder builder = new GsonBuilder();

        if ( webProperties.getJson().isEscapeHtmlChars() ) {
            builder.disableHtmlEscaping();
        }

        if (webProperties.getJson().isSerializeNulls()) {
            builder.serializeNulls();
        }
        String format = webProperties.getJson().getDateFormat();
        if (StringUtil.hasText(format)) {
            builder.setDateFormat(format);
        }
        if (swaggerJsonPresent) {
            builder.registerTypeAdapter(Json.class, (JsonSerializer<Json>) (json, type, context) -> {
                if (json == null) {
                    return null;
                }
                return new JsonParser().parse(json.value());
            });
        }
        builder.registerTypeAdapter(int.class, new IntegerDefault0Adapter());
        builder.registerTypeAdapter(Integer.class, new IntegerDefault0Adapter());
        builder.registerTypeAdapter(long.class, new LongDefault0Adapter());
        builder.registerTypeAdapter(Long.class, new LongDefault0Adapter());
        builder.registerTypeAdapter(double.class, new DoubleDefault0Adapter());
        builder.registerTypeAdapter(Double.class, new DoubleDefault0Adapter());
        builder.registerTypeAdapter(HashMap.class, new HashMapDefault0Adapter());
        builder.registerTypeAdapter(LinkedHashMap.class, new LinkedHashMapDefault0Adapter());
        builder.registerTypeAdapter(LinkedTreeMap.class, new LinkedTreeMapDefault0Adapter());
        if (webProperties.getJson().isStringNulls() ){
            builder.registerTypeAdapter(String.class, new NullStringDefault0Adapter());
        }

        gsonConverter.setGson(builder.create());
        converters.add(gsonConverter);
    }

    private ReplayProtectService createReplayProtectService( CommonWebProperties.ApiAuthConfig apiAuthConfig) {
        return new ReplayProtectService(CacheUtil.newTimedCache(apiAuthConfig.getProtectInterval()), apiAuthConfig.getRequestTimeout());
    }

    public static class IntegerDefault0Adapter implements JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String data = json.getAsString();
            if ("".equals(data) || NULL.equals(data)) {
                //return 0;
                return null;
            }
            return json.getAsInt();
        }
    }

    public static class LongDefault0Adapter implements JsonDeserializer<Long> {
        @Override
        public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String data = json.getAsString();
            if ("".equals(data) || NULL.equals(data)) {
                //return 0L;
                return null;
            }
            return json.getAsLong();
        }
    }

    public static class DoubleDefault0Adapter implements JsonDeserializer<Double> {
        @Override
        public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String data = json.getAsString();
            if ("".equals(data) || NULL.equals(data)) {
                //return 0d;
                return null;
            }
            return json.getAsDouble();
        }
    }

    public static class HashMapDefault0Adapter implements JsonDeserializer<HashMap> {
        @Override
        public HashMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            HashMap<String, Object> resultMap = new HashMap<>();
            JsonUtil.copyToMap(json.getAsJsonObject(), resultMap);
            return resultMap;
        }
    }

    public static class LinkedHashMapDefault0Adapter implements JsonDeserializer<LinkedHashMap> {
        @Override
        public LinkedHashMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
            JsonUtil.copyToMap(json.getAsJsonObject(), resultMap);
            return resultMap;
        }
    }

    public static class LinkedTreeMapDefault0Adapter implements JsonDeserializer<LinkedTreeMap> {
        @Override
        public LinkedTreeMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            LinkedTreeMap<String, Object> resultMap = new LinkedTreeMap<>();
            JsonUtil.copyToMap(json.getAsJsonObject(), resultMap);
            return resultMap;
        }
    }

    public static class NullStringDefault0Adapter extends TypeAdapter<String> {
        @Override
        public void write(JsonWriter jsonWriter, String s) throws IOException {
            if ( s == null ) {
                jsonWriter.value(EmptyObject.STRING);
            } else {
                jsonWriter.value(s);
            }
        }
        @Override
        public String read(JsonReader jsonReader) throws IOException {
            String val = null;
            if ( jsonReader.peek() == JsonToken.NULL ) {
                jsonReader.nextNull();
            } else {
                val = jsonReader.nextString();
            }
            return val;
        }
    }

}

