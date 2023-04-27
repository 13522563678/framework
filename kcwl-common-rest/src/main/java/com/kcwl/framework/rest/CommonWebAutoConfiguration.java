package com.kcwl.framework.rest;

import com.kcwl.framework.cache.config.UserTokenRedisProperties;
import com.kcwl.framework.rest.aop.PlatformFieldCheckAspect;
import com.kcwl.framework.rest.helper.ConfigBeanName;
import com.kcwl.framework.rest.web.CommonWebConfig;
import com.kcwl.framework.rest.web.CommonWebProperties;
import com.kcwl.framework.rest.web.filter.ContentCacheFilter;
import com.kcwl.framework.rest.web.filter.DecryptParamFilter;
import com.kcwl.framework.rest.web.filter.XSSFilter;
import com.kcwl.framework.rest.web.interceptor.FeignRequestInterceptor;
import com.kcwl.framework.rest.web.interceptor.RestTemplateInterceptor;
import com.kcwl.framework.utils.KcBeanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.http.client.*;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @author 姚华成
 * @date 2017-12-14
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({CommonWebProperties.class, UserTokenRedisProperties.class})
@Import({CommonWebConfig.class})
public class CommonWebAutoConfiguration {

    private final boolean httpComponentPresent =
            ClassUtils.isPresent("org.apache.http.client.HttpClient",
                    CommonWebAutoConfiguration.class.getClassLoader());
    private final boolean okHttp3Present =
            ClassUtils.isPresent("okhttp3.OkHttpClient",
                    CommonWebAutoConfiguration.class.getClassLoader());
    @Resource
    private CommonWebProperties webProperties;

    @Bean("highCorsFilter")
    public FilterRegistrationBean<CorsFilter> highCorsFilter() {
        // addCorsMappings是跨域全局配置
        // springmvc提供默认的跨域拦截器，并且是所有拦截器中的最后一个
        // 由于跨域的预检请求OPTIONS默认无法通过认证拦截器，因此会造成无法允许进行跨域访问
        // 使用CorsFilter能够解决此问题，将CorsFilter的优先级提高到最高
        CorsConfiguration config = new CorsConfiguration();
        config.applyPermitDefaultValues().addAllowedMethod(CorsConfiguration.ALL);
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        CorsFilter corsFilter = new CorsFilter(configSource);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(corsFilter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<ContentCacheFilter> contentCacheFilter() {
        FilterRegistrationBean<ContentCacheFilter> bean = new FilterRegistrationBean<>(new ContentCacheFilter());
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<DecryptParamFilter> decryptParamFilter() {
        DecryptParamFilter decryptParamFilter = new DecryptParamFilter();
        decryptParamFilter.setEnableCrypt(webProperties.getCrypt().isEnabled());
        decryptParamFilter.setHttpContent(webProperties.getHttpContent());
        FilterRegistrationBean<DecryptParamFilter> bean = new FilterRegistrationBean<>(decryptParamFilter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 2);
        return bean;
    }

    @Bean
    @ConditionalOnProperty(prefix = "kcwl.common.xss-filter", value = "enabled", havingValue = "true")
    public FilterRegistrationBean<XSSFilter> xssFilter() {
        XSSFilter xssFilter = new XSSFilter();
        FilterRegistrationBean<XSSFilter> bean = new FilterRegistrationBean<>(xssFilter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 3);
        return bean;
    }

    //@Bean
    public RestTemplateCustomizer restTemplateCustomizer() {
        // 强制删除jackson的json的解析器，替换成gson的json解析器
        // Spring5.0默认使用Gson解析器
        return restTemplate -> {
            restTemplate.setRequestFactory(clientHttpRequestFactory());
            restTemplate.getMessageConverters().removeIf(converter ->
                    converter instanceof MappingJackson2HttpMessageConverter);
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
        };
    }

    @Bean
    @Primary
    public RestTemplate defaultRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @LoadBalanced
    @Bean
    public RestTemplate feignRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        RestTemplateInterceptor restTemplateInterceptor = new RestTemplateInterceptor(webProperties.getAuth());
        restTemplate.setInterceptors(Collections.singletonList(restTemplateInterceptor));
        return restTemplate;
    }

    @Bean
    public FeignRequestInterceptor tenantFeignRequestInterceptor() {
        return new FeignRequestInterceptor(webProperties.getAuth());
    }

    @Bean
    //@ConditionalOnProperty(value = "kcwl.tenant.platform.check", matchIfMissing = false)
    public PlatformFieldCheckAspect platformFieldCheckAspect() {
        return new PlatformFieldCheckAspect();
    }

    @Bean
    public KcBeanRepository jwtConfigRepository() {
        KcBeanRepository kcBeanRepository = KcBeanRepository.getInstance();
        kcBeanRepository.saveBean(ConfigBeanName.JWT_CONFIG_NAME, webProperties.getJwt());
        log.info("jwtConfig: {}", kcBeanRepository.getBean(ConfigBeanName.JWT_CONFIG_NAME));
        return kcBeanRepository;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        CommonWebProperties.HttpClient httpClient = webProperties.getHttpClient();
        if (httpClient.getType() == CommonWebProperties.HttpClientType.HTTP_COMPONENT
                && httpComponentPresent) {
            // HttpComponentsClientHttpRequestFactory默认使用HttpClients.createSystem()创建HttpClient，
            // 可以通过系统属性进行定制化设置
            // 系统属性只能设置每个路由目标的最大连接数，整个池的最大连接数为其两倍大小
            int maxConnPerRoute = (webProperties.getHttpClient().getMaxConnTotal() + 1) / 2;
            System.setProperty("http.maxConnections", String.valueOf(maxConnPerRoute));
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectionRequestTimeout(httpClient.getConnectionRequestTimeout());
            factory.setConnectTimeout(httpClient.getConnectTimeout());
            factory.setReadTimeout(httpClient.getReadTimeout());
            factory.setBufferRequestBody(httpClient.isBufferRequestBody());
            return factory;
        } else if (httpClient.getType() == CommonWebProperties.HttpClientType.OK_HTTP3
                && okHttp3Present) {
            OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory();
            factory.setConnectTimeout(httpClient.getConnectTimeout());
            factory.setReadTimeout(httpClient.getReadTimeout());
            factory.setWriteTimeout(httpClient.getWriteTimeout());
            return factory;
        } else {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(httpClient.getConnectTimeout());
            factory.setReadTimeout(httpClient.getReadTimeout());
            return factory;
        }
    }
}
