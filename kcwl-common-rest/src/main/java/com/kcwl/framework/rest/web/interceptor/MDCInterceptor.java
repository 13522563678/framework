package com.kcwl.framework.rest.web.interceptor;

import cn.hutool.core.util.StrUtil;
import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.kcwl.ddd.infrastructure.constants.GlobalConstant.*;

/**
 * Log 上下文 拦截器
 */
public class MDCInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        removeKeys(KC_TRACE, KC_TOKEN);

        String kcTrace = null;
        String kcToken = null;

        UserAgent requestUserAgent = SessionContext.getRequestUserAgent();
        if (requestUserAgent != null) {
            kcTrace = requestUserAgent.getKcTrace();
            kcToken = requestUserAgent.getKcToken();
        }

        kcTrace = StrUtil.isBlank(kcTrace) ? request.getHeader(KC_TRACE) : kcTrace;
        kcTrace = StrUtil.isBlank(kcTrace) ? request.getParameter(KC_TRACE) : kcTrace;

        kcToken = StrUtil.isBlank(kcToken) ? request.getHeader(KC_TOKEN) : kcToken;
        kcToken = StrUtil.isBlank(kcToken) ? request.getParameter(KC_TOKEN) : kcToken;


        if (StrUtil.isNotBlank(kcTrace)) {
            MDC.put(KC_TRACE, kcTrace);
        }

        if (StrUtil.isNotBlank(kcToken)) {
            MDC.put(KC_TOKEN, kcToken);
        }

        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        removeKeys(KC_TRACE, KC_TOKEN);
        super.afterCompletion(request, response, handler, ex);
    }

    // 从 MDC 移除
    private void removeKeys(String... keys) {
        if (keys.length > 0) {
            for (String key : keys) {
                MDC.remove(key);
            }
        }
    }

}
