package net.local.poc.serviceorders.infrastructure.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.local.poc.serviceorders.domain.events.AbstractDomainEvent;

@Slf4j
@Component
public class LogListener {
    
    @Async
    @EventListener
    void onDomainEvent(@SuppressWarnings("rawtypes") AbstractDomainEvent event) {
        log.info("Event dispatched: {}", event.getEventName());
    }
}
