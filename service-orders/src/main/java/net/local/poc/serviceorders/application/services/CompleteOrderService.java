package net.local.poc.serviceorders.application.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import net.local.poc.serviceorders.application.ports.gateways.PurchaseOrderGateway;
import net.local.poc.serviceorders.application.ports.publisher.EventDispacher;
import net.local.poc.serviceorders.application.ports.usecases.CompleteOrderUC;
import net.local.poc.serviceorders.domain.events.OrderCompletedEvent;

@Service
public class CompleteOrderService implements CompleteOrderUC {
    
    private final EventDispacher dispatcher;
    private final PurchaseOrderGateway gateway;

    public CompleteOrderService(EventDispacher dispatcher, PurchaseOrderGateway gateway) {
        this.dispatcher = dispatcher;
        this.gateway = gateway;
    }

    @Override
    public void execute(UUID orderId) {
        // load puchase order from database
        var order = gateway.load(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        // change the status
        var completedOrder = order.complete();
        // persist new status
        gateway.update(completedOrder);
        // dispatch event
        dispatcher.dispatch(new OrderCompletedEvent(completedOrder));
    }
}
