package com.kcwl.framework.datasource.multi;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * @author 姚华成
 * @date 2017-12-19
 */
public class MultiDataSourceConfigurer implements BeanDefinitionRegistryPostProcessor {

    private static final String MAPPER_FACTORY_BEAN_NAME = "org.mybatis.spring.mapper.MapperFactoryBean";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        String[] names = registry.getBeanDefinitionNames();
        GenericBeanDefinition definition;
        for (String name : names) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(name);
            if (beanDefinition instanceof GenericBeanDefinition) {
                definition = (GenericBeanDefinition) beanDefinition;
                System.out.println(name + "===" + definition.getBeanClassName());
                if (MAPPER_FACTORY_BEAN_NAME.equals(definition.getBeanClassName())) {
                    definition.setBeanClass(MultiDataSourceMapperFactoryBean.class);
               }
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // 此处不需要处理
    }
}
