package com.kcwl.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 使用 Spring 环境
 *
 * 若要限定上下文范围，自定义 BeanFactory 并创建 SpELComponent 使用
 */
@Slf4j
@Component
public class SpELUtil implements BeanFactoryAware {

    private static SpELComponent holder;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        holder = new SpELComponent(beanFactory);
    }

    // SpEL evaluate
    public static <T> T eval(String expression) {
        if (!initialized()) {
            return null;
        }
        return holder.eval(expression);
    }

    // SpEL evaluate return Optional
    public static <T> Optional<T> evalOptional(String expression) {
        if (!initialized()) {
            return Optional.empty();
        }
        return holder.evalOptional(expression);
    }

    // 是否初始化完毕
    private static boolean initialized() {
        if (holder == null) {
            log.warn("not initialized", new Throwable());
            return false;
        }
        return true;
    }

}
