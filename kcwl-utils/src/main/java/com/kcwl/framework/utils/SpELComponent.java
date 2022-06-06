package com.kcwl.framework.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * Spring Expression Language Tool
 *
 * 依赖 Spring-Expression
 */
@Slf4j
public class SpELComponent {

    @Getter
    private final ConfigurableBeanFactory beanFactory;
    private final BeanExpressionContext evalContext;
    private final StandardBeanExpressionResolver expressionResolver;

    public SpELComponent(BeanFactory beanFactory) {
        this(beanFactory, null);
    }

    public SpELComponent(BeanFactory beanFactory, @Nullable StandardBeanExpressionResolver expressionResolver) {

        if (beanFactory instanceof ConfigurableBeanFactory) {
            this.beanFactory = (ConfigurableBeanFactory) beanFactory;
        } else {
            this.beanFactory = new DefaultListableBeanFactory(beanFactory);
        }

        evalContext = new BeanExpressionContext(this.beanFactory, null);

        if (expressionResolver != null) {
            this.expressionResolver = expressionResolver;
        } else {
            this.expressionResolver = new StandardBeanExpressionResolver();
        }
    }

    // SpEL evaluate
    public <T> T eval(String expression) {
        return (T) this.expressionResolver.evaluate(expression, evalContext);
    }

    // SpEL evaluate return Optional
    public <T> Optional<T> evalOptional(String expression) {
        try {
            return Optional.<T>ofNullable((T) eval(expression));
        } catch (Exception ignored) {
        }
        return Optional.empty();
    }

}
