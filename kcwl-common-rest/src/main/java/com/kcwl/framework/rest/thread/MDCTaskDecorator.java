package com.kcwl.framework.rest.thread;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import static com.kcwl.ddd.infrastructure.constants.GlobalConstant.KC_TOKEN;
import static com.kcwl.ddd.infrastructure.constants.GlobalConstant.KC_TRACE;

/**
 * MDC 线程池 任务装饰器
 */
public class MDCTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        String kcToken = MDC.get(KC_TOKEN);
        String kcTrace = MDC.get(KC_TRACE);
        return () -> {
            try {
                MDC.put(KC_TOKEN, kcToken);
                MDC.put(KC_TRACE, kcTrace);
                runnable.run();
            } finally {
                MDC.remove(KC_TOKEN);
                MDC.remove(KC_TRACE);
            }
        };
    }

}
