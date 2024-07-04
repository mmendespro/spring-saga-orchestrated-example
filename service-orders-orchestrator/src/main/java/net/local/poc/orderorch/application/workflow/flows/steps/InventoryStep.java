package net.local.poc.orderorch.application.workflow.flows.steps;

import net.local.poc.orderorch.application.ports.clients.InventoryClientPort;
import net.local.poc.orderorch.application.ports.clients.InventoryClientPort.StockInput;
import net.local.poc.orderorch.application.ports.clients.InventoryClientPort.StockItemInput;
import net.local.poc.orderorch.application.workflow.api.WorkflowStepStatus;
import net.local.poc.orderorch.domain.orders.PurchaseOrder;
import reactor.core.publisher.Mono;

public class InventoryStep extends BaseWorkflowStep {

    private final PurchaseOrder purchaseOrder;
    private final InventoryClientPort inventoryPort;
    private WorkflowStepStatus stepStatus = WorkflowStepStatus.PENDING;

    public InventoryStep(PurchaseOrder purchaseOrder, InventoryClientPort inventoryPort) {
        this.purchaseOrder = purchaseOrder;
        this.inventoryPort = inventoryPort;
    }

    @Override
    public Mono<Boolean> process() {
        var items = purchaseOrder.getItems().stream().map(pi -> new StockItemInput(pi.getProductId(), pi.getQuantity())).toList();
        var stockInput = new StockInput(purchaseOrder.getOrderId(), items);
        return inventoryPort.deduct(stockInput)
                            .map(r -> r.hasError())
                            .doOnNext(b -> this.stepStatus = b ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED);
    }

    @Override
    public Mono<Boolean> revert() {
        var items = purchaseOrder.getItems().stream().map(pi -> new StockItemInput(pi.getProductId(), pi.getQuantity())).toList();
        var stockInput = new StockInput(purchaseOrder.getOrderId(), items);
        return inventoryPort.restore(stockInput).map(r ->true).onErrorReturn(false);
    }

    @Override
    public WorkflowStepStatus getStatus() {
        return this.stepStatus;
    }
    
}
