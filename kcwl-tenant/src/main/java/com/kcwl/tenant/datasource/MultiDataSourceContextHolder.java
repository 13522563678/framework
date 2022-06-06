package com.kcwl.tenant.datasource;

/**
 * @author ckwl
 */
public class MultiDataSourceContextHolder {
    private static ThreadLocal<String> contextHolder = new ThreadLocal<>();

    private MultiDataSourceContextHolder() {
    }

    public static String getDataSourceName() {
        return contextHolder.get();
    }

    public static void setDataSourceName(String dataSourceName) {
        contextHolder.set(dataSourceName);
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}
