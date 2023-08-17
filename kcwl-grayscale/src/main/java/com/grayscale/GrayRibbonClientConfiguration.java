package com.grayscale;

import com.kcwl.framework.grayscale.ribbon.GrayDynamicConfigurationServerList;
import com.kcwl.framework.grayscale.ribbon.GrayFlowRoutedRule;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.ServerList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 【KC 全链路灰度发布】
 * 集群内 流量治理 Ribbon负载均衡相关 自定义组件 配置类
 * </p>
 *
 * @author renyp
 * @since 2023/8/14 22:08
 */
@Configuration
public class GrayRibbonClientConfiguration {

    @Autowired
    private PropertiesFactory propertiesFactory;


    @Bean
    public IRule ribbonRule(IClientConfig config) {
        if (this.propertiesFactory.isSet(IRule.class, config.getClientName())) {
            return this.propertiesFactory.get(IRule.class, config, config.getClientName());
        }
        GrayFlowRoutedRule grayFlowRoutedRule = new GrayFlowRoutedRule();
        grayFlowRoutedRule.initWithNiwsConfig(config);
        return grayFlowRoutedRule;
    }

    @Bean
    public ServerList<?> ribbonServerList(IClientConfig config) {
        if (this.propertiesFactory.isSet(ServerList.class, config.getClientName())) {
            return this.propertiesFactory.get(ServerList.class, config, config.getClientName());
        }
        GrayDynamicConfigurationServerList configurationServerList = new GrayDynamicConfigurationServerList();
        configurationServerList.initWithNiwsConfig(config);
        return configurationServerList;
    }


}
