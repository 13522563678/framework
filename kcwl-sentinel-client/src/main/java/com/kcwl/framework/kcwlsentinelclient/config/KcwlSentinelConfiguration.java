package com.kcwl.framework.kcwlsentinelclient.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: renyp
 * @description:
 * @date: created in 14:29 2021/10/29
 * @modify by:
 */

@Data
@ConfigurationProperties(prefix = "kcwl.sentinel.ignore")
public class KcwlSentinelConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 不做sentinel统计资源的url集合
     */
    private List<String> path = new ArrayList<>();

    @ApolloConfigChangeListener("common")
    public void onChange(ConfigChangeEvent changeEvent) {
        if (!changeEvent.changedKeys().stream().collect(Collectors.joining(",")).contains("kcwl.sentinel.ignore.path")) {
            return;
        }
        applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
    }
}
