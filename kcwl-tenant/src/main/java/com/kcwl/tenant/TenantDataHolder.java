package com.kcwl.tenant;

/**
 * @author ckwl
 */
public class TenantDataHolder {

    private static ThreadLocal<String> tenantDataLocal = new ThreadLocal<>();

    private TenantDataHolder() {
    }

    public static String get() {
        return tenantDataLocal.get();
    }

    public static void set(String platformNo) {
        tenantDataLocal.set(platformNo);
    }

    public static void remove() {
        tenantDataLocal.remove();
    }
}
