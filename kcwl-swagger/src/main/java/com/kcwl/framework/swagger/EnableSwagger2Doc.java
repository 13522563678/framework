package com.kcwl.framework.swagger;

import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.annotation.*;

/**
 * @author 姚华成
 * @date 2017-12-27
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableSwagger2
@Import({SwaggerConfiguration.class})
public @interface EnableSwagger2Doc {
}
