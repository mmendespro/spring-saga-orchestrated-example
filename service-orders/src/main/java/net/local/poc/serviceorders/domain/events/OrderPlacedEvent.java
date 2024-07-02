package net.local.poc.serviceorders.domain.events;

import net.local.poc.serviceorders.domain.entities.PurchaseOrder;

public class OrderPlacedEvent extends AbstractDomainEvent<PurchaseOrder> {
    
    private final PurchaseOrder order;

    public OrderPlacedEvent(PurchaseOrder order) {
        this.order = order;
    }

    @Override
    public PurchaseOrder getContent() {
        return this.order;
    }

    @Override
    public String getEventName() {
        return this.getClass().getSimpleName();
    }

}
