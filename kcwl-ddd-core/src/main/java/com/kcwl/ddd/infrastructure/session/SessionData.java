package com.kcwl.ddd.infrastructure.session;

import java.io.Serializable;

public abstract class SessionData implements Serializable {
    private static final long serialVersionUID = 1L;
    public abstract String getSessionId();
    public Long getUserId() { return 0L;}
    public String getUserName() { return "";}
    public Integer getUserType() { return -1;}
    public String getSessionSalt() { return "";}
    public boolean isTestUser() { return false;}
    public boolean isSuperAdministrator() { return false;}
    public String getTenantId() { return null;}
}
