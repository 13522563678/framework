package com.kcwl.framework.rest.web;

import com.kcwl.ddd.infrastructure.constants.GlobalConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 姚华成
 * @date 2018-01-18
 */
@Data
@ConfigurationProperties(prefix = "kcwl.common.web")
public class CommonWebProperties {

    private static final String DEFAULT_INNER_PATH_PATTERN = "/inner/**";
    private static final String DEFAULT_USER_API_PATH_PATTERN = "/user/**";
    private static final String DEFAULT_ALL_PATH_PATTERN = "/**";


    private Json json = new Json();
    private ApiAuthConfig inner = new ApiAuthConfig(DEFAULT_INNER_PATH_PATTERN);
    private ApiAuthConfig api = new ApiAuthConfig(DEFAULT_USER_API_PATH_PATTERN);
    private ApiAuthConfig crm = new ApiAuthConfig(false);
    private ApiAuthConfig deny = new ApiAuthConfig();
    private ApiAuthConfig session = new ApiAuthConfig(DEFAULT_ALL_PATH_PATTERN);
    private ApiAuthConfig mock = new ApiAuthConfig(false);

    private Tenant tenant = new Tenant();
    private Crypt crypt = new Crypt();
    private HttpContent httpContent = new HttpContent();
    private HttpClient httpClient = new HttpClient();
    private ServiceInfo service =new ServiceInfo();
    private String mockUrl;
    private AppAuthInfo auth = new AppAuthInfo();
    private AppPodInfo appPod = new AppPodInfo();
    private SsoAuthInfo sso = new SsoAuthInfo();

    /**
     * http客户端类型支持：
     * HttpComponent,
     * HttpComponentAsync,
     * OkHttp3,
     * OkHttp
     * Netty4,
     * 以及JDK的HttpURLConnection
     *
     * @author 姚华成
     * @date 2018-01-18
     */
    public enum HttpClientType {
        /**
         *
         */
        HTTP_COMPONENT,
        OK_HTTP3,
        JDK
    }

    @Data
    public static class Json {
        private boolean serializeNulls = true;
        private String dateFormat = "yyyy-MM-dd HH:mm:ss";
        private boolean customHttpMessageConverter=true;
    }

    @Data
    public static class ApiAuthConfig {
        private boolean enabled = true;
        private boolean ignoreSession = false;
        private String appSecret ="kcwl@123456";
        private  long requestTimeout = 180*1000;
        private  long protectInterval = 600*1000;
        /*
         * 需要拦截的请求路径
         */
        private List<String> pathPatterns = new ArrayList<>();

        /*
         * 需要排除的的请求路径
         */
        private List<String> excludePathPatterns = new ArrayList<>();

        /*
         * 过滤中忽略的的请求路径
         */
        private List<String> ignorePathPatterns = new ArrayList<>();

        public ApiAuthConfig() {
        }

        public ApiAuthConfig(String defaultPathPatterns) {
            pathPatterns.add(defaultPathPatterns);
        }

        public ApiAuthConfig(boolean enabled) {
            this.enabled = enabled;
        }
    }

    @Data
    public static class Tenant {
        private boolean enabled = true;
        private String defaultPlatform = GlobalConstant.UNIOIN_TENANT_ID;
    }

    @Data
    public static class Crypt {
        private boolean enabled = true;
    }

    @Data
    public static class HttpContent {
        private boolean enableFormToJson = false;
        /*
         * 需要排除的的请求路径
         */
        private List<String> excludePathPatterns = new ArrayList<>();

        public boolean isFormToJson(String path) {
            return enableFormToJson && !excludePathPatterns.contains(path);
        }
    }

    @Data
    public static class HttpClient {
        private HttpClientType type = HttpClientType.HTTP_COMPONENT;
        /**
         * 通用配置
         * 连接超时时间
         */
        private int connectTimeout = 5000;
        /**
         * 通用配置
         * 读取超时时间
         */
        private int readTimeout = 5000;

        /**
         * HttpComponent的专用配置
         * 连接池最大连接数
         */
        private int maxConnTotal = 50;
        /**
         * HttpComponent的专用配置
         * 从连接池获取连接超时时间
         */
        private int connectionRequestTimeout = 5000;

        /**
         * HttpComponent的专用配置
         * 是否缓存请求的Body
         */
        private boolean bufferRequestBody = true;
        /**
         * OkHttp3, OkHttp专用配置
         * 写超时时间
         */
        private int writeTimeout = 5000;
        /**
         * Netty4专用配置
         * 响应最大大小
         */
        private int maxResponseSize = 8192;
    }

    @Data
    public static class ServiceInfo {
        private String type="00";
    }

    @Data
    static class MockInfo{
        private boolean enabled = false;
        private String mockUrl;
        /*
         * 需要拦截的请求路径
         */
        private List<String> pathPatterns = new ArrayList<>();
    }

    @Data
    public static  class AppAuthInfo{
        private boolean enabled = false;
        private String appId;
        private String appSecret;
    }

    @Data
    public static class AppPodInfo{
        private boolean isolation=false;
    }

    @Data
    public static class SsoAuthInfo{
        private List<String> supportProducts;
    }
}
