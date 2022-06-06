package com.kcwl.framework.grayscale.constant;

/**
 * @author: renyp
 * @description: 关于灰度配置的一些常量
 * @date: created in 10:58 2021/8/31
 * @modify by:
 */

public class GrayConstant {

    /**
     * 请求头的灰度标记key
     */
    public static final String GRAY_REQUEST_HEADER_KEY = "x-gray-mark";
    /**
     * 灰度环境中service的host特征字符（如：canary）
     */
    public static final String GRAY_RULE_HOSTMARK = "kcwl.grayflow.rule.grayHostMark";
    /**
     * 用户请求Cookie中的灰度标记 key
     */
    public static final String GRAY_RULE_COOKIE_KEY = "kcwl.grayflow.rule.grayCookieKey";
    /**
     * 用户请求Cookie中的灰度标记 value
     */
    public static final String GRAY_RULE_COOKIE_VALUE = "kcwl.grayflow.rule.grayCookieValue";
    /**
     * 参与灰度测试的服务列表
     */
    public static final String GRAY_RULE_GRAYSERVERLIST = "kcwl.grayflow.rule.grayServersList";
    /**
     * 灰度策略配置key的前缀
     */
    public static final String GRAY_RULE_PROPERTIES_PREFIX = "kcwl.grayflow.rule.";
    /**
     * 各个服务配置其他服务地址key特征字符
     */
    public static final String RIBBON_LISTOFSERVERS = "ribbon.listOfServers";

    /**
     * 灰度策略配置 当前所处的灰度阶段
     * 0：禁用所有灰度服务；
     * 1：灰度服务参与内测；
     * 2：灰度服务处理内测和公测用户请求；
     * 3：存在灰度测试服务，根据权重（grayFlowRatio指定的）决定是否进入灰度环境
     * 4：stable环境升级的过渡阶段，所有请求进入灰度环境
     */
    public static final String GRAY_RULE_LEVEL = "kcwl.grayflow.level";

    /**
     * 灰度测试覆盖阶段
     */
    public static final String GRAY_RULE_GRAY_TEST_SCOPE = "kcwl.grayflow.testScope";

}
