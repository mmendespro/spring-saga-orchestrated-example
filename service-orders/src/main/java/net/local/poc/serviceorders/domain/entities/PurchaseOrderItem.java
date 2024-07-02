package net.local.poc.serviceorders.domain.entities;

import java.math.BigDecimal;
import java.util.UUID;

public class PurchaseOrderItem {
    
    private final UUID productId;
    private final Long quantity;
    private final BigDecimal price;
    
    public PurchaseOrderItem(UUID productId, Long quantity, BigDecimal price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
