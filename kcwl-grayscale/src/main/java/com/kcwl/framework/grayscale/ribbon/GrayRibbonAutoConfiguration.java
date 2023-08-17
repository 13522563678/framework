package com.kcwl.framework.grayscale.ribbon;

import com.grayscale.GrayRibbonClientConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 【KC 全链路灰度发布】
 * 集群内 流量治理 入口配置类
 * </p>
 *   2023/8/15 灰度模块 测试项
 *      1. √ 不同LB之间是否会 交差
 *      2. √ apollo config 变更 serverList后，ServerListImpl 是否可以获取到，若成功获取到 能不能 热更新成功，参与下一次request的 lb
 *      3. √ 配置项 kcwl.grayscale.enabled 配置项 是否有效割裂开 容器 / 非容器 环境
 *      4. √ lb # chooseServer 方法的 入参key，是否有必要 再显示传入 serviceId
 *      5. × 在网关流量染色模块，是否可以按照官方建议 直接加入指定 key-value，这样，在 Lb # chooseServer 方法的 入参key，可以直接获取到 灰度流量标记
 *      6. √ Ribbon # PollingServerListUpdater 的默认更新serverList 的时间间隔 如何通过外部配置更改
 *      7. √ grayServers 配置项 热更新 问题
 *
 * @author renyp
 * @since 2021/8/27 17:26
 */
@Configuration
@EnableConfigurationProperties
@GrayRibbonClientEnabled
@AutoConfigureAfter({RibbonAutoConfiguration.class})
@RibbonClients(defaultConfiguration = GrayRibbonClientConfiguration.class)
public class GrayRibbonAutoConfiguration {


}
