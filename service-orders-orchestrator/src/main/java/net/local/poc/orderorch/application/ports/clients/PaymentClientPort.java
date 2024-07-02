package net.local.poc.orderorch.application.ports.clients;

import java.math.BigDecimal;
import java.util.UUID;

import net.local.poc.orderorch.domain.payment.PaymentStatus;
import reactor.core.publisher.Mono;

public interface PaymentClientPort {
    
    public Mono<PaymentOutput> processPayment(PaymentInput input);

    record PaymentInput(UUID customerId, BigDecimal amout){}
    record PaymentOutput(UUID customerId, PaymentStatus status){}
}
