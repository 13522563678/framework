package com.kcwl.framework.kcwlsentinelclient;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.cloud.sentinel.SentinelProperties;
import com.alibaba.cloud.sentinel.datasource.config.ApolloDataSourceProperties;
import com.alibaba.cloud.sentinel.datasource.config.DataSourcePropertiesConfiguration;
import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlCleaner;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.adapter.servlet.config.WebServletConfig;
import com.alibaba.csp.sentinel.cluster.ClusterStateManager;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientAssignConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfigManager;
import com.alibaba.csp.sentinel.property.DynamicSentinelProperty;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.framework.kcwlsentinelclient.Constant.CommonConstant;
import com.kcwl.framework.kcwlsentinelclient.config.KcwlSentinelConfiguration;
import com.kcwl.framework.rest.helper.ResponseHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author renyp
 */
@Configuration
@EnableConfigurationProperties(KcwlSentinelConfiguration.class)
@ConditionalOnClass({Environment.class, SentinelProperties.class})
public class KcwlSentinelClientAutoConfiguration {

    @Value("${kcwl.sentinel.tokenserver.host:localhost}")
    private String tokenServerHost;

    @Value("${kcwl.sentinel.tokenserver.port:9818}")
    private int tokenServerPort;

    @Resource
    private KcwlSentinelConfiguration kcwlSentinelConfiguration;

    @PostConstruct
    public void initSentinelConfiguration() {
        // 设置当前项目的流控规则 在apollo默认ns中对应的key
        SentinelProperties sentinelProperties = SpringUtil.getBean(SentinelProperties.class);
        String flowRulesKey = SpringUtil.getBean(Environment.class).getProperty(CommonConstant.FLOW_RULES_DS_COMMON_KEY);
        String applicationName = SpringUtil.getBean(Environment.class).getProperty(CommonConstant.APPLICATION_KEY);
        if (!StringUtils.isEmpty(flowRulesKey) && !StringUtils.isEmpty(applicationName)) {
            flowRulesKey = flowRulesKey.replace(CommonConstant.FLOW_RULES_PREFIX, applicationName);

            Map<String, DataSourcePropertiesConfiguration> dataSource = sentinelProperties.getDatasource();
            if (dataSource.containsKey(CommonConstant.DS) && dataSource.get(CommonConstant.DS).getValidDataSourceProperties() instanceof ApolloDataSourceProperties) {
                dataSource.get(CommonConstant.DS).getApollo().setFlowRulesKey(flowRulesKey);
            }
        }

        // sentinel urlCleaner 的统一处理
        WebCallbackManager.setUrlCleaner(new UrlCleaner() {
            @Override
            public String clean(String originUrl) {
                if (StringUtils.isEmpty(originUrl)) {
                    return originUrl;
                }
                // 健康检查接口不做处理
                if (kcwlSentinelConfiguration.getPath().contains(originUrl)) {
                    return "";
                }
                return originUrl;
            }
        });
        // sentinel BlockException 的统一处理
        WebCallbackManager.setUrlBlockHandler(new UrlBlockHandler() {
            @Override
            public void blocked(HttpServletRequest request, HttpServletResponse response, BlockException e) throws IOException {
                // copy from com.alibaba.csp.sentinel.adapter.servlet.util.FilterUtil
                StringBuffer url = request.getRequestURL();
                if ("GET".equals(request.getMethod()) && StringUtil.isNotBlank(request.getQueryString())) {
                    url.append("?").append(request.getQueryString());
                }

                if (StringUtil.isBlank(WebServletConfig.getBlockPage())) {
                    writeDefaultBlockedPage(response);
                } else {
                    String redirectUrl = WebServletConfig.getBlockPage() + "?http_referer=" + url.toString();
                    response.sendRedirect(redirectUrl);
                }
            }

            private void writeDefaultBlockedPage(HttpServletResponse response) throws IOException {
                // custom
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.print(ResponseHelper.fail(CommonCode.SYS_ERROR.getCode(), CommonCode.SYS_ERROR.getDescription()));
                out.flush();
                out.close();
            }
        });

        // 配置集群流控模式中的token-server地址
        ClusterClientConfigManager.registerServerAssignProperty(new DynamicSentinelProperty(
                new ClusterClientAssignConfig() {{
                    this.setServerHost(tokenServerHost);
                    this.setServerPort(tokenServerPort);
                }})
        );

        // 默认配置：token client模式
        ClusterStateManager.registerProperty(new DynamicSentinelProperty<>(ClusterStateManager.CLUSTER_CLIENT));
    }
}
