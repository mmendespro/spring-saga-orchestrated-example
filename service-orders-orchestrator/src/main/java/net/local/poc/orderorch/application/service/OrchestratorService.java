package net.local.poc.orderorch.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import net.local.poc.orderorch.application.dto.OrchestratorResponse;
import net.local.poc.orderorch.application.ports.clients.InventoryClientPort;
import net.local.poc.orderorch.application.ports.clients.PaymentClientPort;
import net.local.poc.orderorch.application.workflow.api.Workflow;
import net.local.poc.orderorch.application.workflow.api.WorkflowStep;
import net.local.poc.orderorch.application.workflow.api.WorkflowStepStatus;
import net.local.poc.orderorch.application.workflow.exceptions.WorkflowException;
import net.local.poc.orderorch.application.workflow.flows.OrderWorkflow;
import net.local.poc.orderorch.application.workflow.flows.steps.InventoryStep;
import net.local.poc.orderorch.application.workflow.flows.steps.PaymentStep;
import net.local.poc.orderorch.domain.orders.OrderStatus;
import net.local.poc.orderorch.domain.orders.PurchaseOrder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrchestratorService {
    
    private final PaymentClientPort paymentPort;
    private final InventoryClientPort inventoryPort;

    public OrchestratorService(PaymentClientPort paymentPort, InventoryClientPort inventoryPort) {
        this.paymentPort = paymentPort;
        this.inventoryPort = inventoryPort;
    }

    public Mono<OrchestratorResponse> processOrderFlow(final PurchaseOrder purchaseOrder) {
        Workflow orderWorkflow = this.getOrderWorkflow(purchaseOrder);
        return Flux.fromStream(() -> orderWorkflow.getSteps().stream())
                   .flatMap(WorkflowStep::process)
                   .handle(((response, synchronousSink) -> {
                    if(response)
                        synchronousSink.next(true);
                    else
                        synchronousSink.error(new WorkflowException("create order failed!"));
                }))
                .then(Mono.fromCallable(() -> new OrchestratorResponse(purchaseOrder.getOrderId(), OrderStatus.ORDER_COMPLETED, "")))
                .onErrorResume(ex -> revertOrder(orderWorkflow, purchaseOrder, ex));
    }

    private Mono<OrchestratorResponse> revertOrder(final Workflow workflow, final PurchaseOrder purchaseOrder, Throwable ex){
        return Flux.fromStream(() -> workflow.getSteps().stream())
                .filter(wf -> wf.getStatus().equals(WorkflowStepStatus.COMPLETE))
                .flatMap(WorkflowStep::revert)
                .retry(3)
                .then(Mono.just(new OrchestratorResponse(purchaseOrder.getOrderId(), OrderStatus.ORDER_CANCELLED, ex.getMessage())));
    }

    private Workflow getOrderWorkflow(PurchaseOrder purchaseOrder) {
        WorkflowStep paymentStep = new PaymentStep(purchaseOrder, paymentPort);
        WorkflowStep inventoryStep = new InventoryStep(purchaseOrder, inventoryPort);
        return new OrderWorkflow(List.of(paymentStep, inventoryStep));
    }

}
