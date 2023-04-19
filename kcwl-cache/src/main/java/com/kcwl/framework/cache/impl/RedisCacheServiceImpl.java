package com.kcwl.framework.cache.impl;

import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @author ckwl
 */
@Deprecated
public class RedisCacheServiceImpl extends RedisCacheService {
    public RedisCacheServiceImpl(RedisConnectionFactory factory) {
        super(factory);
    }
}
