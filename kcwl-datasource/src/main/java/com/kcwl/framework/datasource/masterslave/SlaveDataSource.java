package com.kcwl.framework.datasource.masterslave;

import java.lang.annotation.*;

/**
 * 用于指定使用Slave数据源
 *
 * @author 姚华成
 * @date 2017-12-15
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SlaveDataSource {
}