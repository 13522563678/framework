package com.kcwl.framework.rest;

import com.kcwl.framework.rest.feign.FormEncoderExtend;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@ConfigurationProperties("feign")
public class FeignFormConfiguration {
    private Integer connectionTimeout = 5000;
    private Integer readTimeout = 10000;
    private Integer retry = 0;
    private Long period = 100L;
    private Long maxPeriod = 1000L;

    @Bean
    @Scope("prototype")
    public Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        try {
            return new FormEncoderExtend(new SpringEncoder(messageConverters));
        } catch (Exception e) {
            return new FormEncoder(new SpringEncoder(messageConverters));
        }
    }


    public Request.Options feignOptions() {
        return new Request.Options(connectionTimeout, readTimeout);
    }

    public Retryer feignRetryer() {
        if (retry > 0) {
            // the first call is also one attempt try
            return new Retryer.Default(period, maxPeriod, retry + 1);
        } else {
            return Retryer.NEVER_RETRY;
        }
    }

    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public Long getMaxPeriod() {
        return maxPeriod;
    }

    public void setMaxPeriod(Long maxPeriod) {
        this.maxPeriod = maxPeriod;
    }
}
