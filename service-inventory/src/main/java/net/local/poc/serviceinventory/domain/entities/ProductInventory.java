package net.local.poc.serviceinventory.domain.entities;

import java.util.UUID;

public class ProductInventory {

    private final UUID productId;
    private final Long quantity;
    
    public ProductInventory(UUID productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
    
    public ProductInventory increase(Long quantity) {
        return new ProductInventory(productId, this.quantity + quantity);
    }

    public ProductInventory decrease(Long quantity) {
        return new ProductInventory(productId, this.quantity - quantity);
    }
}
