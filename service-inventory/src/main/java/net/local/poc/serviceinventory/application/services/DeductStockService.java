package net.local.poc.serviceinventory.application.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import net.local.poc.serviceinventory.application.ports.persistence.InventoryGateway;
import net.local.poc.serviceinventory.application.ports.usecases.DeductStockUC;
import net.local.poc.serviceinventory.domain.entities.InventoryStatus;

@Service
public class DeductStockService implements DeductStockUC {

    private final InventoryGateway gateway;

    public DeductStockService(InventoryGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public DeductStockOutput execute(DeductStockInput input) {
        var stockResult = new ArrayList<StockItemOutput>();
        input.items().forEach(item -> {
            gateway.load(item.productId()).ifPresentOrElse((p) -> {
                if(p.getQuantity() >= item.quantity()) {
                    var product = p.decrease(item.quantity());
                    gateway.save(product);
                    stockResult.add(new StockItemOutput(item.productId(), item.quantity(), InventoryStatus.AVAILABLE.name())); 
                } else {
                    stockResult.add(new StockItemOutput(item.productId(), item.quantity(), InventoryStatus.UNAVAILABLE.name()));
                }
            },
            () -> {
                stockResult.add(new StockItemOutput(item.productId(), item.quantity(), InventoryStatus.UNAVAILABLE.name()));
            });
        });

        return new DeductStockOutput(input.orderId(), stockResult);
    }
}
