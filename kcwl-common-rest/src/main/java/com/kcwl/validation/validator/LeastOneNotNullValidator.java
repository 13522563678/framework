package com.kcwl.validation.validator;

import com.kcwl.framework.utils.StringUtil;
import com.kcwl.validation.LeastOneNotNull;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 至少一个参数不为空
 */
@Slf4j
public class LeastOneNotNullValidator implements ConstraintValidator<LeastOneNotNull, Object> {

    private LeastOneNotNull annotation;

    @Override
    public void initialize(LeastOneNotNull constraintAnnotation) {
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
                if (fieldValue != null) {
                    return true;
                }
            }
            return false;
        } catch (IntrospectionException e) {
            throw new ValidationException("请确认指定参数存在, 且实现标准 getter 方法");
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.error("字段校验 LeastOneNotNullValidator 未知错误", e);
            return false;
        }

    }

}