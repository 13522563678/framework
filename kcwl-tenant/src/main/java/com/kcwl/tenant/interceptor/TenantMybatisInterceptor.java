package com.kcwl.tenant.interceptor;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.kcwl.tenant.constants.TenantConstant;
import com.kcwl.tenant.helper.TenantProperties;
import com.kcwl.tenant.mybatis.KcwlTenantHandler;
import com.kcwl.tenant.mybatis.KcwlTenantSqlParser;
import com.kcwl.tenant.mybatis.TenantSqlParserFilter;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.*;

/**
 * @author ckwl
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class TenantMybatisInterceptor extends PaginationInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TenantMybatisInterceptor.class);

    @Override
    public void setProperties(Properties prop) {
        super.setProperties(prop);
        this.init(prop);
    }

    public void initSqlParser(String platformNo, Set<String> tenantTables, boolean tenantEnabled, Set<String> filterSql) {
        this.initSqlParser(platformNo, tenantTables, tenantEnabled, filterSql, false, false);
    }

    public void initSqlParser(String platformNo, Set<String> tenantTables, boolean tenantEnabled, Set<String> filterSql, boolean filterInsert, boolean filterSelect) {
        TenantSqlParser tenantSqlParser = new KcwlTenantSqlParser();

        logger.info("initSqlParser with platform {}, tenantEnabled {}", platformNo, tenantEnabled);
        logger.info("initSqlParser filsterSql {}", filterSql);
        /*
         * platformNo: 为当前系统配置的平台标识码
         * tenantTables: 需要处理平台标识码的表（注意，不是过滤）
         */
        tenantSqlParser.setTenantHandler(new KcwlTenantHandler(platformNo, tenantTables));

        // 创建SQL解析器集合
        List<ISqlParser> sqlParserList = new ArrayList<>();

        sqlParserList.add(tenantSqlParser);

        this.setSqlParserList(sqlParserList);

        this.setSqlParserFilter(new TenantSqlParserFilter(tenantEnabled, filterSql, filterInsert, filterSelect));
    }

    private void  init(Properties prop) {
        Set<String> tenantTablesSet = null;

        String propertiesFile = (String) prop.get("tenantPropertiesFile");
        if (propertiesFile == null) {
            propertiesFile = TenantConstant.PROP_FILE_NAME;
        }

        TenantProperties tenantProperties = new TenantProperties(propertiesFile);

        String platformNo = tenantProperties.getProperty(TenantConstant.PROP_KEY_DEFAULT_TENANT);
        String tenantTables = tenantProperties.getProperty(TenantConstant.PROP_KEY_TENANT_TABLES);
        boolean tenantEnabled = tenantProperties.getPropertyBoolean(TenantConstant.PROP_KEY_TENANT_ENABLED);
        Set<String> filterSql = tenantProperties.getPropertySet(TenantConstant.PROP_KEY_FILTER_SQL);

        if (tenantTables != null) {
            tenantTablesSet = new HashSet<String>(Arrays.asList(tenantTables.split(",")));
        }
        initSqlParser(platformNo, tenantTablesSet, tenantEnabled, filterSql);
    }
}
