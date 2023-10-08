package com.kcwl.framework.rest.web;

import com.kcwl.ddd.infrastructure.config.ISensitiveMaskConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author ckwl
 */
@Component
public class SensitiveMaskConfig implements ISensitiveMaskConfig {

    @Value("${kcwl.common.sensitive.mask.fieldSet:}")
    CopyOnWriteArraySet<String> maskFields;

    @Value("${kcwl.common.sensitive.mask.enableMaskLog}")
    boolean enableMaskField = false;

    /**
     * 获取需要屏蔽的字段
     *
     * @return 返回屏蔽字段列表
     */
    @Override
    public Set<String> getMaskFieldList() {
        return maskFields;
    }

    /**
     * 是否开启屏蔽字段
     *
     * @return 如果设置了开启，返回true；否则返回false
     */
    @Override
    public boolean isEnableMaskField() {
        return enableMaskField;
    }
}
