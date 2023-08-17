package com.kcwl.framework.grayscale.ribbon;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.ServerList;
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
 * 配置项 kcwl.grayflow.enabled 开启，是否按预期注入 自定义Ribbon Client 套件
 * </p>
 *
 * @author renyp
 * @since 2023/8/15 20:53
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {
        EnableGrayRibbonAutoConfigurationTest.TestFoo1Configuration.class,
        EnableGrayRibbonAutoConfigurationTest.TestFoo2Configuration.class},
        properties = {
                "kcwl.grayflow.enabled=true",
                "foo1.ribbon.listOfServers=foo1-1,foo1-2",
                "foo2.ribbon.listOfServers=foo2-1,foo2-2"})
@EnableSpringUtil
public class EnableGrayRibbonAutoConfigurationTest {

    @Autowired
    private SpringClientFactory factory;


    /**
     * ‘kcwl.grayscale.enabled’ 配置项 是否可以有效控制 自定义的RibbonClient套件（LB、IRule、ServerList） 按预期注入
     * {@link GrayRibbonClientEnabled}
     */
    @Test
    public void grayScaleEnabledTest() {

        ServerList<?> serverList = factory.getInstance("foo1", ServerList.class);
        Assertions.assertTrue(serverList instanceof GrayDynamicConfigurationServerList);

        IRule rule = factory.getInstance("foo1", IRule.class);
        Assertions.assertTrue(rule instanceof GrayFlowRoutedRule);

    }

    /**
     * 不同LB之间是否会交差，这个 case 是针对 历史使用方式错误的问题：
     * 将IRule作为Bean注入到Spring ApplicationContext主容器内（非Ribbon定义的SpringClientFactory子容器），Bean的Scope默认又是单例，
     * 导致所有RibbonClient都在共享全局唯一的IRule，此时通过 {@link AbstractLoadBalancerRule#getLoadBalancer()}
     * 获取 LB，ClientA 可能获取到 ClientB 的LB，进而拿到 ClientB 的 serverList
     */
    @Test
    public void multiLoadBalancerLoadTest() {

        IRule ruleFoo1 = factory.getInstance("foo1", IRule.class);
        Assertions.assertEquals(ruleFoo1.getLoadBalancer().getAllServers().size(), 2);
        Assertions.assertEquals(ruleFoo1.getLoadBalancer().getAllServers().get(0).getHost(), "foo1-1");
        Assertions.assertEquals(ruleFoo1.getLoadBalancer().getAllServers().get(1).getHost(), "foo1-2");


        IRule ruleFoo2 = factory.getInstance("foo2", IRule.class);
        Assertions.assertEquals(ruleFoo2.getLoadBalancer().getAllServers().size(), 2);
        Assertions.assertEquals(ruleFoo2.getLoadBalancer().getAllServers().get(0).getHost(), "foo2-1");
        Assertions.assertEquals(ruleFoo2.getLoadBalancer().getAllServers().get(1).getHost(), "foo2-2");

        Assertions.assertEquals(ruleFoo1.getLoadBalancer().getAllServers().size(), 2);
        Assertions.assertEquals(ruleFoo1.getLoadBalancer().getAllServers().get(0).getHost(), "foo1-1");
        Assertions.assertEquals(ruleFoo1.getLoadBalancer().getAllServers().get(1).getHost(), "foo1-2");

    }


    @Configuration
    @RibbonClient("foo1")
    @Import({PropertyPlaceholderAutoConfiguration.class, ArchaiusAutoConfiguration.class,
            RibbonAutoConfiguration.class, GrayRibbonAutoConfiguration.class})
    protected static class TestFoo1Configuration {
    }

    @Configuration
    @RibbonClient("foo2")
    @Import({PropertyPlaceholderAutoConfiguration.class, ArchaiusAutoConfiguration.class,
            RibbonAutoConfiguration.class, GrayRibbonAutoConfiguration.class})
    protected static class TestFoo2Configuration {
    }

}