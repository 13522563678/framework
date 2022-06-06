package com.kcwl.ddd.domain.event;

public interface IDomainEventHandler<T extends DomainEvent>  {
    void onEvent(T event);
}
