package com.kcwl.framework.cache.impl;

import com.kcwl.framework.cache.ByteRedisSerializer;
import com.kcwl.framework.cache.ICacheService;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;

/**
 * @author ckwl
 */
public class TokenCacheServiceImpl implements ICacheService {

    RedisTemplate template;

    public TokenCacheServiceImpl(RedisConnectionFactory factory) {
        template = new RedisTemplate();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new ByteRedisSerializer());
        template.afterPropertiesSet();
    }

    @Override
    public Object get(String key) {
        return template.opsForValue().get(key);
    }

    @Override
    public void save(String key, Object obj) {
        template.opsForValue().set(key, obj);
    }

    @Override
    public void save(String key, Object obj, int timeout) {
        template.opsForValue().set(key, obj, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void remove(String key) {
        template.delete(key);
    }

    @Override
    public void expire(String key, int timeout) {
        template.expire(key, timeout, TimeUnit.SECONDS);
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
}
