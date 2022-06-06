package com.kcwl.validation;

import com.kcwl.validation.validator.LeastOneNotNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 至少一个参数不为空
 * <p>
 * 使用条件
 * - 至少指定一个字段
 * - 指定字段实现标准 getter
 */
@Documented
@Constraint(validatedBy = LeastOneNotNullValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(LeastOneNotNull.List.class)
public @interface LeastOneNotNull {

    String[] value() default {};

    String message() default "字段格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        LeastOneNotNull[] value();
    }
}

