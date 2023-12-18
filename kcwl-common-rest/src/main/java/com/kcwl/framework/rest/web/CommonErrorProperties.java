package com.kcwl.framework.rest.web;

import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.constants.GlobalConstant;
import com.kcwl.ddd.infrastructure.session.SessionContext;
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
    private static final Integer APPEND_TRACE_ALL_ERROR = 1;
    private static final Integer APPEND_TRACE_SPECIFIC_ERROR = 2;
    private Integer appendTraceMode = 0;
    ConcurrentHashMap<String, String> sysErrorMessageMap = new ConcurrentHashMap<String, String>();
    CopyOnWriteArraySet<String> appendTraceErrorSet = new CopyOnWriteArraySet<String>();

    public String decoratorMessageWithTraceId(String code, String defaultMessage) {
        String sysErrorCode = getSysErrorCode(code);
        String sysErrorMessage = sysErrorMessageMap.get(sysErrorCode);
        if  ( sysErrorMessage == null ) {
            sysErrorMessage = defaultMessage;
        }
        if ( isAppendTraceId(sysErrorCode) ) {
            UserAgent userAgent = SessionContext.getRequestUserAgent();
            String kcTrace = null;
            if ( userAgent != null ) {
                kcTrace = userAgent.getKcTrace();
            }
            if ( kcTrace != null ) {
                StringBuilder sb = new StringBuilder();
                sb.append(sysErrorMessage).append("（").append(kcTrace).append("）");
                return sb.toString();
            }
        }
        return sysErrorMessage;
    }

    private boolean isAppendTraceId(String code) {
        String clientType = SessionContext.getRequestClient();
        if ( UserAgent.AGENT_CLIENT_FEIGN.equals(clientType) ) {
            return false;
        }
        if ( APPEND_TRACE_ALL_ERROR.equals(appendTraceMode) ) {
            return true;
        }
        return APPEND_TRACE_SPECIFIC_ERROR.equals(appendTraceMode) && appendTraceErrorSet.contains(code);
    }

    public static String getSysErrorCode(String code){
        int codeLen = code.length();
        if ( codeLen > GlobalConstant.BIZ_ERROR_CODE_LENGHT ) {
            return code.substring(codeLen-GlobalConstant.BIZ_ERROR_CODE_LENGHT, codeLen);
        }
        return code;
    }
}
