package com.kcwl.ddd.domain.event;

public interface IExceptionEventListener {
    void onEvent(ExceptionEvent event);
}
