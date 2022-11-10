package com.kcwl.framework.datasource;

import com.alibaba.druid.Constants;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.kcwl.tenant.datasource.DataSourceType;
import com.kcwl.tenant.datasource.MasterSlaveDataSourceAspect;
import com.kcwl.tenant.datasource.MultiDataSourceAspect;
import com.kcwl.tenant.datasource.TenantDataSource;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import static com.alibaba.druid.util.Utils.getBoolean;

/**
 * @author 姚华成
 * @date 2017-12-19
 */
@EnableConfigurationProperties({TenantDataSourceProperties.class})
@AutoConfigureBefore({DruidDataSourceAutoConfigure.class, DataSourceAutoConfiguration.class})
public class TenantDataSourceAutoConfiguration {
    @Resource
    TenantDataSourceProperties tenantDataSourceProperties;

    @Bean
    public DataSource tenantDataSource() {
        //获取动态数据库的实例（单例方式）
        TenantDataSource tenantDataSource = TenantDataSource.getInstance();
        Iterator<Map.Entry<String, TenantDataSourceProperties.DataSourceProperties>> iter = tenantDataSourceProperties.getDatasource().entrySet().iterator();
        while ( iter.hasNext() ) {
            Map.Entry<String, TenantDataSourceProperties.DataSourceProperties> entry = iter.next();
            createTenantDataSource(tenantDataSource,entry.getKey(), entry.getValue());
        }
        return tenantDataSource;
    }

    @Bean
    @ConditionalOnBean(DataSource.class)
    public MasterSlaveDataSourceAspect masterSlaveDsAspect() {
        return new MasterSlaveDataSourceAspect();
    }

    @Bean
    @ConditionalOnBean(DataSource.class)
    public MultiDataSourceAspect multiDataSourceAspect() {
        return new MultiDataSourceAspect();
    }

    private void createTenantDataSource(TenantDataSource tenantDataSource, String tenantId, TenantDataSourceProperties.DataSourceProperties properties) {
        Properties[] slaves = properties.getSlaves();
        DruidDataSource masterDataSource = createDataSource(properties.getMaster());
        tenantDataSource.addDataSource(tenantId, masterDataSource, DataSourceType.MASTER, 0, properties.isPrimary());
        if ( slaves != null ) {
            for ( int i=0; i<slaves.length; i++ ) {
                DruidDataSource slaverDataSource = createDataSource(slaves[i]);
                tenantDataSource.addDataSource(tenantId, slaverDataSource, DataSourceType.SLAVE, i, false);
            }
        }
        tenantDataSource.addSupportTenant(tenantId, properties.getSupportTenant());
    }

    private DruidDataSource createDataSource(Properties dbProp) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.configFromPropety(dbProp);
        configFromPropertyEx(dataSource, dbProp);
        return dataSource;
    }

    private void configFromPropertyEx(DruidDataSource dataSource, Properties dbProp) {
        {
            Long value = getLong(dbProp, "druid.maxWait");
            if (value != null) {
                dataSource.setMaxWait(value);
            }
        }
        {
            Boolean value = getBoolean(dbProp, "druid.removeAbandoned");
            if (value != null) {
                dataSource.setRemoveAbandoned(value);
            }
        }
        {
            Boolean value = getBoolean(dbProp, "druid.logAbandoned");
            if (value != null) {
                dataSource.setLogAbandoned(value);
            }
        }
        {
            Integer value = getInteger(dbProp, "druid.removeAbandonedTimeout");
            if (value != null) {
                dataSource.setRemoveAbandonedTimeout(value);
            }
        }
    }

    private Long getLong(Properties properties, String key) {
        String property = properties.getProperty(key);
        if ( property != null && property.length() >0 ) {
            try {
                return Long.parseLong(property);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Integer getInteger(Properties properties, String key) {
        String property = properties.getProperty(key);
        if ( property != null && property.length() >0 ) {
            try {
                return Integer.parseInt(property);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
