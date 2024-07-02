package net.local.poc.servicepayment.application.ports.usecases;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProcessPaymentUC {
    
    public PaymentOutput execute(PaymentInput input);

    record PaymentInput(UUID customerId, BigDecimal amout){}
    record PaymentOutput(UUID customerId, String status){}
}
