package com.kcwl.validation.validator;

import com.kcwl.framework.utils.CollectionUtil;
import com.kcwl.framework.utils.StringUtil;
import com.kcwl.validation.LeastOneNotEmpty;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 至少一个参数不为空
 * <p>
 * 不同类型空定义
 * - String: Not Blank
 * - Collection: Not Empty
 * - 其他: Not Null
 */
@Slf4j
public class LeastOneNotEmptyValidator implements ConstraintValidator<LeastOneNotEmpty, Object> {

    private LeastOneNotEmpty annotation;

    @Override
    public void initialize(LeastOneNotEmpty constraintAnnotation) {
        annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

        String[] fields = annotation.value();

        if (fields.length < 1) {
            throw new ValidationException("至少指定一个参数");
        }

        for (String field : fields) {
            if (StringUtil.isBlank(field)) {
                throw new ValidationException("指定参数不得为空");
            }
        }

        try {
            for (String field : fields) {
                PropertyDescriptor prop = new PropertyDescriptor(field, o.getClass());
                Method readMethod = prop.getReadMethod();
                if (readMethod == null) {
                    throw new ValidationException("指定参数需实现标准 getter 方法");
                }
                Object fieldValue = readMethod.invoke(o);

                if (fieldValue == null) {
                    continue;
                }

                if (fieldValue instanceof CharSequence) {
                    if (StringUtil.isNotBlank((CharSequence) fieldValue)) {
                        return true;
                    }
                } else if (fieldValue instanceof Collection) {
                    if (!CollectionUtil.isEmpty((Collection<?>) fieldValue)) {
                        return true;
                    }
                } else {
                    return true;
                }

            }
            return false;
        } catch (IntrospectionException e) {
            throw new ValidationException("请确认指定参数存在, 且实现标准 getter 方法");
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.error("字段校验 LeastOneNotNullOrBlankOrEmptyValidator 未知错误", e);
            return false;
        }

    }

}