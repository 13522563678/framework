package com.kcwl.framework.process.annotation;

import com.kcwl.framework.process.KcProcessInterface;

import java.lang.annotation.*;

/**
 * @description: 处理器注解
 * @author wangwl
 * @date: 2023/2/2 18:21
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KcProcessor {

    Class<? extends KcProcessInterface>[] before() default {};
    Class<? extends KcProcessInterface>[] after() default {};

}
