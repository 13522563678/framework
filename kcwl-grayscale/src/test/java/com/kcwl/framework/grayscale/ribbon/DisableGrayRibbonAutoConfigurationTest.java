package com.kcwl.framework.grayscale.ribbon;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.netflix.loadbalancer.ConfigurationBasedServerList;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.ServerList;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>
 * 配置项 kcwl.grayflow.enabled 关闭，是否按预期排除 自定义Ribbon Client 套件
 * </p>
 *
 * @author renyp
 * @since 2023/8/15 20:53
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DisableGrayRibbonAutoConfigurationTest.TestConfiguration.class, properties = {"kcwl.grayflow.enabled=false"})
@EnableSpringUtil
public class DisableGrayRibbonAutoConfigurationTest {

    @Autowired
    private SpringClientFactory factory;

    /**
     * ‘kcwl.grayscale.enabled=false’ 配置项 是否可以有效控制 自定义的RibbonClient套件（LB、IRule、ServerList） 按预期排除
     * {@link GrayRibbonClientEnabled}
     */
    @Test
    public void grayScaleDisabledTest() {

        ServerList<?> server = factory.getInstance("foo", ServerList.class);
        Assertions.assertTrue(server instanceof ConfigurationBasedServerList);

        IRule rule = factory.getInstance("foo", IRule.class);
        Assertions.assertTrue(rule instanceof ZoneAvoidanceRule);

    }


    @Configuration
    @RibbonClient("foo")
    @Import({PropertyPlaceholderAutoConfiguration.class, ArchaiusAutoConfiguration.class,
            RibbonAutoConfiguration.class, GrayRibbonAutoConfiguration.class})
    protected static class TestConfiguration {
    }

}