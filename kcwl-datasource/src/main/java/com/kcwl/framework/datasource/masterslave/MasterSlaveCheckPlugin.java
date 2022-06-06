package com.kcwl.framework.datasource.masterslave;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ckwl
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MasterSlaveCheckPlugin  implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(MasterSlaveCheckPlugin.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        if ( (SqlCommandType.SELECT != ms.getSqlCommandType()) && (MasterSlaveDataSourceContextHolder.getDataSourceType() == DataSourceType.SLAVE) ) {
            MasterSlaveDataSourceContextHolder.clearDataSourceType();
            logger.warn("force switch to MASTER");
        }
        return invocation.proceed();
    }
}
