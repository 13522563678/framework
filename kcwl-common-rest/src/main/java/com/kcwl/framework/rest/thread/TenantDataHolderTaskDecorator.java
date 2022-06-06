package com.kcwl.framework.rest.thread;

import com.kcwl.tenant.TenantDataHolder;
import org.springframework.core.task.TaskDecorator;

/**
 * 平台码 线程池 任务装饰器
 */
public class TenantDataHolderTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        String tenant = TenantDataHolder.get();
        return () -> {
            String holder = TenantDataHolder.get();
            try {
                TenantDataHolder.set(tenant);
                runnable.run();
            } finally {
                if (holder != null) {
                    TenantDataHolder.set(holder);
                } else {
                    TenantDataHolder.remove();
                }
            }
        };
    }

}
