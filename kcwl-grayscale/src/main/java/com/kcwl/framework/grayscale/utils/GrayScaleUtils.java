package com.kcwl.framework.grayscale.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.kcwl.framework.grayscale.constant.GrayConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: renyp
 * @description: 关于灰度发布相关工具
 * @date: created in 17:50 2021/9/24
 * @modify by:
 */

@Slf4j
public class GrayScaleUtils {

    /**
     * 是否处于灰度测试阶段
     *
     * @return
     */
    public static boolean isGrayPublishSpan() {
        try {
            String grayLevel = SpringUtil.getBean(Environment.class).getProperty(GrayConstant.GRAY_RULE_LEVEL);
            String testScope = SpringUtil.getBean(Environment.class).getProperty(GrayConstant.GRAY_RULE_GRAY_TEST_SCOPE);
            if (!StringUtils.isEmpty(testScope) && testScope.contains(grayLevel)) {
                return true;
            }
        } catch (Exception ex) {
            log.error("识别灰度发布阶段失败, cause by: ", ex);
        }
        return false;
    }
}
