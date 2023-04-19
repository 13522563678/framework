package com.kcwl.framework.cache.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kcwl.framework.cache.ICacheService;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;

import java.util.concurrent.TimeUnit;

/**
 * @author ckwl
 */
public abstract class AbstractRedisService<T> implements ICacheService {

    protected RedisTemplate<String, Object> redisTemplate;

    protected AbstractRedisService(RedisConnectionFactory factory){
        redisTemplate = createDefaultRedisTemplate(factory);
    }

    protected RedisTemplate<String, Object> createDefaultRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template =  new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        setValueSerializer(template);
        template.afterPropertiesSet();
        return template;
    }

    private void setValueSerializer(RedisTemplate<String, Object> template) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void save(String key, Object obj) {
        redisTemplate.opsForValue().set(key, obj);
    }

    @Override
    public void save(String key, Object obj, int timeout) {
        redisTemplate.opsForValue().set(key, obj, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void expire(String key, int timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void increment(String key) {
        redisTemplate.opsForValue().increment(key);
    }

    @Override
    public void decrement(String key) {
        redisTemplate.opsForValue().decrement(key);
    }

    @Override
    public int incrementAndGet(String key, int timeout){
        RedisAtomicInteger counter = new RedisAtomicInteger(key, redisTemplate.getConnectionFactory());
        counter.expire(timeout, TimeUnit.SECONDS);
        return counter.incrementAndGet();
    }
}
