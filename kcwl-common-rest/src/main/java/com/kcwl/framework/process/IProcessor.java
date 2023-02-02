package com.kcwl.framework.process;

import org.springframework.stereotype.Component;

/**
 * @description:
 * @param:
 * @author wangwl
 * @date: 2023/2/2 18:18
 */
@Component
@FunctionalInterface
public interface IProcessor<T> {

    /**
     * @description: 执行处理 
     * @param:  t 处理器入参类型
     * @author wangwl
     * @date: 2023/2/2 18:22
     */
    void process(T t) ;

}
