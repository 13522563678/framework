package com.kcwl.framework.datasource.multi;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author 姚华成
 * @date 2017-12-15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface MultiDataSource {
    /**
     * 定义使用哪个数据源，如果为空或者没有定义则使用默认数据源
     * <p>value与datasource只能定义一个，两个都定义直接报错。
     */
    @AliasFor("datasource")
    String value() default "";

    @AliasFor("value")
    String datasource() default "";
}
