package com.kcwl.framework.rest.web;

import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import com.kcwl.framework.rest.helper.RequestUserAgentHelper;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author ckwl
 */
@Data
@ConfigurationProperties(prefix = "kcwl.common.error")
public class CommonErrorProperties {
    private boolean appendTraceId = false;
    ConcurrentHashMap<String, String> sysErrorMessageMap = new ConcurrentHashMap<String, String>();
    CopyOnWriteArraySet<String> appendTraceErrorSet = new CopyOnWriteArraySet<String>();

    public String decoratorMessageWithTraceId(String code, String defaultMessage) {
        String sysErrorMessage = sysErrorMessage = sysErrorMessageMap.get(code);
        if  ( sysErrorMessage == null ) {
            sysErrorMessage = defaultMessage;
        }
        if ( appendTraceId && appendTraceErrorSet.contains(code) ) {
            UserAgent userAgent = SessionContext.getRequestUserAgent();
            String kcTrace = null;
            if ( userAgent != null ) {
                kcTrace = userAgent.getKcTrace();
            }
            if ( kcTrace != null ) {
                StringBuilder sb = new StringBuilder();
                sb.append(sysErrorMessage).append("(").append(kcTrace).append(")");
                return sb.toString();
            }
        }
        return sysErrorMessage;
    }
}
