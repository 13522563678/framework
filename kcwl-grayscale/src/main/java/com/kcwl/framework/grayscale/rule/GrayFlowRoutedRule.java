package com.kcwl.framework.grayscale.rule;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.kcwl.framework.grayscale.constant.GrayConstant;
import com.kcwl.framework.grayscale.ribbon.KcwlZoneAwareLoadBalancer;
import com.kcwl.framework.grayscale.utils.GrayMarkContextHolder;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: renyp
 * @description: 灰度分流策略1.0版本（只考虑用户请求的单一条件组合权重方式）
 * @date: created in 17:05 2021/8/18
 * @modify by:
 */

@Slf4j
public class GrayFlowRoutedRule extends AbstractLoadBalancerRule {

    private AtomicInteger index = new AtomicInteger(0);
    private AtomicInteger grayIndex = new AtomicInteger(0);
    private AtomicInteger stableIndex = new AtomicInteger(0);

    /**
     * 灰度环境中service的host特征字符（如：canary）
     */
//    @Value("#{GrayConstant.GRAY_RULE_HOSTMARK}")
    @Value("${kcwl.grayflow.rule.grayHostMark:}")
    private String grayHostMark;

    /**
     * 参与灰度测试的服务列表
     */
    @Value("${kcwl.grayflow.rule.grayServersList:}")
    private List<String> grayServers;


    @Override
    public Server choose(Object serviceId) {
        try {
            ILoadBalancer loadBalancer = SpringUtil.getBean(SpringClientFactory.class).getLoadBalancer(serviceId.toString());

            // feign 在调用时，这里不能热更新 灰度标记那几个属性（grayHostMark、grayServers），在这里手动更新
            refreshCurrentFieldValue(loadBalancer);
            if (StringUtils.isEmpty(grayHostMark)) {
                if (CollectionUtil.isEmpty(loadBalancer.getAllServers())) {
                    return null;
                }
                // 若没有灰度标记区分severs，则认为全部服务都属于stable版本，在allServers中轮询
                return loadBalancer.getAllServers().get(this.index.getAndIncrement() % loadBalancer.getAllServers().size());
            }

            Map<Boolean, List<Server>> serverPartition = loadBalancer.getAllServers().stream().collect(Collectors.partitioningBy(server -> server.getHost().contains(grayHostMark)));

            // 根据请求特征 确定是否进入gray环境
            return selectServer(serverPartition.get(false), serverPartition.get(true), serviceId.toString());
        } catch (Exception ex) {
            log.error("", ex);
        } finally {
            if (stableIndex.get() >= Integer.MAX_VALUE) {
                stableIndex = new AtomicInteger(0);
            }
            if (grayIndex.get() >= Integer.MAX_VALUE) {
                grayIndex = new AtomicInteger(0);
            }
            if (index.get() >= Integer.MAX_VALUE) {
                index = new AtomicInteger(0);
            }
        }
        return null;
    }

    private void refreshCurrentFieldValue(ILoadBalancer loadBalancer) {
        IRule rule = ((BaseLoadBalancer) loadBalancer).getRule();
        if (!(rule instanceof GrayFlowRoutedRule)) {
            return;
        }
        if (!((GrayFlowRoutedRule) rule).grayHostMark.equals(this.grayHostMark)) {
            this.grayHostMark = ((GrayFlowRoutedRule) ((KcwlZoneAwareLoadBalancer) loadBalancer).getRule()).grayHostMark;
        }
        String newGrayServers = ((GrayFlowRoutedRule) rule).grayServers.stream().collect(Collectors.joining(","));
        String oldGrayServers = this.grayServers.stream().collect(Collectors.joining(","));
        if (!newGrayServers.equals(oldGrayServers)) {
            this.grayServers = ((GrayFlowRoutedRule) ((KcwlZoneAwareLoadBalancer) loadBalancer).getRule()).grayServers;
        }
    }

    /**
     * 根据请求特征 确定是否进入gray环境
     *
     * @param stableServers stable版本服务
     * @param grayServers   gray版本服务
     * @return
     */
    private Server selectServer(List<Server> stableServers, List<Server> grayServers, String serviceId) {
        String remoteHost = GrayMarkContextHolder.get();
        if (null != RequestContextHolder.getRequestAttributes() &&
                !StringUtils.isEmpty(((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest().getHeader(GrayConstant.GRAY_REQUEST_HEADER_KEY))) {
            remoteHost = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest().getHeader(GrayConstant.GRAY_REQUEST_HEADER_KEY);
        }
        remoteHost = remoteHost == null ? "default" : remoteHost;

        Server selectedServer = null;
        if (stableServers.size() == 0) {
            log.error("【{}】 stable版本服务地址为空，请确认！", serviceId);
            return selectedServer;
        }
        if (grayServers.size() > 0 && remoteHost.contains(grayHostMark) && this.grayServers.contains(serviceId)) {
            selectedServer = grayServers.get(this.grayIndex.getAndIncrement() % grayServers.size());
        } else {
            // 一般请求轮询选择 选择范围是stable版本的servers
            selectedServer = stableServers.get(this.stableIndex.getAndIncrement() % stableServers.size());
        }
        log.info("The server which GrayFlowRule selected: 【{}】", selectedServer.getHost());

        return selectedServer;
    }

    /**
     * IRule 初始化，会调用这个函数 通过配置文件指定Ribbon的 lb 和 IRule时，IRule的初始化是通过Class.ForName形式，导致@Value注解失效
     *
     * @param iClientConfig
     */
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        try {
            Environment environment = SpringUtil.getBean(Environment.class);
            this.grayHostMark = environment.getProperty(GrayConstant.GRAY_RULE_HOSTMARK);
            this.grayServers = Arrays.asList(environment.getProperty(GrayConstant.GRAY_RULE_GRAYSERVERLIST).split(","));
        } catch (Exception ex) {
            log.error("灰度分流IRule初始化失败，cause by:", ex);
        }
    }

}
