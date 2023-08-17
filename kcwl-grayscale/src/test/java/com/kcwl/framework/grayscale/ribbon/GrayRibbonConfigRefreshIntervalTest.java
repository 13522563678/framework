package com.kcwl.framework.grayscale.ribbon;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.IClientConfig;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.archaius.ArchaiusAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>
 * 配置项 kcwl.grayflow.enabled 开启，是否按预期注入 自定义Ribbon Client 套件
 * </p>
 *
 * @author renyp
 * @since 2023/8/15 20:53
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {GrayRibbonConfigRefreshIntervalTest.TestFoo3Configuration.class},
        properties = {"kcwl.grayflow.enabled=true"})
@EnableSpringUtil
@ActiveProfiles("foo3")
public class GrayRibbonConfigRefreshIntervalTest {

    @Autowired
    private SpringClientFactory factory;

    /**
     * Ribbon # {@link com.netflix.loadbalancer.PollingServerListUpdater} 默认 刷新serverList 的时间间隔 通过配置项修改后，是否按预期生效
     */
    @Test
    public void configDefaultRefreshServerListTimeInterval() {

        IClientConfig configFoo3 = factory.getInstance("foo3", IClientConfig.class);
        Assertions.assertEquals(1000, configFoo3.get(CommonClientConfigKey.ServerListRefreshInterval, 30000));

    }


    @Configuration
    @RibbonClient("foo3")
    @Import({PropertyPlaceholderAutoConfiguration.class, ArchaiusAutoConfiguration.class,
            RibbonAutoConfiguration.class, GrayRibbonAutoConfiguration.class})
    protected static class TestFoo3Configuration {
    }


}