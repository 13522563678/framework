package com.kcwl.framework.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("kcwl.user.token.redis")
public class UserTokenRedisProperties {
    private int database = 0;
    private String host;
    private int port;
    private String password;
}
