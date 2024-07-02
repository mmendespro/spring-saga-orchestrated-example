package net.local.poc.serviceorders.application.ports.gateways;

import java.util.Optional;
import java.util.UUID;

import net.local.poc.serviceorders.domain.entities.PurchaseOrder;

public interface PurchaseOrderGateway {
    Optional<PurchaseOrder> load(UUID orderId);
    void save(PurchaseOrder order);
    void update(PurchaseOrder order);
}
