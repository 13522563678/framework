package com.kcwl.framework.grayscale.ribbon;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.kcwl.framework.grayscale.constant.GrayConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.stereotype.Component;

/**
 * @author: renyp
 * @description: 自定义lbclient，在choose server的时候，将当前服务的service name传入
 * @date: created in 17:14 2021/8/27
 * @modify by:
 */

@Slf4j
@Component
@ConditionalOnProperty(value = "kcwl.grayflow.enabled", matchIfMissing = false)
public class KcwlRibbonLoadBalancerClient extends RibbonLoadBalancerClient {

    private SpringClientFactory springClientFactory;

    public KcwlRibbonLoadBalancerClient(SpringClientFactory clientFactory) {
        super(clientFactory);
        this.springClientFactory = clientFactory;
    }

    @Override
    public ServiceInstance choose(String serviceId) {
        return this.choose(serviceId, serviceId);
    }

    @ApolloConfigChangeListener({"common", "gray-service"})
    public void refreshGrayParameters(ConfigChangeEvent changeEvent) {
        for (String changedKey : changeEvent.changedKeys()) {
            log.info("配置修改：{} -- {} -- {}", changedKey, changeEvent.getChange(changedKey).getOldValue(), changeEvent.getChange(changedKey).getNewValue());
            if (changedKey.startsWith(GrayConstant.GRAY_RULE_PROPERTIES_PREFIX) || changedKey.contains(GrayConstant.RIBBON_LISTOFSERVERS)) {
                springClientFactory.destroy();
                break;
            }
        }
    }
}
