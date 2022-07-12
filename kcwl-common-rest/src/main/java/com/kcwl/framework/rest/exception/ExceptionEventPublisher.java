package com.kcwl.framework.rest.exception;

import com.kcwl.ddd.domain.event.ExceptionEvent;
import com.kcwl.ddd.domain.event.IExceptionEventListener;
import com.kcwl.ddd.domain.event.IExceptionEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExceptionEventPublisher implements IExceptionEventPublisher {

    List<IExceptionEventListener> eventListenerList;

    @Override
    public void publish(ExceptionEvent event) {
        if ( eventListenerList != null ) {
            for ( IExceptionEventListener listener : eventListenerList ) {
                listener.onEvent(event);
            }
        }
    }

    @Autowired(required = false)
    public void setExceptionEventListener(List<IExceptionEventListener> eventListenerList) {
        this.eventListenerList = eventListenerList;
    }

}
