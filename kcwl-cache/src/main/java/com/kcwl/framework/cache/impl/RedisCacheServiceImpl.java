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

import java.util.concurrent.TimeUnit;

/**
 * @author ckwl
 */
public class RedisCacheServiceImpl implements ICacheService {

    RedisTemplate<String, Object> redisTemplate;

    public RedisCacheServiceImpl(RedisConnectionFactory factory) {
        redisTemplate = createDefaultRedisTemplate(factory);
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
    }

    @Override
    public void decrement(String key) {
    }

    @Override
    public int incrementAndGet(String key, int timeout){
        return 0;
    }

    private RedisTemplate<String, Object> createDefaultRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate =  new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        setValueSerializer(redisTemplate);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
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
}
