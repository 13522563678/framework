package com.kcwl.ddd.domain.event;

public interface IDomainEventPublisher {
    void publish(DomainEvent event);
}
