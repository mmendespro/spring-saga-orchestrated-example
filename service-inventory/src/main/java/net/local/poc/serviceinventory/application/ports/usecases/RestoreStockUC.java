package net.local.poc.serviceinventory.application.ports.usecases;

import java.util.List;
import java.util.UUID;

public interface RestoreStockUC {
    
    void execute(RestoreStockInput input);

    record RestoreStockInput(UUID orderId, List<RestoreStockItem> items){}
    record RestoreStockItem(UUID productId, Long quantity) {}

}
