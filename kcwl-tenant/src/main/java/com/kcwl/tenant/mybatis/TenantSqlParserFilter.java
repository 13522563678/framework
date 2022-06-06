package com.kcwl.tenant.mybatis;

import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author ckwl
 */
public class TenantSqlParserFilter implements ISqlParserFilter {

    private static final Logger logger = LoggerFactory.getLogger(TenantSqlParserFilter.class);

    private boolean enabledTenant;
    private boolean filterInsert = false;
    private boolean filterSelect = false;

    private Set<String> filterSql;


    public TenantSqlParserFilter() {
    }

    public TenantSqlParserFilter(boolean enabledTenant, Set<String> filterSql) {
        this.enabledTenant = enabledTenant;
        this.filterSql = filterSql;
        this.filterInsert = false;
        this.filterSelect = false;
    }

    public TenantSqlParserFilter(boolean enabledTenant, Set<String> filterSql, boolean filterInsert, boolean filterSelect) {
        this.enabledTenant = enabledTenant;
        this.filterSql = filterSql;
        this.filterInsert = filterInsert;
        this.filterSelect = filterSelect;
    }

    @Override
    public boolean doFilter(MetaObject metaObject) {
        if ( !enabledTenant ) {
            return true;
        }
        MappedStatement ms = SqlParserHelper.getMappedStatement(metaObject);

        if ( ms.getSqlCommandType() == SqlCommandType.INSERT && this.filterInsert ) {
            return true;
        }
        if ( ms.getSqlCommandType() == SqlCommandType.SELECT && this.filterSelect ) {
            return true;
        }
        if ((filterSql != null) && (filterSql.contains(ms.getId()))) {
            logger.info("sql filter: {}", ms.getId());
            return true;
        }

        return false;
    }
}
