package net.local.poc.serviceorders.application.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import net.local.poc.serviceorders.application.ports.gateways.PurchaseOrderGateway;
import net.local.poc.serviceorders.application.ports.publisher.EventDispacher;
import net.local.poc.serviceorders.application.ports.usecases.CancelOrderUC;
import net.local.poc.serviceorders.domain.events.OrderCanceledEvent;

@Service
public class CancelOrderService implements CancelOrderUC {

    private final EventDispacher dispatcher;
    private final PurchaseOrderGateway gateway;

    public CancelOrderService(PurchaseOrderGateway gateway, EventDispacher dispatcher) {
        this.dispatcher = dispatcher;
        this.gateway = gateway;
    }

    @Override
    public void execute(UUID orderId) {
        // load puchase order from database
        var order = gateway.load(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        // change the status
        var canceledOrder = order.cancel();
        // persist new status
        gateway.update(canceledOrder);
        // dispatch event
        dispatcher.dispatch(new OrderCanceledEvent(canceledOrder));
    }
    
}
