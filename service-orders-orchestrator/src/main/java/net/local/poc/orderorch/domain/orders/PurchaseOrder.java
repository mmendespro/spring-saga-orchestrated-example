package net.local.poc.orderorch.domain.orders;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class PurchaseOrder {

    private final UUID orderId;
    private final UUID customerId;
    private final OrderStatus status;
    private final List<PurchaseOrderItem> items;
    
    public PurchaseOrder(UUID orderId, UUID customerId, OrderStatus status, List<PurchaseOrderItem> items) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.status = status;
        this.items = items;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<PurchaseOrderItem> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return items.stream()
                    .map(PurchaseOrderItem::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public PurchaseOrder cancel() {
        return new PurchaseOrder(orderId, customerId, OrderStatus.ORDER_CANCELLED, items);
    }

    public PurchaseOrder complete() {
        return new PurchaseOrder(orderId, customerId, OrderStatus.ORDER_COMPLETED, items);
    }

    @Override
    public String toString() {
        return "PurchaseOrder [orderId=" + orderId + ", customerId=" + customerId + ", status=" + status + ", items=" + items.size() + "]";
    }
    
}
