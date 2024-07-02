package net.local.poc.serviceorders.application.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import net.local.poc.serviceorders.application.ports.gateways.PurchaseOrderGateway;
import net.local.poc.serviceorders.application.ports.publisher.EventDispacher;
import net.local.poc.serviceorders.application.ports.usecases.PlaceOrderUC;
import net.local.poc.serviceorders.domain.entities.PurchaseOrder;
import net.local.poc.serviceorders.domain.entities.PurchaseOrderItem;
import net.local.poc.serviceorders.domain.events.OrderPlacedEvent;

@Service
public class PlaceOrderService implements PlaceOrderUC {
    
    private final EventDispacher dispatcher;
    private final PurchaseOrderGateway gateway;

    public PlaceOrderService(PurchaseOrderGateway gateway, EventDispacher dispatcher) {
        this.dispatcher = dispatcher;
        this.gateway = gateway;
    }

    @Override
    public UUID execute(PlaceOrderInput input) {
        var newOrder = PurchaseOrder.placeOrder(input.customerId(), convertItems(input.items()));
        
        gateway.save(newOrder);

        dispatcher.dispatch(new OrderPlacedEvent(newOrder));

        return newOrder.getOrderId();
    }

    private List<PurchaseOrderItem> convertItems(List<PlaceOrderItemInput> items) {
        return items.stream()
                    .map(it -> new PurchaseOrderItem(it.productId(), it.quantity(), it.price()))
                    .toList();
    }
}
