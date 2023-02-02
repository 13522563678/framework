package com.kcwl.framework.process.annotation;

import com.kcwl.framework.process.IProcessor;

import java.lang.annotation.*;

/**
 * @author wangwl
 * @description: 处理器注解
 * @date: 2023/2/2 18:21
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KcProcessor {

    /**
     * @description: 前置处理
     * @author wangwl
     * @date: 2023/2/2 18:45
     */
    Class<? extends IProcessor>[] before() default {};

    /**
     * @description: 后置处理
     * @author wangwl
     * @date: 2023/2/2 18:45
     */
    Class<? extends IProcessor>[] after() default {};

}
