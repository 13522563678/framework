package com.kcwl.tenant.mybatis;

import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.kcwl.tenant.TenantDataHolder;
import com.kcwl.tenant.constants.TenantConstant;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author ckwl
 */
public class KcwlTenantHandler implements TenantHandler {

    private static final Logger logger = LoggerFactory.getLogger(KcwlTenantHandler.class);

    private static final char FLAG_QUOTA_MARK = '`';

    private String defaultPlatformNo;

    private Set<String> tenantTables;

    public KcwlTenantHandler() {
    }

    public KcwlTenantHandler(String defaultPlatformNo, Set<String> tenantTables) {
        this.defaultPlatformNo = defaultPlatformNo;
        this.tenantTables = tenantTables;
    }

    public void setTenantTables(Set<String> tenantTables) {
        this.tenantTables = tenantTables;
    }

    public void setDefaultPlatformNo(String defaultPlatformNo) {
        this.defaultPlatformNo = defaultPlatformNo;
    }

    @Override
    public Expression getTenantId(boolean select) {
        return new StringValue(getPlatformNo());
    }

    @Override
    public String getTenantIdColumn() {
        // 对应数据库租户ID的列名
        return TenantConstant.DB_TENANT_FIELD_NAME;
    }

    @Override
    public boolean doTableFilter(String tableName) {
        String platformNo = getPlatformNo();
        String realTableName=trimChar(tableName, FLAG_QUOTA_MARK);

        return isNullOrUnionPlatform(platformNo) || !tenantTables.contains(realTableName);
    }

    private String getPlatformNo() {
        String platformNo  = TenantDataHolder.get();
        if ( isNullOrUnionPlatform(platformNo) ) {
            platformNo = defaultPlatformNo;
        }
        return platformNo;
    }

    private String trimChar(String str, char ch) {
        int len = str.length();
        int beginIndex = -1, endIndex = -1;
        if (len > 0) {
            if (str.charAt(0) == ch) {
                beginIndex = 1;
            }
            if (str.charAt(len - 1) == ch) {
                endIndex = len - 1;
            }
        }
        return (beginIndex >0) && (beginIndex<endIndex)? str.substring(beginIndex, endIndex): str;
    }

    private boolean isNullOrUnionPlatform(String platformNo) {
        return platformNo == null || platformNo.isEmpty() || platformNo.endsWith(TenantConstant.UNIOIN_TENANT_ID_SUFFIX);
    }
}
