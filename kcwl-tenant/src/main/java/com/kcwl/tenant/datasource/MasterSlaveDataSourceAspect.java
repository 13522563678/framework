package com.kcwl.tenant.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

/**
 * 切换主从数据源Advice
 *
 * @author 姚华成
 * @date 2017-12-15
 */
@Aspect
@Order(-1)
public class MasterSlaveDataSourceAspect {

    private static final Logger logger = LoggerFactory.getLogger(MasterSlaveDataSourceAspect.class);

    @Around("@annotation(SlaveDataSource)")
    public Object readonly(ProceedingJoinPoint pjp) throws Throwable {
        DataSourceType oldDataSourceType = MasterSlaveDataSourceContextHolder.getDataSourceType();
        try {
            MasterSlaveDataSourceContextHolder.setDataSourceType(DataSourceType.SLAVE);
            logger.debug("Before: Use DataSource : {} > {}", "slave", pjp.getSignature());
            Object ret = pjp.proceed();
            if (logger.isDebugEnabled()) {
                logger.debug("Result: Use DataSource : slave > {} > {}", pjp.getSignature(), ret);
            }
            return ret;
        } finally {
            if ( oldDataSourceType == null ) {
                logger.debug("After: Revert DataSource : {} > {}", "master", pjp.getSignature());
                MasterSlaveDataSourceContextHolder.clearDataSourceType();
            } else {
                logger.debug("After: Revert DataSource : {} > {}", "oldDataSource", pjp.getSignature());
                MasterSlaveDataSourceContextHolder.setDataSourceType(oldDataSourceType);
            }
        }
    }
}
