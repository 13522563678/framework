package com.kcwl.ddd.domain.entity;

import com.kcwl.ddd.infrastructure.constants.GlobalConstant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author ckwl
 */
public class UserAgent {

    public static final String REQUEST_AGENT_HEADER_NAME ="x-user-agent";
    public static final String REQUEST_AGENT_CLIENT_FIELD_NAME = "x-agent-client";

    public static final String FIELD_PLATFORM="platform";
    public static final String FILED_TOKEN="token";
    public static final String FILED_SESSION_ID ="sessionId";
    public static final String FILED_PRODUCT="product";
    public static final String FILED_APPVERSION="version";
    public static final String FILED_REQUEST_DEPTH="depth";
    public static final String FIELD_USER_SSID = "x-user-ssid";
    public static final String FILED_OPERATING_SYSTEM ="operatingSystem";
    public static final String FILED_APP_ID="appId";
    public static final String FILED_APP_TYPE="appType";
    public static final String FILED_OS_TYPE="osType";


    public static final String FILED_REQUEST="request";

    public static final String AGENT_CLIENT_FEIGN = "feign";
    public static final String REQUEST_SERVER_TYPE = "server";

    private static final String SEMICOLON=";";
    private static final String EQUALSIGN="=";

    private Map<String, String> userAgent = new HashMap<String, String>();

    public UserAgent() {
    }

    public UserAgent(String strUserAgent) {
        parse(strUserAgent);
    }

    public String getProduct() {
        return userAgent.get(FILED_PRODUCT);
    }

    public void setProduct(String productType) {
        userAgent.put(FILED_PRODUCT, productType);
    }

    public String getPlatform() {
        return userAgent.get(FIELD_PLATFORM);
    }

    public void setPlatform(String platformNo) {
        userAgent.put(FIELD_PLATFORM, platformNo);
    }

    public String getAppVersion() {
        return userAgent.get(FILED_APPVERSION);
    }

    public void setAppVersion(String appVersion) {
        userAgent.put(FILED_APPVERSION, appVersion);
    }

    public String getToken() {
        return userAgent.get(FILED_TOKEN);
    }

    public void setToken(String token) {
        userAgent.put(FILED_TOKEN, token);
    }

    public String getSessionId() {
        return userAgent.get(FILED_SESSION_ID);
    }

    public void setSessionId(String sessionId) {
        userAgent.put(FILED_SESSION_ID, sessionId);
    }

    public String  getKcToken() {
        return userAgent.get(GlobalConstant.KC_TOKEN);
    }

    public void setKcToken(String kcToken) {
        userAgent.put(GlobalConstant.KC_TOKEN, kcToken);
    }

    public String  getKcTrace() {
        return userAgent.get(GlobalConstant.KC_TRACE);
    }

    public void setKcTrace(String kcTrace) {
        userAgent.put(GlobalConstant.KC_TRACE, kcTrace);
    }

    public String getRequestType() {
        return userAgent.get(FILED_REQUEST);
    }

    public void setRequestType(String requestType) {
        userAgent.put(FILED_REQUEST, requestType);
    }

    public String getValue(String name) {
        return userAgent.get(name);
    }

    public String getAppId() {
        return userAgent.get(FILED_APP_ID);
    }

    public String getOsType() {
        return userAgent.get(FILED_OS_TYPE);
    }

    public int getRequestDepth() {
        String depth = userAgent.get(FILED_REQUEST_DEPTH);
        return (depth !=null ) ? Integer.parseInt(depth) : 0;
    }

    public String getOperatingSystem() {
        return userAgent.get(FILED_OPERATING_SYSTEM);
    }

    public boolean isServerRequest() {
        String request = userAgent.get(FILED_REQUEST);
        return (request != null) && request.equals(REQUEST_SERVER_TYPE);
    }

    public UserAgent nextRequestUserAgent() {
        UserAgent requestUserAgent = new UserAgent();
        Map<String, String> data = new HashMap<>(this.userAgent);
        data.put(FILED_REQUEST_DEPTH, String.valueOf(this.getRequestDepth()+1));
        requestUserAgent.userAgent = data;
        return requestUserAgent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int agentCount = 0;
        Iterator<Map.Entry<String, String>> entries = userAgent.entrySet().iterator();

        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            if ( agentCount++ > 0 ) {
                sb.append(SEMICOLON);
            }
            sb.append(entry.getKey()).append(EQUALSIGN).append(entry.getValue());
        }
        return sb.toString();
    }

    private void parse(String strUserAgent) {
        if (strUserAgent != null) {
            String[] agentItems = strUserAgent.split(SEMICOLON);
            if (agentItems != null) {
                for (int i = 0; i < agentItems.length; i++) {
                    String[] agentItem = agentItems[i].split(EQUALSIGN);
                    if (agentItem.length == 2) {
                        userAgent.put(agentItem[0], agentItem[1]);
                    }
                }
            }
        }
    }
}
