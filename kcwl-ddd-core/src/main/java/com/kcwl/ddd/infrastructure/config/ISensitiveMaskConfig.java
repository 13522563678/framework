package com.kcwl.ddd.infrastructure.config;

import java.util.List;
import java.util.Set;

/**
 * @author ckwl
 */
public interface ISensitiveMaskConfig {
    /**
     * 获取需要屏蔽的字段
     * @return 返回屏蔽字段列表
     */
    Set<String> getMaskFieldList();

    /**
     * 是否开启屏蔽字段
     * @return 如果设置了开启，返回true；否则返回false
     */
    boolean isEnableMaskField();
}
