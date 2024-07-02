package net.local.poc.serviceorders.application.ports.usecases;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PlaceOrderUC {
    
    UUID execute(PlaceOrderInput input);

    record PlaceOrderInput(UUID customerId, List<PlaceOrderItemInput> items){}
    record PlaceOrderItemInput(UUID productId, Long quantity, BigDecimal price){}
}
