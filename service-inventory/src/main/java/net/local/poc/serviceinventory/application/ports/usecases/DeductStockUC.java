package net.local.poc.serviceinventory.application.ports.usecases;

import java.util.List;
import java.util.UUID;

public interface DeductStockUC {
    
    DeductStockOutput execute(DeductStockInput input);

    record DeductStockInput(UUID orderId, List<StockItemInput> items){}
    record StockItemInput(UUID productId, Long quantity) {}
    
    record DeductStockOutput(UUID orderId, List<StockItemOutput> items) {}
    record StockItemOutput(UUID productId, Long quantity, String status) {}
}
