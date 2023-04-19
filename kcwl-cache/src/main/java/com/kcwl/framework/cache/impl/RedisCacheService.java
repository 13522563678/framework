package com.kcwl.framework.cache.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kcwl.framework.cache.ICacheService;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;

/**
 * @author ckwl
 */
public class RedisCacheService extends AbstractRedisService {

    public RedisCacheService(RedisConnectionFactory factory) {
        super(factory);
    }

    public ListOperations opsForList(){
        return redisTemplate.opsForList();
    }

    public HashOperations opsForHash(){
        return redisTemplate.opsForHash();
    }

    public SetOperations opsForSet(){
        return redisTemplate.opsForSet();
    }
}
