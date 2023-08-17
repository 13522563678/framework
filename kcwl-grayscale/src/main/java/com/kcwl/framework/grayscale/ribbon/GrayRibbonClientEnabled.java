package com.kcwl.framework.grayscale.ribbon;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * <p>
 * 控制 自定义RibbonClient组件 的注入条件 组合注解
 * </p>
 *
 * @author renyp
 * @since 2023/8/15 14:40
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(GrayRibbonClientEnabled.OnRibbonAndGrayEnabledCondition.class)
public @interface GrayRibbonClientEnabled {

    class OnRibbonAndGrayEnabledCondition extends AllNestedConditions {

        OnRibbonAndGrayEnabledCondition() {
            super(ConfigurationPhase.REGISTER_BEAN);
        }

        @ConditionalOnClass(GrayDynamicConfigurationServerList.class)
        @ConditionalOnBean(SpringClientFactory.class)
        @ConditionalOnProperty(value = "kcwl.grayflow.enabled", havingValue = "true", matchIfMissing = false)
        static class Defaults {

        }


    }
}
