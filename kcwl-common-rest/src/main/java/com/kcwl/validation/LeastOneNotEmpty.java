package com.kcwl.validation;

import com.kcwl.validation.validator.LeastOneNotEmptyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 至少一个参数不为空
 * <p>
 * 不同类型空定义
 * - String: Not Blank
 * - Collection: Not Empty
 * - 其他: Not Null
 * <p>
 * 使用条件
 * - 至少指定一个字段
 * - 指定字段实现标准 getter
 */
@Documented
@Constraint(validatedBy = LeastOneNotEmptyValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(LeastOneNotEmpty.List.class)
public @interface LeastOneNotEmpty {

    String[] value() default {};

    String message() default "字段格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        LeastOneNotEmpty[] value();
    }
}

