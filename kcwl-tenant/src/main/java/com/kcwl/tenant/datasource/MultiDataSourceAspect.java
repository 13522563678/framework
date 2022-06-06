package com.kcwl.tenant.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

/**
 * @author ckwl
 */
@Aspect
@Order(-1)
public class MultiDataSourceAspect {

    private static final Logger logger = LoggerFactory.getLogger(MultiDataSourceAspect.class);

    @Around("@within(MultiDataSource)")
    public Object multiDataSourceMethodPoint(ProceedingJoinPoint pjp) throws Throwable {
        return switchMultiDataSource(pjp);
    }

    @Around("@annotation(MultiDataSource)")
    public Object multiDataSourceClassPoint(ProceedingJoinPoint pjp) throws Throwable {
        return switchMultiDataSource(pjp);
    }

    public Object switchMultiDataSource(ProceedingJoinPoint pjp) throws Throwable {
        String oldDataSourceName = MultiDataSourceContextHolder.getDataSourceName();
        try {
            String newDataSourceName = getDataSourceName(pjp);
            if ( !StringUtils.isEmpty(newDataSourceName) ) {
                MultiDataSourceContextHolder.setDataSourceName(newDataSourceName);
                logger.debug("Before: Use DataSource : {} > {}", "slave", pjp.getSignature());
            }
            Object ret = pjp.proceed();
            if (logger.isDebugEnabled()) {
                logger.debug("Result: Use DataSource : slave > {} > {}", pjp.getSignature(), ret);
            }
            return ret;
        } finally {
            if ( oldDataSourceName == null ) {
                logger.debug("After: Revert DataSource : {} > {}", "master", pjp.getSignature());
                MultiDataSourceContextHolder.clearDataSourceType();
            } else  {
                logger.debug("After: Revert DataSource : {}", oldDataSourceName);
                MultiDataSourceContextHolder.setDataSourceName(oldDataSourceName);
            }
        }
    }

    /**
     * 根据类或方法获取数据源注解
     */
    private String getDataSourceName(ProceedingJoinPoint joinPoint){
        MultiDataSource multiDataSource = null;
        Signature signature = joinPoint.getSignature();
        if ( signature != null ) {
            MethodSignature methodSignature = (MethodSignature) signature;
            multiDataSource = methodSignature.getMethod().getAnnotation(MultiDataSource.class);
            if ( multiDataSource == null ) {
                multiDataSource = joinPoint.getTarget().getClass().getAnnotation(MultiDataSource.class);
            }
        }
        return (multiDataSource!=null) ?  multiDataSource.value() : null;
    }
}
