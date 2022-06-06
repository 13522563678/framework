package com.kcwl.framework.rest.thread;

import cn.hutool.core.collection.CollUtil;
import org.springframework.core.task.TaskDecorator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 多个 TaskDecorator
 */
public class MultiTaskDecorator implements TaskDecorator {

    private final List<TaskDecorator> taskDecoratorList = new ArrayList<>();

    public MultiTaskDecorator(TaskDecorator... taskDecorators) {
        if (taskDecorators == null || taskDecorators.length == 0) {
            throw new IllegalArgumentException("");
        }
        taskDecoratorList.addAll(Arrays.asList(taskDecorators));
    }

    public static MultiTaskDecorator of(TaskDecorator... taskDecorators) {
        return new MultiTaskDecorator(taskDecorators);
    }

    @Override
    public Runnable decorate(Runnable runnable) {
        if (CollUtil.isNotEmpty(taskDecoratorList)) {
            for (TaskDecorator taskDecorator : taskDecoratorList) {
                runnable = taskDecorator.decorate(runnable);
            }
        }
        return runnable;
    }

}
