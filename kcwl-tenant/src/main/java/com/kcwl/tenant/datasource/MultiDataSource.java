package com.kcwl.tenant.datasource;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author ckwl
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MultiDataSource {

    @AliasFor("datasource")
    String value() default "";

    @AliasFor("value")
    String datasource() default "";
}
