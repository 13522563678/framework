package com.kcwl.ddd.infrastructure.session;

import com.kcwl.ddd.domain.entity.SimpleThreadLocal;
import com.kcwl.ddd.domain.entity.UserAgent;

/**
 * @author 姚华成
 * @date 2018-01-02
 */
public class SessionContext {

    private static SimpleThreadLocal<SessionData> sessionDataLocal = new SimpleThreadLocal<>();
    private static SimpleThreadLocal<Integer> apiVersionThreadLocal = new SimpleThreadLocal<Integer>();
    private static SimpleThreadLocal<UserAgent> userAgentThreadLocal = new SimpleThreadLocal<UserAgent>();
    private static SimpleThreadLocal<String> requestClient = new SimpleThreadLocal<String>();

    private SessionContext() {
    }

    public static SessionData getSessionData() {
        return sessionDataLocal.get();
    }

    public static void setSessionData(SessionData sessionData) {
        sessionDataLocal.set(sessionData);
    }

    public static Integer getApiVersion() {
        return apiVersionThreadLocal.get();
    }

    public static void setApiVersion(Integer apiVersion) {
        apiVersionThreadLocal.set(apiVersion);
    }

    public static UserAgent getRequestUserAgent() {
        return userAgentThreadLocal.get();
    }
    public static void setRequestUserAgent(UserAgent requestUserAgent) {
        userAgentThreadLocal.set(requestUserAgent);
    }

    public static void setRequestClient(String clientType) {
        requestClient.set(clientType);
    }

    public static String getRequestClient() {
        return requestClient.get();
    }
}
