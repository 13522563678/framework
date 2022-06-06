package com.kcwl.framework.grayscale.config;

import com.kcwl.framework.grayscale.rule.GrayFlowRoutedRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: renyp
 * @description: ribbon rule（灰度分流规则）配置类
 * @date: created in 17:26 2021/8/27
 * @modify by:
 */

@Configuration
public class KcwlRibbonAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "kcwl.grayflow.enabled", matchIfMissing = false)
    @ConditionalOnBean(SpringClientFactory.class)
    public IRule createIRule() {
        return new GrayFlowRoutedRule();
    }
}
