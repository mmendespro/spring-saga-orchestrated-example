package net.local.poc.orderorch.application.ports.clients;

import java.util.List;
import java.util.UUID;

import net.local.poc.orderorch.domain.inventory.InventoryStatus;
import reactor.core.publisher.Mono;

public interface InventoryClientPort {
    
    Mono<StockOutput> deduct(StockInput input);
    Mono<Void> restore(StockInput input);

    record StockInput(UUID orderId, List<StockItemInput> items){}
    record StockItemInput(UUID productId, Long quantity){}

    record StockOutput(UUID orderId, List<StockItemOutput> items){
        public boolean hasError() {
            return items().stream()
                          .filter(i -> i.status.equals(InventoryStatus.UNAVAILABLE))
                          .findAny()
                          .isPresent();
        }
    }
    record StockItemOutput(UUID productId, Long quantity, InventoryStatus status){}
}
