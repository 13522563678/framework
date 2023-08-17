package com.kcwl.framework.grayscale.ribbon;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.kcwl.framework.grayscale.constant.GrayConstant;
import com.kcwl.framework.grayscale.utils.GrayMarkContextHolder;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author: renyp
 * @description: 灰度分流策略1.0版本（只考虑用户请求的单一条件组合权重方式）
 * @date: created in 17:05 2021/8/18
 * @modify by:
 */

@Slf4j
public class GrayFlowRoutedRule extends AbstractLoadBalancerRule {

    private final AtomicInteger index = new AtomicInteger(0);
    private final AtomicInteger grayIndex = new AtomicInteger(0);
    private final AtomicInteger stableIndex = new AtomicInteger(0);

    private IClientConfig clientConfig;


    @Override
    public Server choose(Object key) {
        try {
            ILoadBalancer loadBalancer = getLoadBalancer();

            String grayHostMark = Optional.ofNullable(SpringUtil.getProperty(GrayConstant.GRAY_RULE_HOSTMARK)).orElseGet(() -> {
                log.warn("灰度发布 为读取到灰度域名标识，将默认使用 ‘canary’ 作为灰度域名标识！");
                return "canary";
            });
            if (StringUtils.isEmpty(grayHostMark)) {
                if (CollectionUtil.isEmpty(loadBalancer.getAllServers())) {
                    return null;
                }
                // 若没有灰度标记区分severs，则认为全部服务都属于stable版本，在allServers中轮询
                return loadBalancer.getAllServers().get(this.index.getAndIncrement() % loadBalancer.getAllServers().size());
            }

            Map<Boolean, List<Server>> serverPartition = loadBalancer.getAllServers().stream().collect(Collectors.partitioningBy(server -> server.getHost().contains(grayHostMark)));

            // 根据请求特征 确定是否进入gray环境
            return selectServer(serverPartition.get(false), serverPartition.get(true), grayHostMark);
        } catch (Exception ex) {
            log.error("", ex);
        } finally {
            stableIndex.compareAndSet(Integer.MAX_VALUE, 0);
            grayIndex.compareAndSet(Integer.MAX_VALUE, 0);
            index.compareAndSet(Integer.MAX_VALUE, 0);
        }
        return null;
    }


    /**
     * 根据请求特征 确定是否进入gray环境
     *
     * @param stableServers stable版本服务
     * @param grayServers   gray版本服务
     * @return 被选中的ServerInstance
     */
    private Server selectServer(List<Server> stableServers, List<Server> grayServers, String grayHostMark) {
        String remoteHost = GrayMarkContextHolder.get();
        if (null != RequestContextHolder.getRequestAttributes() &&
                !StringUtils.isEmpty(((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest().getHeader(GrayConstant.GRAY_REQUEST_HEADER_KEY))) {
            remoteHost = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest().getHeader(GrayConstant.GRAY_REQUEST_HEADER_KEY);
        }
        remoteHost = remoteHost == null ? "default" : remoteHost;

        Server selectedServer = null;
        if (CollUtil.isEmpty(stableServers)) {
            log.error("【{}】 stable版本服务地址为空，请确认！", clientConfig.getClientName());
            return selectedServer;
        }
        List<String> grayTestServers = Optional.ofNullable(SpringUtil.getProperty(GrayConstant.GRAY_RULE_GRAYSERVERLIST))
                .filter(StrUtil::isNotBlank)
                .map(servers -> servers.split(","))
                .map(Arrays::asList)
                .orElseGet(() -> {
                    log.error("灰度发布 读取当前灰度发布参与测试的服务列表失败，当前环境中灰度测试灰度测试的服务列表：{}, 将默认为无服务参与灰度测试！", SpringUtil.getProperty(GrayConstant.GRAY_RULE_GRAYSERVERLIST));
                    return new ArrayList<>();
                });
        if (CollUtil.isNotEmpty(grayServers) && remoteHost.contains(grayHostMark) && grayTestServers.contains(clientConfig.getClientName())) {
            selectedServer = grayServers.get(this.grayIndex.getAndIncrement() % grayServers.size());
        } else {
            // 一般请求轮询选择 选择范围是stable版本的servers
            selectedServer = stableServers.get(this.stableIndex.getAndIncrement() % stableServers.size());
        }
        log.info("The server which GrayFlowRule selected: 【{}】", selectedServer.getHost());

        return selectedServer;
    }

    /**
     * IRule 初始化，会调用这个函数 通过配置文件指定Ribbon的 lb、 IRule
     *
     * @param iClientConfig Ribbon Client 相关配置项
     */
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        this.clientConfig = iClientConfig;
    }

}
