package com.kcwl.framework.auth;

import com.kcwl.ddd.infrastructure.session.SessionData;

/**
 * @author ckwl
 */
public interface IKcSsoAuth {
    /**
     * sso登录验证
     * @return
     */
    SessionData ssoLogin();
}
