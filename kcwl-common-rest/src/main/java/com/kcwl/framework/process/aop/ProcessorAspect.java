package com.kcwl.framework.process.aop;

import cn.hutool.extra.spring.SpringUtil;
import com.kcwl.framework.process.annotation.KcProcessor;
import com.kcwl.framework.process.IProcessor;
import com.kcwl.framework.utils.KcBeanConverter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @description:  处理器切面
 * @author wangwl
 * @date: 2023/2/2 18:21
 */
@Aspect
@Component
@Slf4j
public class ProcessorAspect {

    @Pointcut("@annotation(com.kcwl.framework.process.annotation.KcProcessor)")
    public void KcProcessor() {
    }

    @Around("KcProcessor()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //通过切点获取注解中的方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取注解中的方法
        Method method = signature.getMethod();
        Method targetMethod = AopUtils.getMostSpecificMethod(method, joinPoint.getTarget().getClass());
        //再获取被注解的类携带的参数
        Object[] args = joinPoint.getArgs();
        if (null == args || args.length <= 0) {
            return joinPoint.proceed();
        }
        //获取当前方法注解中的参数
        KcProcessor handlers = AnnotationUtils.findAnnotation(targetMethod, KcProcessor.class);
        //构建校验参数
        Class<? extends IProcessor>[] pres = handlers.before();
        doPreHandler(args[0], pres);

        Object returnArgs = joinPoint.proceed();
        Class<? extends IProcessor>[] posts = handlers.after();

        doPostHandler(args[0], posts, returnArgs, targetMethod.getReturnType());
        return returnArgs;
    }

    /**
     * @description: 执行前置处理
     * @author wangwl
     * @date: 2023/2/1 18:10
     */
    private void doPreHandler(Object args, Class<? extends IProcessor>[] pres) {
        for (Class<? extends IProcessor> clazz : pres) {
            //获取handler泛型中的类型，将入参转换为该类型
            doHandler(args, clazz);
        }
    }

    /**
     * @description: 执行后置处理
     * @author wangwl
     * @date: 2023/2/1 18:10
     */
    private void doPostHandler(Object args, Class<? extends IProcessor>[] posts, Object returnArgs, Class<?> returnType) {
        for (Class<? extends IProcessor> clazz : posts) {
            Class paramType = ResolvableType.forType(clazz.getGenericInterfaces()[0]).as(IProcessor.class).getGeneric(0).resolve();
            //返回值、返回类型不为空并且handler泛型中的类型和方法返回值类型一致
            if (returnArgs != null && returnType != null && returnType.getTypeName().equals(paramType.getTypeName())) {
                //使用方法的返回值进行后置处理
                doHandler(returnArgs, clazz);
            } else {
                //使用入参进行后置处理
                doHandler(args, clazz);
            }
        }
    }

    /**
     * @description: 执行Handler
     * @author wangwl
     * @date: 2023/2/1 18:10
     */
    private void doHandler(Object args, Class<? extends IProcessor> clazz) {
        Class resolve = ResolvableType.forType(clazz.getGenericInterfaces()[0]).as(IProcessor.class).getGeneric(0).resolve();
        Object params = getParams(args, resolve);
        IProcessor IProcessor = SpringUtil.getBean(clazz);
        IProcessor.process(params);
    }


    /**
     * @description: 参数转换
     * @author wangwl
     * @date: 2023/2/1 18:11
     */
    private Object getParams(Object args, Class paramType) {
        return KcBeanConverter.toBean(args, paramType);
    }

}
