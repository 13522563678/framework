package com.kcwl.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

/**
 * @author 姚华成
 * @date 2018-01-30
 */
@Slf4j
public class ClassUtil extends ClassUtils {

    /**
     * 尝试加载类
     */
    public static Class<?> tryLoadClass(String name) {
        try {
            return ClassUtils.forName(name, null);
        } catch (Exception e) {
            log.debug("manual load class failed, class_name: {}", name);
            return null;
        }
    }

}
