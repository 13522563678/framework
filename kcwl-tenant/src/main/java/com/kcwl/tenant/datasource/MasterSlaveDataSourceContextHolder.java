package com.kcwl.tenant.datasource;

/**
 * @author 姚华成
 * @date 2017-12-15
 */
public class MasterSlaveDataSourceContextHolder {
    private static ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<>();

    private MasterSlaveDataSourceContextHolder() {
    }

    public static DataSourceType getDataSourceType() {
        return contextHolder.get() == null ? DataSourceType.MASTER : contextHolder.get();
    }

    public static void setDataSourceType(DataSourceType dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}