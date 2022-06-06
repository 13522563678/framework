package com.kcwl.framework.datasource.multi;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启多数据源支持。<p>
 * 目前对于非主数据源，无法启用事务
 *
 * @author 姚华成
 * @date 2017-12-18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MultiDataSourceConfigurer.class)
@EnableConfigurationProperties(MultiDataSourceProperties.class)
public @interface EnableMultiDataSource {
}
