package net.local.poc.servicepayment.application.services;

import org.springframework.stereotype.Service;

import net.local.poc.servicepayment.application.ports.persistence.PaymentGateway;
import net.local.poc.servicepayment.application.ports.usecases.ProcessPaymentUC;
import net.local.poc.servicepayment.domain.PaymentStatus;

@Service
public class ProcessPaymentService implements ProcessPaymentUC {

    private final PaymentGateway gateway;

    public ProcessPaymentService(PaymentGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public PaymentOutput execute(PaymentInput input) {
        try {
            var customer = gateway.load(input.customerId()).orElseThrow(() -> new RuntimeException("Customer not found"));
            if(customer.getBalance().compareTo(input.amout()) < 0) {
                throw new RuntimeException("Customer has no funds to this operation");
            }
            gateway.save(customer.debit(input.amout()));
            return new PaymentOutput(input.customerId(), PaymentStatus.PAYMENT_APPROVED.name());
        } catch(RuntimeException ex) {
            return new PaymentOutput(input.customerId(), PaymentStatus.PAYMENT_REJECTED.name());
        }
    }
}
