package com.kcwl.ddd.domain.event;

public interface IExceptionEventPublisher {
    void publish(ExceptionEvent event);
}
