package com.kcwl.ddd.infrastructure.api;

/**
 * @author ckwl
 */
public interface IErrorPromptDecorator {
    /**
     * 获取错误提示语
     * @param code 错误码
     * @param product 端类型
     * @return 返回错误提示语,如果没有定义错误提示语，返回null或空字符串；
     */
    String getErrorPrompt(String code, String product);
}
