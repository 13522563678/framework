package com.kcwl.framework.cache.impl;

import com.kcwl.framework.cache.ICacheService;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.util.concurrent.TimeUnit;

/**
 * @author ckwl
 */
public class StringRedisCacheServiceImpl implements ICacheService {

    RedisTemplate<String, String> redisTemplate;

    public StringRedisCacheServiceImpl(RedisConnectionFactory factory) {
        redisTemplate = new StringRedisTemplate(factory);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void save(String key, Object obj) {
        redisTemplate.opsForValue().set(key, obj.toString());
    }

    @Override
    public void save(String key, Object obj, int timeout) {
        redisTemplate.opsForValue().set(key, obj.toString(), timeout, TimeUnit.SECONDS);
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
