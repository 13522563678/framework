package com.kcwl.framework.datasource.masterslave;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author 姚华成
 * @date 2017-12-19
 */
@ConditionalOnProperty(value = "spring.datasource.masterslave.enabled", matchIfMissing = true)
@EnableConfigurationProperties({MasterSlaveDataSourceProperties.class})
@AutoConfigureBefore({DruidDataSourceAutoConfigure.class, DataSourceAutoConfiguration.class})
public class MasterSlaveDataSourceAutoConfiguration {
    @Resource
    private MasterSlaveDataSourceProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public DataSource defaultDataSource() {
        Properties masterProperties = properties.getMaster();
        if (masterProperties == null) {
            throw new RuntimeException("使用主从数据源必须使用master数据源");
        }
        return new MasterSlaveDataSource(properties);
    }

    @Bean
    @ConditionalOnBean(DataSource.class)
    public MasterSlaveDataSourceAspect masterSlaveDsAspect() {
        return new MasterSlaveDataSourceAspect();
    }

}
