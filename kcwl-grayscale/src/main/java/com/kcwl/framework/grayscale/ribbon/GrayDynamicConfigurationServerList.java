package com.kcwl.framework.grayscale.ribbon;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.EnableSpringUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ConfigurationBasedServerList;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.ribbon.RibbonUtils;

import java.util.List;

/**
 * <p>
 * 基于config的 动态 ServerList
 * </p>
 *
 * @author renyp
 * @since 2023/8/14 21:11
 */

@Slf4j
@EnableSpringUtil
public class GrayDynamicConfigurationServerList extends ConfigurationBasedServerList {

    private IClientConfig clientConfig;

    public GrayDynamicConfigurationServerList() {
    }

    @Override
    public List<Server> getUpdatedListOfServers() {
        String ribbonServerListKey = RibbonUtils.getRibbonKey(clientConfig.getClientName(), CommonClientConfigKey.ListOfServers.key());
        String serverListValue = SpringUtil.getProperty(ribbonServerListKey);
        if (StrUtil.isNotBlank(serverListValue)) {
            log.debug("{} refresh client {} serverList {}", GrayDynamicConfigurationServerList.class.getSimpleName(), clientConfig.getClientName(), serverListValue);
            return super.derive(serverListValue);
        }
        log.warn("GrayDynamicServerList based on apollo config couldn't receive server list and therefore perform as super class's behavior");
        return super.getUpdatedListOfServers();
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        super.initWithNiwsConfig(clientConfig);
        this.clientConfig = clientConfig;
    }


}
