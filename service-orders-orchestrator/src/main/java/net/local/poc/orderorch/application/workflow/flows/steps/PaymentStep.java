package net.local.poc.orderorch.application.workflow.flows.steps;

import net.local.poc.orderorch.application.ports.clients.PaymentClientPort;
import net.local.poc.orderorch.application.ports.clients.PaymentClientPort.PaymentInput;
import net.local.poc.orderorch.application.workflow.api.WorkflowStep;
import net.local.poc.orderorch.application.workflow.api.WorkflowStepStatus;
import net.local.poc.orderorch.domain.orders.PurchaseOrder;
import net.local.poc.orderorch.domain.payment.PaymentStatus;
import reactor.core.publisher.Mono;

public class PaymentStep implements WorkflowStep {

    private final PurchaseOrder purchaseOrder;
    private final PaymentClientPort paymentPort;
    private WorkflowStepStatus stepStatus = WorkflowStepStatus.PENDING;

    public PaymentStep(PurchaseOrder purchaseOrder, PaymentClientPort paymentPort) {
        this.purchaseOrder = purchaseOrder;
        this.paymentPort = paymentPort;
    }

    @Override
    public Mono<Boolean> process() {
        return paymentPort.processPayment(new PaymentInput(purchaseOrder.getCustomerId(), purchaseOrder.getTotal()))
                          .map(r -> r.status().equals(PaymentStatus.PAYMENT_APPROVED))
                          .doOnNext(b -> this.stepStatus = b ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED);
    }

    @Override
    public Mono<Boolean> revert() {
        return Mono.just(true);
    }

    @Override
    public WorkflowStepStatus getStatus() {
        return this.stepStatus;
    }
    
}
