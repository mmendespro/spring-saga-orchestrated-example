package net.local.poc.serviceorders.domain.events;

public abstract class AbstractDomainEvent<T> {
    
    public abstract T getContent();
    public abstract String getEventName();
}
