package net.local.poc.serviceinventory.application.services;

import org.springframework.stereotype.Service;

import net.local.poc.serviceinventory.application.ports.persistence.InventoryGateway;
import net.local.poc.serviceinventory.application.ports.usecases.RestoreStockUC;

@Service
public class RestoreStocService implements RestoreStockUC {
    
    private final InventoryGateway gateway;

    public RestoreStocService(InventoryGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void execute(RestoreStockInput input) {
        input.items().stream().forEach(item -> {
            var product = gateway.load(item.productId()).orElseThrow(RuntimeException::new);
            var restoreProduct = product.increase(item.quantity());
            gateway.save(restoreProduct);
        });
    }
}
