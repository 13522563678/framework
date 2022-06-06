package com.kcwl.framework.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

/**
 * @author ckwl
 */
@Data
@ConfigurationProperties(prefix = "kcwl.tenant")
public class TenantDataSourceProperties {

    private HashMap<String, DataSourceProperties> datasource;

    @Data
    public static class DataSourceProperties {
        private Properties master;
        private Properties[] slaves;
        private Set<String> supportTenant;
        private boolean primary;
    }
}
