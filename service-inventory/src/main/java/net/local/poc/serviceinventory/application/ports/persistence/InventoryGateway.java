package net.local.poc.serviceinventory.application.ports.persistence;

import java.util.Optional;
import java.util.UUID;

import net.local.poc.serviceinventory.domain.entities.ProductInventory;

public interface InventoryGateway {
    Optional<ProductInventory> load(UUID productId);
    void save(ProductInventory inventory);
}
