package net.local.poc.orderorch.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import net.local.poc.orderorch.application.dto.OrchestratorResponse;
import net.local.poc.orderorch.application.ports.clients.InventoryClientPort;
import net.local.poc.orderorch.application.ports.clients.PaymentClientPort;
import net.local.poc.orderorch.application.workflow.api.Workflow;
import net.local.poc.orderorch.application.workflow.api.WorkflowStep;
import net.local.poc.orderorch.application.workflow.api.WorkflowStepStatus;
import net.local.poc.orderorch.application.workflow.flows.OrderWorkflow;
import net.local.poc.orderorch.application.workflow.flows.steps.InventoryStep;
import net.local.poc.orderorch.application.workflow.flows.steps.PaymentStep;
import net.local.poc.orderorch.domain.orders.OrderStatus;
import net.local.poc.orderorch.domain.orders.PurchaseOrder;
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
        Workflow orderWorkflow = new OrderWorkflow(List.of(
            new InventoryStep(purchaseOrder, inventoryPort),
            new PaymentStep(purchaseOrder, paymentPort)
        ));

        var result = Mono.just(orderWorkflow)
                         .flatMap(workflow -> processWorkflow(workflow.getSteps()))
                         .doOnSuccess(success -> {
                             if (success) {
                                 System.out.println("Workflow concluído com sucesso!");
                             } else {
                                 System.out.println("Workflow falhou. Reversão concluída.");
                             }
                         })
                         .doFinally(signalType -> {
                             System.out.println("Resultado final do workflow:");
                             orderWorkflow.getSteps().forEach(step -> 
                                 System.out.println(step.getClass().getSimpleName() + ": " + step.getStatus())
                             );
                         }).block();

        var resonse = result ? new OrchestratorResponse(purchaseOrder.getOrderId(), OrderStatus.ORDER_COMPLETED, "Success") : new OrchestratorResponse(purchaseOrder.getOrderId(), OrderStatus.ORDER_CANCELLED, "Cancelled");
        return Mono.just(resonse);
    }

    private Mono<Boolean> processWorkflow(List<WorkflowStep> steps) {
        return processSteps(steps, 0).onErrorResume(e -> {
                    System.out.println("Erro durante o processamento: " + e.getMessage());
                    return revertSteps(steps, steps.size() - 1).thenReturn(false);
               });
    }

    private Mono<Boolean> processSteps(List<WorkflowStep> steps, int index) {
        if (index >= steps.size()) {
            return Mono.just(true);
        }
        return steps.get(index).process()
            .flatMap(success -> {
                if (success) {
                    return processSteps(steps, index + 1);
                } else {
                    return revertSteps(steps, index).thenReturn(false);
                }
            });
    }

    private Mono<Void> revertSteps(List<WorkflowStep> steps, int index) {
        if (index < 0) {
            return Mono.empty();
        }
        WorkflowStep step = steps.get(index);
        return (step.getStatus() == WorkflowStepStatus.COMPLETE || step.getStatus() == WorkflowStepStatus.FAILED ? step.revert() : Mono.just(true)).then(revertSteps(steps, index - 1));
    }
}
