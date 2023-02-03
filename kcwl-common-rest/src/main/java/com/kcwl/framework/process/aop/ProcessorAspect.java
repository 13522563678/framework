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
        KcProcessor annotation = AnnotationUtils.findAnnotation(targetMethod, KcProcessor.class);
        //构建校验参数
        Class<? extends IProcessor>[] beforeProcessors = annotation.before();
        doBeforeProcessor(args[0], beforeProcessors);

        Object returnArgs = joinPoint.proceed();
        Class<? extends IProcessor>[] afterProcessors = annotation.after();

        doAfterProcessor(args[0], afterProcessors, returnArgs, targetMethod.getReturnType());
        return returnArgs;
    }

    /**
     * @description: 执行前置处理
     * @author wangwl
     * @date: 2023/2/1 18:10
     */
    private void doBeforeProcessor(Object args, Class<? extends IProcessor>[] beforeProcessors) {
        for (Class<? extends IProcessor> processor : beforeProcessors) {
            //获取processor泛型中的类型，将入参转换为该类型
            doProcessor(args, processor);
        }
    }

    /**
     * @description: 执行后置处理
     * @author wangwl
     * @date: 2023/2/1 18:10
     */
    private void doAfterProcessor(Object args, Class<? extends IProcessor>[] afterProcessors, Object returnArgs, Class<?> returnType) {
        for (Class<? extends IProcessor> processor : afterProcessors) {
            Class paramType = ResolvableType.forType(processor.getGenericInterfaces()[0]).as(IProcessor.class).getGeneric(0).resolve();
            //返回值、返回类型不为空并且processor泛型中的类型和方法返回值类型一致
            if (returnArgs != null && returnType != null && returnType.getTypeName().equals(paramType.getTypeName())) {
                //使用方法的返回值进行后置处理
                doProcessor(returnArgs, processor);
            } else {
                //使用入参进行后置处理
                doProcessor(args, processor);
            }
        }
    }

    /**
     * @description: 执行processor
     * @author wangwl
     * @date: 2023/2/1 18:10
     */
    private void doProcessor(Object args, Class<? extends IProcessor> processor) {
        Class resolve = ResolvableType.forType(processor.getGenericInterfaces()[0]).as(IProcessor.class).getGeneric(0).resolve();
        Object params = getParams(args, resolve);
        IProcessor IProcessor = SpringUtil.getBean(processor);
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
