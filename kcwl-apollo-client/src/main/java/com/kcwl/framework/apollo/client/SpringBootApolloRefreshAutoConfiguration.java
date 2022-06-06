package com.kcwl.framework.apollo.client;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnClass(EnvironmentChangeEvent.class)
public class SpringBootApolloRefreshAutoConfiguration {

  @Bean
  public RefreshApolloConfig refreshConfig() {
    return new RefreshApolloConfig();
  }

  public static class RefreshApolloConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @ApolloConfigChangeListener({"application", "common", "dict"})
    public void onChange(ConfigChangeEvent changeEvent) {
      log.warn("配置更改，全部刷新！namespace={},changedKeys={}", changeEvent.getNamespace(), changeEvent.changedKeys());
      applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
      this.applicationContext = applicationContext;
    }
  }

}
