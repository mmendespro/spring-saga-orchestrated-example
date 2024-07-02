package net.local.poc.serviceorders.infrastructure.publishers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.local.poc.serviceorders.application.ports.publisher.EventDispacher;
import net.local.poc.serviceorders.domain.events.AbstractDomainEvent;

@Slf4j
@Component
@SuppressWarnings({"rawtypes"})
public class ApplicationEventDispatcher implements EventDispacher {
    
    private final ApplicationEventPublisher publisher;

    public ApplicationEventDispatcher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void dispatch(AbstractDomainEvent event) {
        log.info("Dispatching event {}", event.getEventName());
        publisher.publishEvent(event);
    }
}
