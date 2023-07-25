package com.kcwl.framework.session;

import com.kcwl.ddd.infrastructure.session.SessionData;

public interface ISessionEventListener {
    public void onSessionRenew(SessionData sessionData, int timeout);
}
