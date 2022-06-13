package com.kcwl.framework.cache;

import com.kcwl.framework.cache.config.UserTokenRedisProperties;
import com.kcwl.framework.cache.impl.RedisCacheServiceImpl;
import com.kcwl.framework.cache.impl.StringRedisCacheServiceImpl;
import com.kcwl.framework.cache.impl.TokenCacheServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import javax.annotation.Resource;

/**
 * @author ckwl
 */
@Configuration
public class CacheConfiguration {

    @Resource
    UserTokenRedisProperties userTokenRedisProperties;

    @Bean(name="cacheService")
    public ICacheService cacheService(RedisConnectionFactory redisConnectionFactory){
        return new RedisCacheServiceImpl(redisConnectionFactory);
    }
    @Bean(name="stringCache")
    public ICacheService stringCache(RedisConnectionFactory redisConnectionFactory){
        return new StringRedisCacheServiceImpl(redisConnectionFactory);
    }
    @Bean(name="userTokenCache")
    public ICacheService userTokenCache(RedisConnectionFactory redisConnectionFactory){
        RedisConnectionFactory userTokenRedisConnectionFactory = userTokenRedisConnectionFactory();
        if ( userTokenRedisConnectionFactory == null ) {
            userTokenRedisConnectionFactory = redisConnectionFactory;
        }
        return new RedisCacheServiceImpl(userTokenRedisConnectionFactory);
    }

    public RedisConnectionFactory userTokenRedisConnectionFactory(){
        if ( (userTokenRedisProperties != null) && (userTokenRedisProperties.getHost() != null ) ) {
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
            redisStandaloneConfiguration.setHostName(userTokenRedisProperties.getHost());
            redisStandaloneConfiguration.setDatabase(userTokenRedisProperties.getDatabase());
            redisStandaloneConfiguration.setPassword(userTokenRedisProperties.getPassword());
            redisStandaloneConfiguration.setPort(userTokenRedisProperties.getPort());

            LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
            lettuceConnectionFactory.afterPropertiesSet();
            return lettuceConnectionFactory;
        }
        return null;
    }
}
