package com.kcwl.framework.datasource.masterslave;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import static com.alibaba.druid.util.Utils.getBoolean;

/**
 * 一主多从数据源
 *
 * @author 姚华成
 * @date 2017-12-15
 */
public class MasterSlaveDataSource extends AbstractRoutingDataSource {
    private int slaveCount;
    private Random random = new Random();

    public MasterSlaveDataSource(MasterSlaveDataSourceProperties properties) {
        init(properties.getMaster(), properties.getSlave(), properties.getSlaves());
    }

    @Override
    protected Object determineCurrentLookupKey() {
        DataSourceType dataSourceType = MasterSlaveDataSourceContextHolder.getDataSourceType();
        if (dataSourceType.equals(DataSourceType.MASTER) || slaveCount == 0) {
            return DataSourceType.MASTER;
        }
        return genSlaveKey(random.nextInt(slaveCount));
    }

    public void init(Properties master, Properties slave, Properties[] slaves) {
        if (slave != null) {
            // slave覆盖slaves的配置
            slaves = new Properties[]{slave};
        }
        slaveCount = (slaves == null ? 0 : slaves.length);
        Map<Object, Object> targetDataSources = new HashMap<>(slaveCount + 1);
        // master
        DruidDataSource masterDataSource = DruidDataSourceBuilder.create().build();
        masterDataSource.configFromPropety(decrypt(master));
        configFromPropertyEx(masterDataSource, master);
        this.setDefaultTargetDataSource(masterDataSource);
        targetDataSources.put(DataSourceType.MASTER, masterDataSource);

        // slaves
        if (slaves != null) {
            for (int i = 0; i < slaves.length; i++) {
                DruidDataSource slaveDataSource = DruidDataSourceBuilder.create().build();
                slaveDataSource.configFromPropety(decrypt(slaves[i]));
                configFromPropertyEx(slaveDataSource, slaves[i]);
                targetDataSources.put(genSlaveKey(i), slaveDataSource);
            }
        }
        this.setTargetDataSources(targetDataSources);
    }

    private Properties decrypt(Properties properties) {
        boolean encrypt = Boolean.parseBoolean(
                properties.getProperty("druid.password-encrypt", "false"));
        if (encrypt) {
            String encryptPassword = properties.getProperty("druid.password");
            String decryptPassword = DbDesUtil.decrypt(encryptPassword);
            properties.setProperty("druid.password", decryptPassword);
            properties.setProperty("druid.password-encrypt", "false");
        }
        return properties;
    }


    private String genSlaveKey(int i) {
        return "slave:" + i;
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
