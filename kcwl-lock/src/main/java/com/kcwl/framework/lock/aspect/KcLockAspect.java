package com.kcwl.framework.lock.aspect;

import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.framework.lock.DistributedLocker;
import com.kcwl.framework.lock.annotations.KcLock;
import com.kcwl.framework.lock.exception.LockResourceException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 *@ClassName KcLockAspect
 *@Description
 *@Author wangl
 *@Date 2022/07/22
 */
@Component
@Aspect
public class KcLockAspect implements Ordered {
    @Resource
    DistributedLocker distributedLocker;

    private final static String LOCK_PREFIX = "kcLock:";

    @Pointcut(value = "@annotation(com.kcwl.framework.lock.annotations.KcLock)")
    private void pointcut() {
    }
    @Around("pointcut()")
    public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        Method m = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        Method methodWithAnnotations;
        try {
            methodWithAnnotations = proceedingJoinPoint.getTarget().getClass().getDeclaredMethod(
                    proceedingJoinPoint.getSignature().getName(), m.getParameterTypes());
            KcLock kcLock = methodWithAnnotations.getAnnotation(KcLock.class);
            String key = parseKey(methodWithAnnotations, proceedingJoinPoint.getArgs(), kcLock.key());
            return proceedingOnLock(proceedingJoinPoint, kcLock, key);
        } catch (Exception e) {
            throw e;
        }
    }

    private  Object proceedingOnLock(ProceedingJoinPoint proceedingJoinPoint, KcLock kcLock, String key) throws Throwable {
        String lockKey = LOCK_PREFIX + kcLock.prefix() + key;
        try {
            Boolean b = distributedLocker.tryLock(lockKey, kcLock.timeUnit(), kcLock.waitingTime(), kcLock.leaseTime());
            if (!b) {
                throwLockException(CommonCode.RESOURCE_LOCK_FAIL, kcLock.message());
            }
            return proceedingJoinPoint.proceed();
        }finally {
            distributedLocker.unlock(lockKey);
        }
    }

    private String parseKey(Method method, Object[] argValues, String keyEl) {
        if (StringUtils.isEmpty(keyEl)) {
            return "";
        }
        //创建解析器
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(keyEl);
        EvaluationContext context = new StandardEvaluationContext();
        // 添加参数
        DefaultParameterNameDiscoverer discover = new DefaultParameterNameDiscoverer();
        String[] parameterNames = discover.getParameterNames(method);
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], argValues[i]);
        }
        // 解析
        return expression.getValue(context).toString();
    }

    private void throwLockException(CommonCode commonCode, String lockErrorMessage) {
        String message = null;
        if (StringUtils.isEmpty(lockErrorMessage) ) {
            message =   lockErrorMessage;
        } else {
            message = commonCode.getDescription();
        }
        throw new LockResourceException(commonCode.getCode(), message);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
