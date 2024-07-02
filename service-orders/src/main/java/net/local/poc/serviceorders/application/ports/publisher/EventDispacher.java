package net.local.poc.serviceorders.application.ports.publisher;

import net.local.poc.serviceorders.domain.events.AbstractDomainEvent;

@SuppressWarnings("rawtypes")
public interface EventDispacher {
    void dispatch(AbstractDomainEvent event);
}
