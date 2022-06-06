package com.kcwl.framework.kcwlsentinelclient.Constant;

/**
 * @author: renyp
 * @description: 涉及常量
 * @date: created in 14:06 2021/10/29
 * @modify by:
 */
public class CommonConstant {
    /**
     * 获取项目名对应的key
     */
    public static String APPLICATION_KEY = "spring.application.name";
    /**
     * 流控规则占位前缀
     */
    public static String FLOW_RULES_PREFIX = "{spring.application.name}";

    /**
     * sentinel配置流控规则的首个数据源key
     */
    public static String DS = "ds";

    /**
     * 获取通用sentinel flowRules 的key
     */
    public static String FLOW_RULES_DS_COMMON_KEY = "spring.cloud.sentinel.datasource.ds.apollo.flowRulesKey";
}
