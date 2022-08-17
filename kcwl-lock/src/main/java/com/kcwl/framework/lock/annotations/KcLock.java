package com.kcwl.framework.lock.annotations;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description 分布式锁注解，默认一分钟，等待5秒
 * @Author wl
 * @Date 2022/7/21 17:58
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface KcLock {

    String key();

    String prefix() default "";

    String message() default "";

    int waitingTime() default 60;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    int leaseTime() default 5;

}
