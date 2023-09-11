package com.kcwl.framework.utils;

import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.session.SessionContext;

/**
 * @author ckwl
 */
public class RequestUtil {

    public static boolean isInternalRequest() {
        return UserAgent.AGENT_CLIENT_FEIGN.equals(SessionContext.getRequestClient());
    }
}
