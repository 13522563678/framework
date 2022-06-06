package com.kcwl.framework.grayscale.ribbon;

import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;

/**
 * @author: renyp
 * @description: 自定义lb，在choose server的时候，将当前服务的service name传入
 * @date: created in 11:43 2021/8/30
 * @modify by:
 */
public class KcwlZoneAwareLoadBalancer<T extends Server> extends ZoneAwareLoadBalancer{

    private static final DynamicBooleanProperty ENABLED = DynamicPropertyFactory.getInstance().getBooleanProperty("ZoneAwareNIWSDiscoveryLoadBalancer.enabled", true);

    @Override
    public Server chooseServer(Object key) {
        if (ENABLED.get() && this.getLoadBalancerStats().getAvailableZones().size() > 1) {
            return super.chooseServer(key);
        } else {
            String serviceId = this.getLoadBalancerStats().getName();
            return super.chooseServer(serviceId);
        }
    }

}
