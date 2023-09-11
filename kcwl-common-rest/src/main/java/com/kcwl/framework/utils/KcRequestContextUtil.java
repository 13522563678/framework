package com.kcwl.framework.utils;

import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.session.SessionContext;

/**
 * @author ckwl
 */
public class KcRequestContextUtil {

    /**
     * 是否为内部调用的请求
     * @return 如果是内部请求，返回true
     */
    public static boolean isInternalRequest() {
        return UserAgent.AGENT_CLIENT_FEIGN.equals(SessionContext.getRequestClient());
    }
}
