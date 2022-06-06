package com.kcwl.framework.datasource.masterslave;

import com.google.gson.Gson;
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
    private static Gson gson = new Gson();

    @Around("@annotation(SlaveDataSource)")
    public Object readonly(ProceedingJoinPoint pjp) throws Throwable {
        try {
            MasterSlaveDataSourceContextHolder.setDataSourceType(DataSourceType.SLAVE);
            logger.debug("Before: Use DataSource : {} > {}", "slave", pjp.getSignature());
            Object ret = pjp.proceed();
            if (logger.isDebugEnabled()) {
                logger.debug("Result: Use DataSource : {} > {} > {}", "slave", pjp.getSignature(), gson.toJson(ret));
            }
            return ret;
        } finally {
            logger.debug("After: Revert DataSource : {} > {}", "master", pjp.getSignature());
            MasterSlaveDataSourceContextHolder.clearDataSourceType();
        }
    }
}
